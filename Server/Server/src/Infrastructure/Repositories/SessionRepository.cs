using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;
using Server.src.DTOs;

namespace Server.src.Repositories
{
    public class SessionRepository : ISessionRepository
    {
        private readonly AppDbContext _context;
        public SessionRepository(AppDbContext context)
        {
            _context = context;
        }
        // １ユーザーのセッション一覧を取得
        public async Task<List<Session>> GetSessionsByUserIdAsync(int userId)
        {
            return await _context.Sessions
                .Where(s => s.UserId == userId)
                .OrderBy(s => s.CreatedAt)
                .ToListAsync();
        }

        // 1ユーザーの１つのセッションを詳しい情報を取得
        public async Task<Session?> GetSessionDetailByIdAsync(int sessionId, bool asTracking = true)
        {
            IQueryable<Session> query = _context.Sessions
                .Where(s => s.Id == sessionId)
                .Include(s => s.Provider)
                .Include(s => s.Scene)
                .Include(s => s.SessionSortSetting)
                .Include(s => s.SessionTag!)
                    .ThenInclude(st => st.Tag);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.FirstOrDefaultAsync();
        }

        // 1ユーザーの１つのセッションの簡易的な情報を取得
        public async Task<Session?> GetSessionSimpByIdAsync(int sessionId, bool asTracking = true)
        {
            IQueryable<Session> query = _context.Sessions
                .Where(s => s.Id == sessionId);
            
            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }
            return await query.FirstOrDefaultAsync();
        }

        // １ユーザーのアクティブな公開セッションを取得
        public async Task<Session?> GetActPubSessionsByUserIdAsync(int userId)
        {
            return await _context.Sessions
                .Where(s => s.UserId == userId && !s.Finished && s.IsPublic)
                .Include(s => s.User)
                .FirstOrDefaultAsync();
        }

        // 終了しておらず、公開されているセッションで参加人数が多い順に取得
        public async Task<List<SessionPopularResponseDto>> GetSessionPopularAsync(int n, int skip)
        {
            // SessionとGuestを結合して参加人数が多い順に取得
            var query = _context.Sessions
            .Where(s => !s.Finished && s.IsPublic)
            .GroupJoin(
                _context.Guests,
                session => session.Id,
                guest => guest.SessionId,
                (session, guests) => new
                {
                    Session = session,
                    GuestCount = guests.Count()
                }
            )
            .OrderByDescending(x => x.GuestCount)
            .Skip(skip)
            .Take(n)
            .Select(x => new SessionPopularResponseDto(x.Session, x.GuestCount));

            return await query.ToListAsync();
        }
    }
}
