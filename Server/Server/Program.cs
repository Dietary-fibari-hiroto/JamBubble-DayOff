using DotNetEnv; // �� �������ɒǉ�
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using Server.Data;
using Server.Data.Configrations;
using Server.src.Configrations;
using System.Reflection;
using System.Security.Cryptography.Xml;
using Server.src.DTOs;
using Server.src.Entities;
using Microsoft.AspNetCore.Identity;
using Server.src.Middlewares;

//�A�v���̐ݒ��DI�������邽�߂̏���
var builder = WebApplication.CreateBuilder(args);

Env.Load();

builder.Services.AddControllers(); //API�ŃR���g���[���g���܂����܂��Ȃ�
builder.Services.AddEndpointsApiExplorer(); //SwaggerUI�p��API�h�L�������g�\�z

builder.Services.AddCustomSwagger(); // Swaggerの設定

var connectionString =
    Environment.GetEnvironmentVariable("MYSQL_CONNECTION")
    ?? Environment.GetEnvironmentVariable("ConnectionStrings__DefaultConnection")
    ?? Environment.GetEnvironmentVariable("CONNECTION_STRING")
    ?? throw new InvalidOperationException("Connection string not found.");

builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseMySql(
        connectionString,
        new MySqlServerVersion(new Version(8, 0, 38)),
        mySqlOptions => mySqlOptions.EnableRetryOnFailure(
    maxRetryCount: 5,               // �ő僊�g���C��
    maxRetryDelay: TimeSpan.FromSeconds(10), // ���g���C�Ԋu�̍ő厞��
    errorNumbersToAdd: null          // �ǉ��Ń��g���C�Ώۂɂ���G���[�ԍ� (null�ł�OK)
)
    ));
builder.Services.RegisterServices();
builder.Services.RegisterRepositories();

// DIコンテナに登録
builder.Services.AddScoped<IPasswordHasher<User>, PasswordHasher<User>>();

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
            ValidateIssuer = true, // 発行者の検証
            ValidateAudience = true, // 対象者の検証
            ValidateLifetime = false, // 有効期限の検証
            ValidateIssuerSigningKey = true, // 署名キーの検証
        };
    }
    );

var app = builder.Build();//��L�݌v�}����ɍ\�z

app.MapGet("/", () => "Hello World!");

//SwaggerUI�̃G���h�|�C���g��UI�ǂݍ��݁��\�z
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection(); //Http�Ń��N�G�X�g���ꂽ�Ƃ���Https�փ��_�C���N�g

app.UseMiddleware<ExceptionHandlingMiddleware>(); // // カスタム例外処理ミドルウェア

app.UseAuthentication(); // 認証ミドルウェア
app.UseAuthorization(); // 認可ミドルウェア

app.MapControllers(); //controller�Œ�`���ꂽ���[�g��L���ɂ���(�R���g���[���[��L���ɂ���)

app.Run();//���s�I�I�I

