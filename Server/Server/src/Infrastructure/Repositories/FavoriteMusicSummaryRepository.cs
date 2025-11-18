using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Repositories
{
    public class FavoriteMusicSummaryRepository : IFavoriteMusicSummaryRepository
    {
        private readonly AppDbContext _context;
        public FavoriteMusicSummaryRepository(AppDbContext context)
        {
            _context = context;
        }

        // お気に入り音楽の集計を行い更新する
        public async Task AggregeteFavoriteMusicAsync()
        {
            var aggregatedData = await _context.FavoriteMusics
                        .Where(fm => fm.MusicId != null)
                        .GroupBy(fm => fm.MusicId)
                        .Select(group => new FavoriteMusicSummary
                        {
                            MusicId = group.Key!,
                            Count = group.Count()
                        })
                        .ToListAsync();

            _context.FavoriteMusicSummaries.RemoveRange(_context.FavoriteMusicSummaries);
            await _context.FavoriteMusicSummaries.AddRangeAsync(aggregatedData);
            await _context.SaveChangesAsync();
        }
    }
}
