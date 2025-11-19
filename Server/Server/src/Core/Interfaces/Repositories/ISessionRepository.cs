using Server.src.Entities;
using Server.src.DTOs;
namespace Server.src.Interfaces
{
    public interface ISessionRepository
    {
        Task<List<Session>> GetSessionsByUserIdAsync(int userId);
        Task<Session?> GetSessionDetailByIdAsync(int sessionId, bool asTracking = true);  
        Task<Session?> GetSessionSimpByIdAsync(int sessionId, bool asTracking = true);
        Task<Session?> GetActPubSessionsByUserIdAsync(int userId);
        Task<List<Session>> GetSavePossibleSessionsAsync(int userId);
        Task<List<SessionPopularResponseDto>> GetSessionPopularAsync(int n, int skip);
        Task<Session> AddSessionAsync(Session session);
        Task UpdateSessionAsync(Session session);
    }
}
