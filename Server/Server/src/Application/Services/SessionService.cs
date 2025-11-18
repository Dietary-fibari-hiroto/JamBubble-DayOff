using Microsoft.EntityFrameworkCore;
using Server.Data;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Repositories;
using System.Diagnostics;

namespace Server.src.Services
{
    public class SessionService : ISessionService
    {
        private readonly ISessionRepository _sessionRepo;
        private readonly IFriendRepository _friendRepo;

        public SessionService(ISessionRepository sessionRepo, IFriendRepository friendRepo)
        {
            _sessionRepo = sessionRepo;
            _friendRepo= friendRepo;
        }
        
        // １ユーザーのセッション一覧を取得
        public async Task<List<SessionResponseDto>> GetSessionsAsync(int userId)
        {
            var sessions = await _sessionRepo.GetSessionsByUserIdAsync(userId);
            if (sessions == null || sessions.Count == 0)
            {
                return new List<SessionResponseDto>(); // 空のリスト
            }
            return sessions.Select(s => new SessionResponseDto(s)).ToList();
        }

        // １ユーザーの１セッションの詳細を返す
        public async Task<SessionDetailResponseDto?> GetSessionAsync(int sessionId)
        {
            var session = await _sessionRepo.GetSessionDetailByIdAsync(sessionId, false);
            if (session == null)
            {
                return null;
            }
            return new SessionDetailResponseDto(session);
        }   

        // 人気なアクティブ公開セッションを取得
        public async Task<List<SessionPopularResponseDto>> GetSessionPopularAsync(int n, int skip)
        {
            var sessions = await _sessionRepo.GetSessionPopularAsync(n, skip);
            if (sessions == null || sessions.Count == 0)
            {
                return new List<SessionPopularResponseDto>();
            }
            return sessions;
        }

        public async Task<List<SessionResponseDto>> GetSessionActPubFrinedAsync(int userId, int n, int skip)
        {
            var friends = await _friendRepo.GetFriendsByUserIdAsync(userId, 100000, false);
            if (friends == null || friends.Count == 0)
            {
                return new List<SessionResponseDto>();
            }
            var sessions = friends.Select(f =>
            {
                // フレンドのユーザーだけを取り出す
                var friendUser = f.User1Id == userId ? f.User2 : f.User1;
                var session = 
            }).ToList();
        }
    }
}
