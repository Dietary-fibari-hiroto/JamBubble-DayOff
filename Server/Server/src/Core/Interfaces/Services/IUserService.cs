using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IUserService
    {
        public Task<IEnumerable<User>> GetAllUserAsync();
    }
}
