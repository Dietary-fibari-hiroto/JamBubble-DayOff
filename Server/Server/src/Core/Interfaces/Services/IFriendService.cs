using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFriendService
    {
        public Task<List<FriendResposeDto>> GetFriendsAsync(int userId, int number);
        public Task<List<Session>> GetFriendSessionsAsync(int userId, int friendId);
        public Task<List<Fornow>> GetFriendFornowsAsync(int userId, int friendId);
        public Task<Fornow> GetFriendFornowAsync(int userId, int friendId, int id);
        public Task<bool> SetFavoriteToFornowAsync(int userId, int friendId, int id);
        public Task<bool> RequestFriendAsync(int userId, int friendId);
        public Task<bool> ProprietyFriendAsync(int userId, int friendId);
    }
}
