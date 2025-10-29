using DotNetEnv; // �� �������ɒǉ�
using Microsoft.EntityFrameworkCore;
using Server.Data;
using Server.Data.Configrations; 
//�A�v���̐ݒ��DI�������邽�߂̏���
var builder = WebApplication.CreateBuilder(args);

Env.Load();

builder.Services.AddControllers(); //API�ŃR���g���[���g���܂����܂��Ȃ�
builder.Services.AddEndpointsApiExplorer(); //SwaggerUI�p��API�h�L�������g�\�z
builder.Services.AddSwaggerGen(); //SwaggerUI����
//���ϐ�����ڑ��������ǂ݂���
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

var app = builder.Build();//��L�݌v�}����ɍ\�z

app.MapGet("/", () => "Hello World!");

//SwaggerUI�̃G���h�|�C���g��UI�ǂݍ��݁��\�z
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection(); //Http�Ń��N�G�X�g���ꂽ�Ƃ���Https�փ��_�C���N�g
app.UseAuthorization(); //�F���~�h���E�F�A�p�C�v���C���ɒǉ�
app.MapControllers(); //controller�Œ�`���ꂽ���[�g��L���ɂ���(�R���g���[���[��L���ɂ���)

app.Run();//���s�I�I�I

