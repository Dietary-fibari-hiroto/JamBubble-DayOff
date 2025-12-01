using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using System.Reflection;
using Client.Services.SpotifyServices.Auths;
using Client.Services.SpotifyServices.Apis;
using Client.Platforms.Android.Services;
namespace Client
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();

            var a = Assembly.GetExecutingAssembly();
            using var stream = a.GetManifestResourceStream("Client.appsettings.json");
            var config = new ConfigurationBuilder()
                .AddJsonStream(stream!)
                .Build();
            builder.Configuration.AddConfiguration(config);

            builder
                .UseMauiApp<App>()
                .ConfigureFonts(fonts =>
                {
                    fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                });

            builder.Services.AddMauiBlazorWebView();


            builder.Services.AddSingleton<HttpClient>();
            builder.Services.AddSingleton<SpotifyAuthService>();
            builder.Services.AddSingleton<SpotifyApiService>();

#if ANDROID
            builder.Services.AddSingleton<ISpotifyService, SpotifyService>();
#else
            // 他のプラットフォーム用のダミー実装
            builder.Services.AddSingleton<ISpotifyService, DummySpotifyService>();
#endif

#if DEBUG
            builder.Services.AddBlazorWebViewDeveloperTools();
    		builder.Logging.AddDebug();
#endif
#if DEBUG
            builder.Services.AddBlazorWebViewDeveloperTools();
            builder.Logging.AddDebug();
#endif


            return builder.Build();
        }
    }
}
