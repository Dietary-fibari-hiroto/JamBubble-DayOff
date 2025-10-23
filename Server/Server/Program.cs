using Microsoft.EntityFrameworkCore;
using Server.DbContexts;

var builder = WebApplication.CreateBuilder(args);

// composeファイルから環境変数を読み込む
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection") 
    ?? throw new InvalidOperationException("Connection string 'DefaultConnection' not found.");

builder.Services.AddDbContext<SampleDbcontext>(optinons =>
    //optinons.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString))); composeから接続情報を
    optinons.UseMySql(builder.Configuration.GetConnectionString("MySQLSampleContext"), new MySqlServerVersion(new Version(8, 4, 2))));　// appsetting.jsonから接続情報を

var app = builder.Build();

app.MapGet("/", () => "Hello World!");

app.MapGet("/db-ping", async (SampleDbcontext db) =>
{
    try
    {
        await db.Database.ExecuteSqlRawAsync("SELECT 1");
        return "MySQL接続OK";
    }
    catch (Exception ex)
    {
        return $"MySQL接続NG: {ex.Message}";
    }
});
app.Run();
