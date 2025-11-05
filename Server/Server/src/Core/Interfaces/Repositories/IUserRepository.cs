using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface IUserRepository
    {
        Task<IEnumerable<User>> GetAllAsync();
        Task<User?> GetByIdAsync(int id); // IDでユーザーを取得
        Task<User?> GetByEmailAsync(string email); // Emailでユーザーを取得
        Task<User> AddAsync(User user); // 新しいユーザーを追加
        Task UpdateAsync(User user); // 既存のユーザーを更新
        Task DeleteAsync(User user); // IDでユーザーを削除
        Task<List<UserProvider>?> GetUserProvidersByUserIdAsync(int userId); // ユーザーIDからプロバイダーを取得
        Task<UserProvider?> GetUserProviderByIdsAsync(int userId, int provideId); // ユーザープロバイダーIDで取得
        Task<UserProvider> AddUserProviderAsync(UserProvider userProvider); // ユーザープロバイダーを追加
        Task DeleteUserProviderAsync(UserProvider userProvider); // ユーザープロバイダーを削除
    }
}
