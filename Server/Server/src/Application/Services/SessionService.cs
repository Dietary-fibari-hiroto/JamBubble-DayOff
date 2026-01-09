using Microsoft.EntityFrameworkCore;
using Server.Data;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Repositories;
using System.Diagnostics;
using System.Diagnostics.CodeAnalysis;

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

        // フレンドのアクティブ公開セッションを取得
        public async Task<List<SessionResponseDto>> GetSessionActPubFrinedAsync(int userId, int n, int skip)
        {
            var friends = await _friendRepo.GetFriendsByUserIdAsync(userId, n, false, skip);
            if (friends == null || friends.Count == 0)
            {
                return new List<SessionResponseDto>();
            }

            var sessions = new List<SessionResponseDto>();
            foreach(var f in friends)
            {
                // フレンドのユーザーだけを取り出す
                var friendUser = f.User1Id == userId ? f.User2 : f.User1;
                if (friendUser != null)
                {
                    var session = await _sessionRepo.GetActPubSessionsByUserIdAsync(friendUser!.Id);
                    if (session != null)
                    {
                        sessions.Add(new SessionResponseDto(session!));
                    }
                }
            }
            return sessions;
        }

        // 保存可能なセッション一覧を取得
        public async Task<List<SessionResponseDto>> GetSavePossibleSessionsAsync(int userId)
        {
            var sessions = await _sessionRepo.GetSavePossibleSessionsAsync(userId);
            if(sessions == null || sessions.Count == 0)
            {
                return new List<SessionResponseDto>();
            }
            return sessions.Select(s => new SessionResponseDto(s)).ToList();
        }

        // セッションを追加
        public async Task<SessionDetailResponseDto?> AddSessionAsync(SessionRequestDto sessionReqDto, int userID)
        {
            // DTOからEntityに変換
            var session = sessionReqDto.ToSessionEntity(userID);
            if (session == null)
            {
                return null;
            }
            // セッションを追加
            var addedSession =  await _sessionRepo.AddSessionAsync(session);
            // セッションタグを追加
            var sessionTagList = sessionReqDto.ToSessionTagsEntities(addedSession.Id);
            addedSession.SessionTag = sessionTagList;
            await _sessionRepo.UpdateSessionAsync(addedSession);

            return new SessionDetailResponseDto(addedSession);
        }

        // セッションの終了判定を更新
        public async Task<bool> SetSessionEndedAsync(int sessionId)
        {
            var session = await _sessionRepo.GetSessionByIdAsync(sessionId);
            if(session == null)
            {
                return false;
            }
            session.Finished = true;
            session.FinishedAt = DateTime.UtcNow;
            await _sessionRepo.UpdateSessionAsync(session);
            return true;
        }
    }
}
