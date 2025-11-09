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

        async Task<List<Friend>> IFriendRepository.GetFrindsByUserIdAsync(int userId, int number, bool asTracking)
        {
            IQueryable<Friend> query = _context.Friends
                .Where(f => f.User1Id == userId || f.User2Id == userId)
                .Take(number);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }

            return await query.ToListAsync();
        }
    }
}
