using Server.src.Repositories;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.DTOs;
using Microsoft.AspNetCore.Identity;

namespace Server.src.Services
{
    public class UserService:IUserService
    {
        private readonly IUserRepository _repo;
        private readonly IPasswordHasher<User> _passwordHasher;

        private readonly ILogger<UserService> _logger;
        public UserService(IUserRepository repo, IPasswordHasher<User> passwordHasher, ILogger<UserService> logger)
        {
            _repo = repo;
            _passwordHasher = passwordHasher;

            _logger = logger;
        }

        public async Task<UserAllDataResponseDto?> GetUserAllDataAsync(int userId)
        {
            // IDで取得
            var user = await _repo.GetByIdAsync(userId);
            if (user == null)
            {
                return null;
            }

            var userResponseDto = new UserAllDataResponseDto(user);

            return userResponseDto;
        }
        public async Task<UserResponseDto?> AddUserAsync(RegisterUserRequestDto userDto)
        {
            // すでに同じEmailのユーザーが存在するか確認
            var existingUser = await _repo.GetByEmailAsync(userDto.Email);
            if (existingUser != null)
            {
                throw new InvalidOperationException("EmailConflict");
            }

            var user = userDto.RequestToUser(new User());

            // パスワードのハッシュ化
            user.Password = _passwordHasher.HashPassword(user, user.Password);

            var addedUser = await _repo.AddAsync(user);

            return new UserResponseDto(addedUser);
        }

        public async Task<UserAllDataResponseDto?> UpdateUserAsync(UpdateUserAllDataRequestDto updateDataDto, int userId)
        {
            // IDで取得
            var updateUser = await _repo.GetByIdAsync(userId);
            if (updateUser == null) return null;

            // Emailが重複していないかをチェック
            var newEmail = updateDataDto.userDto?.Email;
            if(newEmail != null && newEmail != updateUser.Email)
            {
                var existingUser = await _repo.GetByEmailAsync(newEmail);
                if (existingUser != null && existingUser.Id != userId)
                {
                    throw new InvalidOperationException("EmailConflict");
                }
            }
            string? newPassword = updateDataDto.userDto?.Password; // 新しい平文のパスワード一時保存
            updateUser = updateDataDto.RequestDtoToEntitie(updateUser);
            if (!string.IsNullOrEmpty(newPassword))
            {
                // パスワードのハッシュ化
                updateUser.Password = _passwordHasher.HashPassword(updateUser, newPassword);
            }
            
            await _repo.UpdateAsync(updateUser); // 更新処理

            return new UserAllDataResponseDto(updateUser);
        }

        public async Task<bool> DeleteUserAsync(int userId)
        {
            // IDで取得
            var deleteUser = await _repo.GetByIdAsync(userId);
            if (deleteUser == null) return false;

            await _repo.DeleteAsync(deleteUser);

            return true;
        }
    }
}
