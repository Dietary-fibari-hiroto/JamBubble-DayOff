using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Repositories
{
    public class FornowLikeRepository : IFornowLikeRepository
    {
        private readonly AppDbContext _context;
        public FornowLikeRepository(AppDbContext context)
        {
            _context = context;
        }

        // FornowLikeをFornowIdとUserIdで取得
        public async Task<FornowLike?> GetFornowLikeByIdsAsync(int fornowId, int userId, bool asTracking = true)
        {
            IQueryable<FornowLike> query = _context.FornowLikes
                .Where(fl => fl.FornowId == fornowId && fl.UserId == userId);
            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.FirstOrDefaultAsync();
        }

        // FornowLikeを追加
        public async Task<FornowLike> AddFornowLikeAsync(FornowLike fornowLike)
        {
            var entity = await _context.FornowLikes.AddAsync(fornowLike);
            await _context.SaveChangesAsync();
            return entity.Entity;
        }

        // いいね数をカウント
        public async Task<int> CountLikesByFornowIdAsync(int fornowId)
        {
            return await _context.FornowLikes
                .Where(fl => fl.FornowId == fornowId)
                .CountAsync();
        }
    }
}
