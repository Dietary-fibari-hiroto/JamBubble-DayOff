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

        public FriendService(IFriendRepository friendrRepo, ISessionRepository sessionRepo, IFornowRepository fornowRepo, IFornowLikeRepository fornowLikeRepo)
        {
            _friendRepo = friendrRepo;
            _sessionRepo = sessionRepo;
            _fornowRepo = fornowRepo;
            _fornowLikeRepo = fornowLikeRepo;
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
                    return new FriendResposeDto(friendUser); // Dto
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

        public async Task<bool> ProprietyFriendAsync(int userId, int fornowId)
        {
            throw new NotImplementedException();
        }

        public async Task<bool> RequestFriendAsync(int userId, int friendId)
        {
            throw new NotImplementedException();
        }
    }
}
