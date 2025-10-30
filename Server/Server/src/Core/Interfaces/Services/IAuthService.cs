using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IAuthService
    {
        Task<string?> LoginAsync(string email, string password);
        //bool VerifyPassword(User user, string password);
    }
}
