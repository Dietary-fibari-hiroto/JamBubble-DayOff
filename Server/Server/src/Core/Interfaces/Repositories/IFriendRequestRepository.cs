using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IFriendRequestRepository
    {
        Task<FriendRequest?> GetFriendRequestByIdsAsync(int id1, int id2, bool asTracking = true);
        Task<List<FriendRequest>> GetFriendRequestsBySenderUserIdAsync(int senderUserId, bool asTracking = true);
        Task<List<FriendRequest>> GetFriendRequestsByReceiverUserIdAsync(int receiverUserId, bool asTracking = true);
        Task<FriendRequest> AddFriendRequestAsync(FriendRequest friendRequest);
        Task UpdateAsync(FriendRequest friendRequest);
        Task DeleteAsync(FriendRequest friendRequest);
        Task<bool> IsFriendRequestExistAsync(int sendUserId, int targetUserId);
    }
}
