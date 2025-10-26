using Microsoft.EntityFrameworkCore;
using Server.Data;

var builder = WebApplication.CreateBuilder(args);

// composeƒtƒ@ƒCƒ‹‚©‚çŠÂ‹«•Ï”‚ğ“Ç‚İ‚Ş
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection") 
    ?? throw new InvalidOperationException("Connection string 'DefaultConnection' not found.");

builder.Services.AddDbContext<AppDbContext>(optinons =>
    optinons.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString))); // compose‚©‚çÚ‘±î•ñ

var app = builder.Build();

app.MapGet("/", () => "Hello World!");

app.MapGet("/db-ping", async (AppDbContext db) =>
{
    try
    {
        await db.Database.ExecuteSqlRawAsync("SELECT 1");
        return "MySQLÚ‘±OK";
    }
    catch (Exception ex)
    {
        return $"MySQLÚ‘±NG: {ex.Message}";
    }
});
app.Run();