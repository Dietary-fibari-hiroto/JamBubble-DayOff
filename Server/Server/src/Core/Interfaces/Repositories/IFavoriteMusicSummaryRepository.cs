using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFavoriteMusicSummaryRepository
    {
        public Task AggregeteFavoriteMusicAsync();
    }
}
