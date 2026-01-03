using DotNetEnv; // �� �������ɒǉ�
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using Server.Data;
using Server.Data.Configrations;
using Server.src.Configrations;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Middlewares;
using Server.src.Services;
using Server.src.Signaling.Hubs;
using System.Reflection;
using System.Security.Cryptography.Xml;
using System.Text.Json;

//�A�v���̐ݒ��DI�������邽�߂̏���
var builder = WebApplication.CreateBuilder(args);

// 変更後
try
{
    Env.Load();
}
catch
{
    // Docker環境では.envファイルがないので無視
}

builder.Services.AddControllers(); //API�ŃR���g���[���g���܂����܂��Ȃ�
builder.Services.AddEndpointsApiExplorer(); //SwaggerUI�p��API�h�L�������g�\�z

builder.Services.AddCustomSwagger(); // Swaggerの設定

//Signalingの設定
builder.Services.AddRazorPages();
builder.Services.AddServerSideBlazor();
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

// JWT認証の設定
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

var app = builder.Build();//��L�݌v�}����ɍ\�z


//SwaggerUI�̃G���h�|�C���g��UI�ǂݍ��݁��\�z
if (app.Environment.IsDevelopment())
{
    app.UseStaticFiles();
    app.UseSwagger();
    app.UseSwaggerUI();
    // await DevelopmentDataSeeder.SeedAsync(app.Services); // 開発用データ
}
app.UseRouting();
app.UseHttpsRedirection(); //Http�Ń��N�G�X�g���ꂽ�Ƃ���Https�փ��_�C���N�g
app.UseMiddleware<ExceptionHandlingMiddleware>(); // // カスタム例外処理ミドルウェア

app.UseAuthentication(); // 認証ミドルウェア
app.UseAuthorization(); // 認可ミドルウェア

//Blazerの設定
app.MapRazorPages();
app.MapBlazorHub();
app.MapFallbackToPage("/_Host");
app.MapHub<MusicSessionHub>("/musicsessionhub");

app.MapControllers(); //controller�Œ�`���ꂽ���[�g��L���ɂ���(�R���g���[���[��L���ɂ���)

app.Run();//���s�I�I�I

