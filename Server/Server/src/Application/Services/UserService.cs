using Server.src.Repositories;
using Server.src.Entities;
using Server.src.Interfaces;
namespace Server.src.Services
{
    public class UserService:IUserService
    {
        private readonly IUserRepository _repo;
        public UserService(IUserRepository repo)
        {
            _repo = repo;
        }

        public async Task<IEnumerable<User>> GetAllUserAsync() => await _repo.GetAllAsync();
    }
}
