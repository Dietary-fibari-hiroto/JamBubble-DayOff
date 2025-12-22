using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IUserService
    {
        public Task<UserAllDataResponseDto?> GetUserAllDataAsync(int userId);
        public Task<UserProfileResponseDto?> GetUserProfileAsync(int userId, int targetId);
        public Task<User?> UserExistsAsync(string email);
        public Task<UserAllDataResponseDto?> AddUserAsync(RegisterUserRequestDto user);
        public Task<UserAllDataResponseDto?> UpdateUserAsync(UpdateUserAllDataRequestDto updateData, int userId);
        public Task<bool> DeleteUserAsync(int userId);
        public Task<List<UserProviderResponseDto>?> GetUserProvidersAsync(int userId);
        public Task<bool> AddUserProviderAsync(RegisterUserProviderRequestDto userProviderDto, int userId);
        public Task<bool> DeleteUserProviderAsync(DeleteUserProviderRequestDto providerDto, int userId);
    }
}
