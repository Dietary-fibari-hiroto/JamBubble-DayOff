using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Repositories
{
    public class GuestRepository : IGuestRepository
    {
        public readonly AppDbContext _context;

        public GuestRepository(AppDbContext context)
        {
            _context = context;
        }

        // ゲストを追加
        public async Task<Guest> AddGuestAsync(Guest guest)
        {
            await _context.Guests.AddAsync(guest);
            await _context.SaveChangesAsync();
            return guest;
        }

        // ゲストを取得
        public async Task<Guest?> GetGuestByIdAsync(int guestId, bool asTracking = true)
        {
            IQueryable<Guest> query = _context.Guests
                .Where(g => g.Id == guestId);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ
            }

            return await query.FirstOrDefaultAsync();
        }
    }
}