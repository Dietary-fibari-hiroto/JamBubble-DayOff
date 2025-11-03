using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;
namespace Server.src.Repositories
{
    public class UserRepository : IUserRepository
    {
        private readonly AppDbContext _context;
        public UserRepository(AppDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<User>> GetAllAsync() => await _context.Users.ToListAsync();
        // IDでユーザーを取得
        // public async Task<User?> GetByIdAsync(int id) => await _context.Users.FindAsync(id);
        // IDでユーザーのすべての情報を取得
        public async Task<User?> GetByIdAsync(int id)
        {
            var user = await _context.Users
                .Include(u => u.UserHistory) // 先行読み込み
                .AsNoTracking() // 読み込み専用
                .FirstOrDefaultAsync(u => u.Id == id);
            return user;
        }
        // Emailでユーザーを取得
        public async Task<User?> GetByEmailAsync(string email) => await _context.Users.FirstOrDefaultAsync(u => u.Email == email);
        // ユーザー登録
        public async Task<User?> AddAsync(User user)
        {
            // ユーザーの履歴も同時に作成
            user.UserHistory = new UserHistory
            {
                User = user,
                SessionCount = 0
            };
            
            var entry = await _context.Users.AddAsync(user);
            await _context.SaveChangesAsync();
            return entry.Entity;
        }
        // ユーザー情報更新
        public async Task<User?> UpdateAsync(User user)
        {
            _context.Users.Update(user);
            await _context.SaveChangesAsync();
            return user;
        }
        // ユーザー情報の削除
        public async Task DeleteAsync(User user)
        {
            _context.Users.Remove(user);
            await _context.SaveChangesAsync();
        }
    }
}
