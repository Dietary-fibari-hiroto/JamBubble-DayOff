using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Repositories
{
    public class FriendRepository : IFriendRepository
    {
        public readonly AppDbContext _context;
        public FriendRepository(AppDbContext context)
        {
            _context = context;
        }

        // ユーザーIDでフレンド一覧を取得
        public async Task<List<Friend>> GetFriendsByUserIdAsync(int userId, int number, bool asTracking = true, int skip = 0)
        {
            IQueryable<Friend> query = _context.Friends
                .Where(f => f.User1Id == userId || f.User2Id == userId)
                .Include(f => f.User1)
                .Include(f => f.User2)
                .OrderBy(f => f.CreatedAt)
                .Skip(skip)
                .Take(number);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }

            return await query.ToListAsync();
        }


        // ユーザーIDとフレンドIDでフレンドを取得
        public async Task<Friend?> GetFriendByUserIdAsync(int userId, int friendId, bool asTracking = true)
        {
            var id1 = Math.Min(userId, friendId);
            var id2 = Math.Max(userId, friendId);
            IQueryable<Friend> query = _context.Friends
                .Where(f => f.User1Id == id1 && f.User2Id == id2)
                .Include(f => f.User1)
                .Include(f => f.User2);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }

            return await query.FirstOrDefaultAsync();
        }

        // フレンドを追加
        public async Task<Friend> AddFriendAsync(Friend friend)
        {
            _context.Friends.Add(friend);
            await _context.SaveChangesAsync();
            return friend;
        }

        // フレンドを削除
        public async Task DeleteFriendAsync(Friend friend)
        {
            _context.Friends.Remove(friend);
            await _context.SaveChangesAsync();
        }

        // ユーザーIDとフレンドIDでフレンド関係か確認
        public async Task<bool> IsFriendAsync(int userId, int friendId)
        {
            var id1 = Math.Min(userId, friendId);
            var id2 = Math.Max(userId, friendId);
            return await _context.Friends.AnyAsync(f => f.User1Id == id1 && f.User2Id == id2);
        }
    }
}
