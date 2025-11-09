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

        Task<Fornow> IFriendService.GetFriendFornowAsync(int userId, int friendId, int id)
        {
            throw new NotImplementedException();
        }

        Task<List<Fornow>> IFriendService.GetFriendFornowsAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        Task<List<Friend>> IFriendService.GetFriendsAsync(int userId)
        {
            throw new NotImplementedException();
        }

        Task<List<Session>> IFriendService.GetFriendSessionsAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        Task<bool> IFriendService.ProprietyFriendAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        Task<bool> IFriendService.RequestFriendAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }

        Task<bool> IFriendService.SetFavoriteToFornowAsync(int userId, int friendId, int id)
        {
            throw new NotImplementedException();
        }
    }
}
