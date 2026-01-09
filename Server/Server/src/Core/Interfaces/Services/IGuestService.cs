using Server.src.Entities;
using Server.src.DTOs;

namespace Server.src.Interfaces
{
    public interface IGuestService
    {
        public Task<bool> AddGuestAsync(RegisterGuestRequestDto guestDto);
    }
}