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
        public async Task<User?> GetUserByIdAsync(int id)
        {
            var user = await _context.Users
                .Include(u => u.UserHistory) // 先行読み込み
                .Include(u => u.FavoriteMusic)
                .Include(u => u.UserProviders)
                //.AsNoTracking() // 読み込み専用 // ここで追跡しておくとupdate時に変更保存するだけで済むらしい
                .FirstOrDefaultAsync(u => u.Id == id);
            return user;
        }
        // Emailでユーザーを取得
        public async Task<User?> GetUserByEmailAsync(string email) => await _context.Users.FirstOrDefaultAsync(u => u.Email == email);
        // ユーザー登録
        public async Task<User> AddUserAsync(User user)
        {
            var entry = await _context.Users.AddAsync(user);
            await _context.SaveChangesAsync();
            return entry.Entity;
        }
        // ユーザー情報更新
        public async Task UpdateAsync(User user)
        {
            // なんか情報取得時に追跡しているのでupdateはいらないらしい
            //_context.Users.Update(user);
            await _context.SaveChangesAsync();
        }
        // ユーザー情報の削除
        public async Task DeleteUserAsync(User user)
        {
            _context.Users.Remove(user);
            await _context.SaveChangesAsync();
        }

        public async Task<List<UserProvider>?> GetUserProvidersByUserIdAsync(int userId)
        {
            return await _context.UserProviders
                .Where(up => up.UserId == userId)
                .ToListAsync();
        }

        public async Task<UserProvider?> GetUserProviderByIdsAsync(int userId, int provideId)
        {
            return await _context.UserProviders
                .FirstOrDefaultAsync(up => up.UserId == userId && up.ProviderId == provideId);
        }
        
        public async Task DeleteUserProviderAsync(UserProvider userProvider)
        {
            _context.UserProviders.Remove(userProvider);
            await _context.SaveChangesAsync();
        }
    }
}
