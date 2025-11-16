using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface ISessionService
    {
        Task<List<SessionResponseDto>> GetSessionsAsync(int userId);
        Task<SessionDetailResponseDto?> GetSessionAsync(int sessionId);
    }
}
