using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Infrastructure.Repositories
{
    public class FriendRepository : IFriendRepository
    {
        public readonly AppDbContext _context;
        public FriendRepository(AppDbContext context)
        {
            _context = context;
        }

        public async Task<List<Friend>> GetFriendsByUserIdAsync(int userId, int number, bool asTracking = true)
        {
            IQueryable<Friend> query = _context.Friends
                .Where(f => f.User1Id == userId || f.User2Id == userId)
                .Include(f => f.User1)
                .Include(f => f.User2)
                .Take(number);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }

            return await query.ToListAsync();
        }

        public async Task<Friend?> GetFriendByUserIdAsync(int userId, int friendId, bool asTracking = true)
        {
            IQueryable<Friend> query = _context.Friends
                .Where(f => (f.User1Id == userId && f.User2Id == friendId) || (f.User2Id == userId && f.User1Id == friendId))
                .Include(f => f.User1)
                .Include(f => f.User2);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }

            return await query.FirstOrDefaultAsync();
        }
    }
}
