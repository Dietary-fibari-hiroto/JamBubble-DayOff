using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface IUserRepository
    {
        Task<IEnumerable<User>> GetAllAsync();
        Task<User?> GetUserByIdAsync(int id, bool asTracking = true); // IDでユーザー取得
        Task<User?> GetUserByEmailAsync(string email); // Emailでユーザーを取得
        Task<User> AddUserAsync(User user); // 新しいユーザーを追加
        Task UpdateAsync(User user); // 既存のデータを更新
        Task DeleteUserAsync(User user); // IDでユーザーを削除
        Task<List<UserProvider>?> GetUserProvidersByUserIdAsync(int userId); // ユーザーIDからプロバイダーを取得
        Task<UserProvider?> GetUserProviderByIdsAsync(int userId, int provideId); // ユーザープロバイダーIDで取得
        Task DeleteUserProviderAsync(UserProvider userProvider); // ユーザープロバイダーを削除
    }
}
