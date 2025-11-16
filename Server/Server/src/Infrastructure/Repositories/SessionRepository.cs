using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Repositories
{
    public class SessionRepository : ISessionRepository
    {
        private readonly AppDbContext _context;
        public SessionRepository(AppDbContext context)
        {
            _context = context;
        }
        public async Task<List<Session>> GetSessionsByUserIdAsync(int userId)
        {
            return await _context.Sessions
                .Where(s => s.UserId == userId)
                .ToListAsync();
        }

        public async Task<Session?> GetSessionByIdAsync(int sessionId, bool asTracking = true)
        {
            IQueryable<Session> query = _context.Sessions
                .Include(s => s.Provider)
                .Include(s => s.Scene)
                .Include(s => s.SessionSortSetting)
                .Include(s => s.SessionTag!)
                    .ThenInclude(st => st.Tag);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.FirstOrDefaultAsync(s => s.Id == sessionId);
        }
    }
}
