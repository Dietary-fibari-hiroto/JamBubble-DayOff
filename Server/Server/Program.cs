using DotNetEnv;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using Server.Data;
using Server.Data.Configrations;
using Server.Pages;
using Server.src.Configrations;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Middlewares;
using Server.src.Services;
using Server.src.Signaling.Hubs;
using System.Reflection;
using System.Security.Cryptography.Xml;
using System.Text.Json;

//アプリの設定・DIを準備するための準備
var builder = WebApplication.CreateBuilder(args);

// .envファイル読み込み
try
{
    Env.Load();
}
catch
{
    // Docker環境では.envファイルがないので無視
}

builder.Services.AddControllers(); //APIでコントローラーを使うなら
builder.Services.AddEndpointsApiExplorer(); //SwaggerUI用のAPIドキュメントを構築

builder.Services.AddCustomSwagger(); // Swaggerの設定

// ===== 新形式Blazor設定 =====
// Razor Componentsを追加（新方式）
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents(); // SignalR経由のインタラクティブモード

// SignalRは引き続き使える
builder.Services.AddSignalR();


var connectionString =
    Environment.GetEnvironmentVariable("SQLSERVER_CONNECTION")
    ?? Environment.GetEnvironmentVariable("ConnectionStrings__DefaultConnection")
    ?? Environment.GetEnvironmentVariable("CONNECTION_STRING")
    ?? throw new InvalidOperationException("Connection string not found.");

builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlServer(
        connectionString,
        sqlServerOptions => sqlServerOptions.EnableRetryOnFailure(
            maxRetryCount: 5,
            maxRetryDelay: TimeSpan.FromSeconds(10),
            errorNumbersToAdd: null
        )
    ));
builder.Services.RegisterServices();
builder.Services.RegisterRepositories();

// DIコンテナに登録
builder.Services.AddScoped<IPasswordHasher<User>, PasswordHasher<User>>();

// 定期実行サービスの登録
builder.Services.AddHostedService<TimedHostedService>();

builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new Microsoft.IdentityModel.Tokens.TokenValidationParameters
        {
            ValidIssuer = Environment.GetEnvironmentVariable("JWT__ISSUER")!,
            ValidAudience = Environment.GetEnvironmentVariable("JWT__AUDIENCE")!,
            IssuerSigningKey = new Microsoft.IdentityModel.Tokens.SymmetricSecurityKey(
                System.Text.Encoding.UTF8.GetBytes(Environment.GetEnvironmentVariable("JWT__KEY")!)
            ),
            ValidateIssuer = true, //発行者の検証
            ValidateAudience = true, //対象者の検証
            ValidateLifetime = false, //有効期限の検証
            ValidateIssuerSigningKey = true, //署名キーの検証
        };
    }
    );

var app = builder.Build(); // 上記設計図から実際に構築


//SwaggerUIのエンドポイントやUI読み込み・構築
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
    // await DevelopmentDataSeeder.SeedAsync(app.Services); // 開発用データ
}

// 静的ファイル（CSS/JS）はルーティングより先に！
app.UseStaticFiles();

app.UseRouting();
app.UseHttpsRedirection(); // HttpでリクエストされたときHttpsへリダイレクト
app.UseMiddleware<ExceptionHandlingMiddleware>(); // カスタム例外処理ミドルウェア

app.UseAuthentication(); // 認証ミドルウェア
app.UseAuthorization(); // 認可ミドルウェア

// アンチフォージェリー（CSRF対策）はAuthentication/Authorizationの後！
app.UseAntiforgery();

// ===== 新形式Blazor設定 =====
// Razor Componentsのマッピング（App.razorがエントリーポイント）
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode(); // SignalR経由のインタラクティブモード

// SignalR Hubは引き続き使える
app.MapHub<MusicSessionHub>("/musicsessionhub");

app.MapControllers(); // controllerで定義されたルートを有効にする

app.Run(); // 実行！！！





/*
 * 
 * # ビルドステージ
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# csprojファイルをコピーして依存関係を復元
COPY ["Server.csproj", "./"]
RUN dotnet restore "Server.csproj"

# 残りのソースコードをコピーしてビルド
COPY . .
RUN dotnet build "Server.csproj" -c Release -o /app/build

# 公開ステージ
FROM build AS publish
RUN dotnet publish "Server.csproj" -c Release -o /app/publish /p:UseAppHost=false

# ランタイムステージ
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app

# 非rootユーザーで実行（重要！）
USER app

# ポート設定
EXPOSE 8080
EXPOSE 8081

# ASP.NET Coreのポート設定を明示
ENV ASPNETCORE_URLS=http://+:8080

COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Server.dll"]
 */

/**
 * 
 * # デバッグ コンテナーをカスタマイズする方法と、Visual Studio がこの Dockerfile を使用してより高速なデバッグのためにイメージをビルドする方法については、https://aka.ms/customizecontainer をご覧ください。

# このステージは、VS から高速モードで実行するときに使用されます (デバッグ構成の既定値)
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
USER $APP_UID
WORKDIR /app
EXPOSE 8080
EXPOSE 8081


# このステージは、サービス プロジェクトのビルドに使用されます
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
ARG BUILD_CONFIGURATION=Release
WORKDIR /src
COPY ["Server/Server.csproj", "Server/"]
RUN dotnet restore "./Server/Server.csproj"
COPY . .
WORKDIR "/src/Server"
RUN dotnet build "./Server.csproj" -c $BUILD_CONFIGURATION -o /app/build

# このステージは、最終ステージにコピーするサービス プロジェクトを公開するために使用されます
FROM build AS publish
ARG BUILD_CONFIGURATION=Release
RUN dotnet publish "./Server.csproj" -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

# このステージは、運用環境または VS から通常モードで実行している場合に使用されます (デバッグ構成を使用しない場合の既定)
FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Server.dll"]
 */