using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;

namespace Server.src.Services
{
    public class FriendService : IFriendService
    {
        private readonly IFriendRepository _repo;

        public FriendService(IFriendRepository repo)
        {
            _repo = repo;
        }

        public async Task<List<FriendResposeDto>> GetFriendsAsync(int userId, int number)
        {
            var friends = await _repo.GetFriendsByUserIdAsync(userId, number, false);
            if (friends == null || friends.Count == 0)
            {
                return new List<FriendResposeDto>(); // からのリストを送る
            }
            return friends.Select(f =>
                {
                    // フレンドのユーザーだけを取り出す
                    var friendUser = f.User1Id == userId ? f.User2 : f.User1;
                    return new FriendResposeDto(friendUser); // Dto
                }
            ).ToList();
        }

        public async Task<List<SessionResposeDto>> GetFriendSessionsAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        public async Task<Fornow> GetFriendFornowAsync(int userId, int friendId, int id)
        {
            throw new NotImplementedException();
        }

        public async Task<List<Fornow>> GetFriendFornowsAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        public async Task<bool> ProprietyFriendAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        public async Task<bool> RequestFriendAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        public async Task<bool> SetFavoriteToFornowAsync(int userId, int friendId, int id)
        {
            throw new NotImplementedException();
        }
    }
}
