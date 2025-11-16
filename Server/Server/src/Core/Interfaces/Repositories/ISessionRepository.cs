using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface ISessionRepository
    {
        Task<List<Session>> GetSessionsByUserIdAsync(int userId);
        Task<Session?> GetSessionByIdAsync(int sessionId, bool asTracking = true);  
    }
}
