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
            var user = await _repo.GetUserByIdAsync(userId);
            if (user == null)
            {
                return null;
            }

            return new UserAllDataResponseDto(user);
        }
        public async Task<UserProfileResponseDto?> GetUserProfileAsync(int userId)
        {
            // IDで取得
            var user = await _repo.GetUserByIdAsync(userId, false);
            if (user == null)
            {
                return null;
            }

            return new UserProfileResponseDto(user);
        }

        public Task<User?> UserExistsAsync(string email)
        {
            return _repo.GetUserByEmailAsync(email);
        }

        public async Task<UserAllDataResponseDto?> AddUserAsync(RegisterUserRequestDto userDto)
        {
            // すでに同じEmailのユーザーが存在するか確認
            var existingUser = await _repo.GetUserByEmailAsync(userDto.Email);
            if (existingUser != null)
            {
                throw new InvalidOperationException("EmailConflict");
            }

            var user = userDto.RequestToUser(new User());

            user.UserHistory = new UserHistory
            {
                User = user,
                SessionCount = 0
            };

            user.FavoriteMusic = new FavoriteMusic
            {
                User = user,
                MusicId = null!
            };

            // パスワードのハッシュ化
            user.Password = _passwordHasher.HashPassword(user, user.Password);

            var addedUser = await _repo.AddUserAsync(user);

            return new UserAllDataResponseDto(addedUser);
        }

        public async Task<UserAllDataResponseDto?> UpdateUserAsync(UpdateUserAllDataRequestDto updateDataDto, int userId)
        {
            // IDで取得
            var updateUser = await _repo.GetUserByIdAsync(userId);
            if (updateUser == null) return null;

            // Emailが重複していないかをチェック
            var newEmail = updateDataDto.Email;
            if(newEmail != null && newEmail != updateUser.Email)
            {
                var existingUser = await _repo.GetUserByEmailAsync(newEmail);
                if (existingUser != null && existingUser.Id != userId)
                {
                    throw new InvalidOperationException("EmailConflict");
                }
            }
            string? newPassword = updateDataDto.Password; // 新しい平文のパスワード一時保存
            updateUser = updateDataDto.RequestToUser(updateUser);
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
            var deleteUser = await _repo.GetUserByIdAsync(userId);
            if (deleteUser == null) return false;

            // TODO :画像データの削除処理は必要か？あとここに書くべきか？
            // 画像を削除
            if (!string.IsNullOrEmpty(deleteUser.ImgUrl) && deleteUser.ImgUrl != "/default/default_user_image.png")
            {
                var existingFilePath = Path.Combine("wwwroot", deleteUser.ImgUrl.TrimStart('/'));
                if (File.Exists(existingFilePath))
                {
                    File.Delete(existingFilePath);
                }
            }

            await _repo.DeleteUserAsync(deleteUser);

            return true;
        }

        public async Task<List<UserProviderResponseDto>?> GetUserProvidersAsync(int userId)
        {
            var userProviders = await _repo.GetUserProvidersByUserIdAsync(userId);
            if (userProviders == null)
            {
                return null;
            }

            return userProviders.Select(up => new UserProviderResponseDto(up)).ToList();
        }

        public async Task<bool> AddUserProviderAsync(RegisterUserProviderRequestDto userProviderDto, int userId)
        {
            // ユーザー情報を引っ張ってきてその中にくっついているプロバイダー情報を書き換えて保存し登録する
            // ユーザーが存在するか確認
            var userEntity = await _repo.GetUserByIdAsync(userId);
            if (userEntity == null)
            {
                return false;
            }

            // すでに存在するか確認
            if (userEntity.UserProviders != null &&
                userEntity.UserProviders.Any(up => up.ProviderId == userProviderDto.ProviderId))
            {
                throw new InvalidOperationException("UserProviderConflict"); // 例外スロー
            }

            userEntity = userProviderDto.RequestToUserProvider(userEntity);
            // TODO:ハッシュ化したパスは複号不可だから保存しても意味ないのでは？
            //userEntity.Password = _passwordHasher.HashPassword(userEntity, userEntity.Password);

            await _repo.UpdateAsync(userEntity);

            return true;
        }

        public async Task<bool> DeleteUserProviderAsync(DeleteUserProviderRequestDto providerDto, int userId)
        {
            // ユーザーとプロバイダーリストが存在するか確認
            var userEntity = await _repo.GetUserByIdAsync(userId);
            if (userEntity == null || userEntity.UserProviders == null)
            {
                return false;
            }

            // プロバイダー情報の検索
            var targetProvider = userEntity.UserProviders.FirstOrDefault(up => up.ProviderId == providerDto.ProviderId);
            if (targetProvider != null)
            {
                await _repo.DeleteUserProviderAsync(targetProvider);
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
