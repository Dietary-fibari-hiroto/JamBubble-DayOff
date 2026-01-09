using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IRequestCacheService
    {
        Task<RequestCache?> AddRequestCacheAsync(RegisterRequestCacheRequestDto rcDto);
    }
}