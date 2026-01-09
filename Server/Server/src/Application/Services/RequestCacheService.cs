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
    public class RequestCacheService : IRequestCacheService
    {
        private readonly IRequestCacheRepository _rcRepo;
        private readonly IGuestRepository _guestRepo;
        private readonly ISessionRepository _sessionRepo;

        public RequestCacheService(IRequestCacheRepository rcRepo, IGuestRepository guestRepo, ISessionRepository sessionRepo)
        {
            _rcRepo = rcRepo;
            _guestRepo = guestRepo;
            _sessionRepo = sessionRepo;
        }

        // リクエストキャッシュテーブルに追加
        public async Task<RequestCache?> AddRequestCacheAsync(RegisterRequestCacheRequestDto rcDto)
        {
            // ゲストIDが存在するか確認
            var existingGuest = await _guestRepo.GetGuestByIdAsync(rcDto.GuestId, false);
            if(existingGuest == null)
            {
                return null;
            }

            // セッションIDが存在するか確認
            var existingSession = await _sessionRepo.GetSessionByIdAsync(rcDto.SessionId, false);
            if(existingSession == null)
            {
                return null;
            }

            RequestCache rc = rcDto.RequestToRequetCache();
            await _rcRepo.AddRequestCacheAsync(rc);
            return rc;
        }

    }
}