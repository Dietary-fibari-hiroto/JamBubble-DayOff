using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFriendRequestRepository
    {
        Task<FriendRequest?> GetFriendRequestByIdsAsync(int sendUerId, int passUserId, bool asTracking = true);
        Task<FriendRequest> AddFriendRequestAsync(FriendRequest friendRequest);
        Task UpdateAsync(FriendRequest friendRequest);
        Task DeleteAsync(FriendRequest friendRequest);
    }
}
