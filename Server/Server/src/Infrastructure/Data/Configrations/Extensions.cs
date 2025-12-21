using Server.src.Application.Services;
using Server.src.Interfaces;
using Server.src.Repositories;
using Server.src.Services;
using Server.src.Signaling.Hubs;
namespace Server.Data.Configrations
{
    /// <summary>
    /// DIコンテナに登録するための拡張メソッドをまとめたクラス
    /// </summary>
    public static class Extensions
    {
        public static void RegisterRepositories(this IServiceCollection services)
        {
            services.AddScoped<IUserRepository, UserRepository>();
            services.AddScoped<IMessageRepository, MessageRepository>();
            services.AddScoped<IFriendRepository, FriendRepository>();
            services.AddScoped<IFriendRequestRepository, FriendRequestRepository>();
            services.AddScoped<ISessionRepository, SessionRepository>();
            services.AddScoped<IFornowRepository, FornowRepository>();
            services.AddScoped<IFornowLikeRepository, FornowLikeRepository>();
            services.AddScoped<IFavoriteMusicSummaryRepository, FavoriteMusicSummaryRepository>();
            services.AddScoped<IProviderRepository, ProviderRepository>();
        }

        public static void RegisterServices(this IServiceCollection services) { 
            services.AddScoped<IUserService, UserService>();
            services.AddScoped<IAuthService, AuthService>();
            services.AddScoped<IMessageService, MessageService>();
            services.AddScoped<IFriendService, FriendService>();
            services.AddScoped<ISessionService, SessionService>();
            services.AddScoped<IFavoriteMusicService, FavoriteMusicService>();
            services.AddScoped<IProviderService, ProviderService>();

            //シグナリングハブのサービス登録
            services.AddSingleton<SessionManager>();
            //Spotify検索サービスの登録
            services.AddSingleton<SpotifySearchService>();
        }
    }
}
