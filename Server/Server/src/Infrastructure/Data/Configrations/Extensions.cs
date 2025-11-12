using Server.src.Infrastructure.Repositories;
using Server.src.Interfaces;
using Server.src.Repositories;
using Server.src.Services;
namespace Server.Data.Configrations
{
    public static class Extensions
    {
        public static void RegisterRepositories(this IServiceCollection services)
        {
            services.AddScoped<IUserRepository, UserRepository>();
            services.AddScoped<IMessageRepository, MessageRepository>();
            services.AddScoped<IFriendRepository, FriendRepository>();
        }

        public static void RegisterServices(this IServiceCollection services) { 
            services.AddScoped<IUserService, UserService>();
            services.AddScoped<IAuthService, AuthService>();
            services.AddScoped<IMessageService, MessageService>();
            services.AddScoped<IFriendService, FriendService>();
        }
    }
}
