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
        }

        public static void RegisterServices(this IServiceCollection services) { 
            services.AddScoped<IUserService, UserService>();
        }
    }
}
