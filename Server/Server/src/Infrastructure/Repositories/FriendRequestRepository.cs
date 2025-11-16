using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Repositories
{
    public class FriendRequestRepository : IFriendRequestRepository
    {
        private readonly AppDbContext _context;
        public FriendRequestRepository(AppDbContext context)
        {
            _context = context;
        }

        // 送信者IDと受信者IDでフレンドリクエストを取得
        public async Task<FriendRequest?> GetFriendRequestByIdsAsync(int sendUerId, int passUserId, bool asTracking = true)
        {
            IQueryable<FriendRequest> query = _context.FriendRequests
                .Where(fr => fr.SendUserId == sendUerId && fr.PassUserId == passUserId);
            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.FirstOrDefaultAsync();
        }

        // フレンドリクエストを追加
        public async Task<FriendRequest> AddFriendRequestAsync(FriendRequest friendRequest)
        {
            _context.FriendRequests.Add(friendRequest);
            await _context.SaveChangesAsync();
            return friendRequest;
        }

        // フレンドリクエストを更新
        public async Task UpdateAsync(FriendRequest friendRequest)
        {
            await _context.SaveChangesAsync();
        }

        // フレンドリクエストを削除
        public async Task DeleteAsync(FriendRequest friendRequest)
        {
            _context.FriendRequests.Remove(friendRequest);
            await _context.SaveChangesAsync();
        }
    }
}
