using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFriendService
    {
        public Task<List<FriendResposeDto>> GetFriendsAsync(int userId, int number, int skip = 0);
        public Task<List<SessionResponseDto>> GetFriendSessionsAsync(int friendId);
        public Task<List<FornowSimpResponseDto>> GetFriendsFornowsAsync(int userId, int n, int skip = 0);
        public Task<FornowDetailResponseDto?> GetFriendFornowDetailAsync(int userId, int fornowId);
        public Task<bool> SetLikeToFornowAsync(int userId, int friendId);
        public Task<FriendRequestSndRcvDto> GetfriendRequestSndRcv(int userId);
        public Task<bool> RequestFriendAsync(int userId, int friendId);
        public Task<bool> ProprietyFriendAsync(int userId, int friendId, bool propriety);
        public Task<bool> DeleteFriendRequestAsync(int userId, int friendId);
    }
}
