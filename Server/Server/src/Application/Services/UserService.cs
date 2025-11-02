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

        public async Task<User?> GetUserAsync(int userId)
        {
            // IDで取得
            var user = await _repo.GetByIdAsync(userId);
            if (user == null) return null;

            // パスワードを空に
            user.Password = "";
            return user;
        }
        public async Task<User?> AddUserAsync(User user)
        {
            // すでに同じEmailのユーザーが存在するか確認
            var existingUser = await _repo.GetByEmailAsync(user.Email);
            if (existingUser != null) return null; 

            // パスワードのハッシュ化
            user.Password = _passwordHasher.HashPassword(user, user.Password);

            var addedUser = await _repo.AddAsync(user);
            if (addedUser == null) return null;

            // TODO:レスポンス内容をどうするか
            addedUser.Password = "";
            return addedUser;
        }

        public async Task<User?> UpdateUserAsync(User updateData, int userId)
        {
            // IDで取得
            var updateUser = await _repo.GetByIdAsync(userId);
            if (updateUser == null) return null;



            return updateUser;
        }

        public async Task<bool> DeleteUserAsync(int userId)
        {
            return false;
        }
    }
}
