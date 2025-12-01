using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface ISessionService
    {
        Task<List<SessionResponseDto>> GetSessionsAsync(int userId);
        Task<SessionDetailResponseDto?> GetSessionAsync(int sessionId);
        Task<List<SessionPopularResponseDto>> GetSessionPopularAsync(int n, int skip);
        Task<List<SessionResponseDto>> GetSessionActPubFrinedAsync(int userId, int n, int skip);
        Task<List<SessionResponseDto>> GetSavePossibleSessionsAsync(int userId);
        Task<SessionDetailResponseDto?> AddSessionAsync(SessionRequestDto sessionReqDto, int userId);
    }
}
