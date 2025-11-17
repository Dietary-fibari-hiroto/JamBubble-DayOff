using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;

namespace Server.src.Services
{
    public class FriendService : IFriendService
    {
        private readonly IFriendRepository _friendRepo;
        private readonly ISessionRepository _sessionRepo;
        private readonly IFornowRepository _fornowRepo;
        private readonly IFornowLikeRepository _fornowLikeRepo;
        private readonly IFriendRequestRepository _friendRequestRepo;
        private readonly IUserRepository _userRepo;

        public FriendService(IFriendRepository friendrRepo, ISessionRepository sessionRepo, IFornowRepository fornowRepo, IFornowLikeRepository fornowLikeRepo, IFriendRequestRepository friendRequestReo, IUserRepository userRepo)
        {
            _friendRepo = friendrRepo;
            _sessionRepo = sessionRepo;
            _fornowRepo = fornowRepo;
            _fornowLikeRepo = fornowLikeRepo;
            _friendRequestRepo = friendRequestReo;
            _userRepo = userRepo;
        }

        // フレンド一覧を取得
        public async Task<List<FriendResposeDto>> GetFriendsAsync(int userId, int number)
        {
            var friends = await _friendRepo.GetFriendsByUserIdAsync(userId, number, false);
            if (friends == null || friends.Count == 0)
            {
                return new List<FriendResposeDto>(); // からのリストを送る
            }
            return friends.Select(f =>
                {
                    // フレンドのユーザーだけを取り出す
                    var friendUser = f.User1Id == userId ? f.User2 : f.User1;
                    if (friendUser == null)
                    {
                        // TODO : ここの処理どうするか考える
                        throw new Exception("Friend user not found");
                    }
                    return new FriendResposeDto(friendUser);
                }
            ).ToList();
        }

        // 1フレンドのセッション一覧を取得
        public async Task<List<SessionResponseDto>> GetFriendSessionsAsync(int friendId)
        {
            var sessions = await _sessionRepo.GetSessionsByUserIdAsync(friendId);
            return sessions.Select(s => new SessionResponseDto(s)).ToList();
        }

        // フレンド全員のFornowを取得
        public async Task<List<FornowSimpResponseDto>> GetFriendsFornowsAsync(int userId, int n)
        {
            // フレンド一覧を取得
            var friends = await _friendRepo.GetFriendsByUserIdAsync(userId, n, false);
            if (friends == null || friends.Count == 0)
            {
                return new List<FornowSimpResponseDto>(); // 空のリスト
            }

            var fornows = new List<FornowSimpResponseDto>();
            foreach (var friend in friends)
            {
                // フレンドだけを取り出す
                var friendUserId = friend.User1Id == userId ? friend.User2Id : friend.User1Id;
                var friendFornow = await _fornowRepo.GetFornowByUserIdAsync(friendUserId, false);
                if (friendFornow == null)
                {
                    continue;
                }
                fornows.Add(new FornowSimpResponseDto(friendFornow));
            }

            return fornows;
        }

        // 1フレンドのFornowを取得
        public async Task<FornowDetailResponseDto?> GetFriendFornowDetailAsync(int userId, int friendId)
        {
            // フレンド関係を確認
            var friend = await _friendRepo.GetFriendByUserIdAsync(userId, friendId, false);
            if (friend == null)
            {
                return null;
            }

            // Fornowを取得
            var fornow = await _fornowRepo.GetFornowByUserIdAsync(friendId, false);
            if (fornow == null)
            {
                return null;
            }

            // いいね数と自分がいいねしているか確認
            var likeCount = await _fornowLikeRepo.CountLikesByFornowIdAsync(fornow.Id);
            var isLiked = false;
            if (await _fornowLikeRepo.GetFornowLikeByIdsAsync(fornow.Id, userId, false) != null)
                {
                isLiked = true;
            }

            return new FornowDetailResponseDto(fornow, likeCount, isLiked);
        }

        // フレンドのFornowにいいねを付ける
        public async Task<bool> SetLikeToFornowAsync(int userId, int friendId)
        {
            // フレンド関係を確認
            var friend = await _friendRepo.GetFriendByUserIdAsync(userId, friendId, false);
            if (friend == null)
            {
                return false;
            }

            // Fornowを取得
            var fornow = await _fornowRepo.GetFornowByUserIdAsync(friendId, false);
            if (fornow == null)
            {
                return false;
            }

            // いいねを追加
            var existingLike = await _fornowLikeRepo.GetFornowLikeByIdsAsync(fornow.Id, userId, false);
            if (existingLike != null)
            {
                return false;
            }
            await _fornowLikeRepo.AddFornowLikeAsync(new FornowLike
            {
                FornowId = fornow.Id,
                UserId = userId
            });
            return true;
        }

        // 送信・受信したフレンドリクエスト一覧を取得
        public async Task<FriendRequestSndRcvDto> GetfriendRequestSndRcv(int userId)
        {
            var sentRequests = await _friendRequestRepo.GetFriendRequestsBySenderUserIdAsync(userId);
            var receivedRequests = await _friendRequestRepo.GetFriendRequestsByReceiverUserIdAsync(userId);
            return new FriendRequestSndRcvDto(sentRequests, receivedRequests);
        }

        // フレンドリクエストを送信
        public async Task<bool> RequestFriendAsync(int userId, int targetUserId)
        {
            if (userId == targetUserId)
            {
                return false;
            }
            // ユーザーが存在するか確認
            var existingUser = await _userRepo.GetUserByIdAsync(targetUserId, false);
            if (existingUser == null)
            {
                return false;
            }

            // すでにフレンドか確認
            var existingFriend = await _friendRepo.GetFriendByUserIdAsync(userId, targetUserId, false);
            if (existingFriend != null)
            {
                return false;
            }

            // すでにリクエストがあるか確認
            var existingRequest = await _friendRequestRepo.GetFriendRequestByIdsAsync(userId, targetUserId, false);
            if (existingRequest != null)
            {
                return false;
            }

            // フレンドリクエストを追加
            await _friendRequestRepo.AddFriendRequestAsync(new FriendRequest
            {
                SendUserId = userId,
                PassUserId = targetUserId
            });
            return true;
        }


        // フレンドリクエストの可否を決定
        public async Task<bool> ProprietyFriendAsync(int userId, int requestUserId, bool propriety)
        {
            // フレンドリクエストが存在するか確認
            var friendRequest = await _friendRequestRepo.GetFriendRequestByIdsAsync(requestUserId, userId, true);
            if (friendRequest == null)
            {
                return false;
            }
            
            if (propriety)
            {
                // フレンドリクエストを承認に変更
                friendRequest.State = 1;
                await _friendRequestRepo.UpdateAsync(friendRequest);
                // フレンド関係を追加
                await _friendRepo.AddFriendAsync(new Friend
                {
                    User1Id = userId < requestUserId ? userId : requestUserId,
                    User2Id = userId > requestUserId ? userId : requestUserId
                });
            }
            else
            {
                // フレンドリクエストを拒否に変更
                friendRequest.State = 2;
                await _friendRequestRepo.UpdateAsync(friendRequest);
            }
            return true;
        }

        // フレンドリクエストを削除
        public async Task<bool> DeleteFriendRequestAsync(int userId, int targetuserId)
        {
            var friendRequest = await _friendRequestRepo.GetFriendRequestByIdsAsync(userId, targetuserId, true);
            if (friendRequest == null)
            {
                // 逆方向も確認
                friendRequest =  await _friendRequestRepo.GetFriendRequestByIdsAsync(targetuserId, userId, true);
                if (friendRequest == null)
                {
                    return false;
                }
            }
            await _friendRequestRepo.DeleteAsync(friendRequest);
            return true;
        }
    }
}
