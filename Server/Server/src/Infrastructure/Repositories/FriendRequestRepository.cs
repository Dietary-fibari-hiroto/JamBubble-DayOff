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

        // ID1とID2でフレンドリクエストを取得
        public async Task<FriendRequest?> GetFriendRequestByIdsAsync(int id1, int id2, bool asTracking = true)
        {
            IQueryable<FriendRequest> query = _context.FriendRequests
                .Where(fr => fr.User1Id == id1 && fr.User2Id == id2);
            if (!asTracking)
            {
                query = query.AsNoTracking();
            }
            return await query.FirstOrDefaultAsync();
        }

        // 送信者IDでフレンドリクエスト一覧を取得
        public async Task<List<FriendRequest>> GetFriendRequestsBySenderUserIdAsync(int senderUserId, bool asTracking = true)
        {
            IQueryable<FriendRequest> query = _context.FriendRequests
                .Where(fr => fr.SendUserId == senderUserId)
                .Include(fr => fr.PassUser)
                .OrderBy(fr => fr.CreatedAt);
            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.ToListAsync();
        }

        // 受信者IDでフレンドリクエスト一覧を取得
        public async Task<List<FriendRequest>> GetFriendRequestsByReceiverUserIdAsync(int receiverUserId, bool asTracking = true)
        {
            IQueryable<FriendRequest> query = _context.FriendRequests
                .Where(fr => fr.PassUserId == receiverUserId)
                .Include(fr => fr.SendUser)
                .OrderBy(fr => fr.CreatedAt);
            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.ToListAsync();
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

        // フレンドリクエストの存在確認
        public async Task<bool> IsFriendRequestExistAsync(int sendUserId, int targetUserId)
        {
            return await _context.FriendRequests
                .AnyAsync(fr => fr.SendUserId == sendUserId && fr.PassUserId == targetUserId);
        }
    }
}
