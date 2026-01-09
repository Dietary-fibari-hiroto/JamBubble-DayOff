using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;

namespace Server.src.Services
{
    public class GuestService : IGuestService
    {
        private readonly IGuestRepository _guestRepo;
        private readonly IUserRepository _userRepo;
        private readonly ISessionRepository _sessionRepo;
        public GuestService(IGuestRepository guestRepo, IUserRepository userRepo, ISessionRepository sessionRepo)
        {
            _guestRepo = guestRepo;
            _userRepo = userRepo;
            _sessionRepo = sessionRepo;
        }

        // ゲストを登録
        public async Task<bool> AddGuestAsync(RegisterGuestRequestDto guestDto)
        {
            // セッションが存在するか確認
            var existingSession = await _sessionRepo.GetSessionByIdAsync(guestDto.SessionId, false);
            if (existingSession == null)
            {
                return false;
            }

            // ユーザーが存在するか確認
            if(guestDto.UserId != null)
            {
                var existingUser = await _userRepo.GetUserByIdAsync((int)guestDto.UserId, false);
                if (existingUser == null)
                {
                    return false;
                }
            }
            // Dtoからエンティティに変換
            Guest guest = guestDto.RequestToGuest();
            await _guestRepo.AddGuestAsync(guest);
            return true;
        }
    }
}