using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface IRequestCacheRepository
    {
        Task<RequestCache> AddRequestCacheAsync(RequestCache rc);
    }
}