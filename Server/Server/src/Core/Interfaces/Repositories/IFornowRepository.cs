using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface IFornowRepository
    {
        Task<Fornow?> GetFornowByUserIdAsync(int userId, bool asTacking = true);
        Task<Fornow?> GetFornowByIdAsync(int fornowId, bool asTracking = true);
        Task<Fornow> AddFornowAsync(Fornow fornow);
        Task UpdateAsync(Fornow fornow);
    }
}
