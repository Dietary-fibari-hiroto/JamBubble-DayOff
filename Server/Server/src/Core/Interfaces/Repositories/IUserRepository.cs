using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface IUserRepository
    {
        Task<IEnumerable<User>> GetAllAsync();
        Task<User?> GetByIdAsync(int id); // IDでユーザーを取得
        Task<User?> GetByEmailAsync(string email); // Emailでユーザーを取得
        Task<User?> AddAsync(User user); // 新しいユーザーを追加
        Task<User?> UpdateAsync(User user); // 既存のユーザーを更新
        Task DeleteAsync(User user); // IDでユーザーを削除
    }
}
