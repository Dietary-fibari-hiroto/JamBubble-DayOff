using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface IGuestRepository
    {
        public Task<Guest> AddGuestAsync(Guest guest);
        public Task<Guest?> GetGuestByIdAsync(int guestId, bool asTracking = true);
    }
}