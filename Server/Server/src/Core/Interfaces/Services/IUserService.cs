using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IUserService
    {
        public Task<UserAllDataResponseDto?> GetUserAllDataAsync(int userId);
        public Task<User?> AddUserAsync(User user);
        public Task<User?> UpdateUserAsync(User updateData, int userId);
        public Task<bool> DeleteUserAsync(int userId);
    }
}
