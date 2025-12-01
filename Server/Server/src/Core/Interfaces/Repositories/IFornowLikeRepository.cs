using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFornowLikeRepository
    {
        Task<FornowLike?> GetFornowLikeByIdsAsync(int fornowId, int userId, bool asTracking = true);
        Task<FornowLike> AddFornowLikeAsync(FornowLike fornowLike);
        Task<int> CountLikesByFornowIdAsync(int fornowId);
    }
}
