using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFavoriteMusicService
    {
        public Task<List<FavoriteMusicSummary>> GetFavoriteMusicRankingAsync(int n, int skip);
    }
}
