using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFriendRepository
    {
        public Task<List<Friend>> GetFriendsByUserIdAsync(int userId, int number, bool asTracking = true, int skip = 0);
        public Task<Friend?> GetFriendByUserIdAsync(int userId, int friendId, bool asTracking = true);
        public Task<Friend> AddFriendAsync(Friend friend);
        public Task DeleteFriendAsync(Friend friend);
        public Task<bool> IsFriendAsync(int userId, int friendId);
    }
}
