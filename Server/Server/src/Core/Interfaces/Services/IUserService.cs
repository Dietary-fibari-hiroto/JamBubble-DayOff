using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IUserService
    {
        public Task<UserAllDataResponseDto?> GetUserAllDataAsync(int userId);
        public Task<UserResponseDto?> AddUserAsync(RegisterUserRequestDto user);
        public Task<UserAllDataResponseDto?> UpdateUserAsync(UpdateUserAllDataRequestDto updateData, int userId);
        public Task<bool> DeleteUserAsync(int userId);
    }
}
