using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFriendRepository
    {
        public Task<List<Friend>> GetFrindsByUserIdAsync(int userId, int number, bool asTracking = true);
    }
}
