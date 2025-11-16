using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;


namespace Server.src.Repositories
{
    public class FornowRepository : IFornowRepository
    {
        public readonly AppDbContext _context;
        public FornowRepository(AppDbContext context)
        {
            _context = context;
        }

        // FornowをUserIdで取得
        public async Task<Fornow?> GetFornowByUserIdAsync(int userId, bool asTracking = true)
        {
            IQueryable<Fornow> query = _context.Fornows
                .Where(f => f.UserId == userId)
                .Include(f => f.User);
            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.FirstOrDefaultAsync();
        }

        // FornowをIdで取得
        public async Task<Fornow?> GetFornowByIdAsync(int fornowId, bool asTracking = true)
        {
            IQueryable<Fornow> query = _context.Fornows
                .Where(f => f.Id == fornowId)
                .Include(f => f.User);
            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.FirstOrDefaultAsync();
        }

        public async Task<Fornow> AddFornowAsync(Fornow fornow)
        {
            var entity = await _context.Fornows.AddAsync(fornow);
            await _context.SaveChangesAsync();
            return entity.Entity;
        }

        public async Task UpdateAsync(Fornow fornow)
        {
            _context.Fornows.Update(fornow);
            await  _context.SaveChangesAsync();
        }
    }
}
