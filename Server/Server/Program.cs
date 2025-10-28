using DotNetEnv; // ← これを上に追加
using Microsoft.EntityFrameworkCore;
using Server.Data;
using Server.Data.Configrations; 
//アプリの設定やDI注入するための準備
var builder = WebApplication.CreateBuilder(args);

Env.Load();

builder.Services.AddControllers(); //APIでコントローラ使いまっせまじない
builder.Services.AddEndpointsApiExplorer(); //SwaggerUI用のAPIドキュメント構築
builder.Services.AddSwaggerGen(); //SwaggerUI実装
//環境変数から接続文字列を読みこむ
var connectionString = Environment.GetEnvironmentVariable("MYSQL_CONNECTION");
//Mysqlの登録(DBとやり取りするためのやつ)
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseMySql(
        connectionString,
        new MySqlServerVersion(new Version(8, 0, 38)),
        mySqlOptions => mySqlOptions.EnableRetryOnFailure(
    maxRetryCount: 5,               // 最大リトライ回数
    maxRetryDelay: TimeSpan.FromSeconds(10), // リトライ間隔の最大時間
    errorNumbersToAdd: null          // 追加でリトライ対象にするエラー番号 (nullでもOK)
)
    ));
builder.Services.RegisterServices();
builder.Services.RegisterRepositories();

var app = builder.Build();//上記設計図を基に構築

app.MapGet("/", () => "Hello World!");

//SwaggerUIのエンドポイントとUI読み込み＆構築
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection(); //HttpでリクエストされたときにHttpsへリダイレクト
app.UseAuthorization(); //認可をミドルウェアパイプラインに追加
app.MapControllers(); //controllerで定義されたルートを有効にする(コントローラーを有効にする)

app.Run();//実行！！！

