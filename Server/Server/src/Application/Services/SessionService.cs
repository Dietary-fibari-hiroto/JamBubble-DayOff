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
        private readonly ISessionRepository _sessionRepository;

        public SessionService(ISessionRepository sessionRepository)
        {
            _sessionRepository = sessionRepository;
        }
        
        public async Task<List<SessionResponseDto>> GetSessionsAsync(int userId)
        {
            var sessions = await _sessionRepository.GetSessionsByUserIdAsync(userId);
            if (sessions == null || sessions.Count == 0)
            {
                return new List<SessionResponseDto>(); // 空のリスト
            }
            return sessions.Select(s => new SessionResponseDto(s)).ToList();
        }

        public async Task<SessionDetailResponseDto?> GetSessionAsync(int sessionId)
        {
            var session = await _sessionRepository.GetSessionByIdAsync(sessionId, false);
            if (session == null)
            {
                return null;
            }
            return new SessionDetailResponseDto(session);
        }   

    }
}
