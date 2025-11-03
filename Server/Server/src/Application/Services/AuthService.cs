using DotNetEnv;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Repositories;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace Server.src.Services
{
    public class AuthService : IAuthService
    {
        private readonly IUserRepository _userRepository;
        private readonly IPasswordHasher<User> _passwordHasher;
        private readonly string _jwtKey; // JWT署名キー 
        private readonly string _jwtIssuer; // JWT発行者
        private readonly string _jwtAudience; // JWT対象者

        private readonly ILogger<AuthService> _logger;

        public AuthService(IUserRepository userRepository, IPasswordHasher<User> passwordHasher, ILogger<AuthService> logger)
        {
            _userRepository = userRepository;
            _passwordHasher = passwordHasher;
            _jwtKey = Environment.GetEnvironmentVariable("JWT__KEY")!;
            _jwtIssuer = Environment.GetEnvironmentVariable("JWT__ISSUER")!;
            _jwtAudience = Environment.GetEnvironmentVariable("JWT__AUDIENCE")!;

            _logger = logger;
        }

        public async Task<string?> LoginAsync(string email, string password)
        {
            _logger.LogDebug("AuthService.LoginAsync");
            var user = await _userRepository.GetByEmailAsync(email); // Emailでユーザーを取得

            // 検証
            if (!(user != null && VerifyPassword(user, password)))
                return null;

            // JWTトークンの生成
            var claims = new[]
            {
                new Claim(JwtRegisteredClaimNames.Sub, user!.Id.ToString()), // ユーザーIDをサブジェクトクレームに設定
                new Claim(JwtRegisteredClaimNames.Email, user.Email) // Emailクレームに設定
            };

            // 署名キーを.envから取得
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_jwtKey));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256); // 署名アルゴリズム

            // トークンの作成
            var token = new JwtSecurityToken(
                issuer: _jwtIssuer, // 発行者
                audience: _jwtAudience, // 対象者
                claims: claims, // クレーム
                expires: DateTime.UtcNow.AddHours(1), // 有効期限
                signingCredentials: creds // 署名情報
                );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        private bool VerifyPassword(User user, string password)
        {
            // パスワード ハッシュ比較
            var result = _passwordHasher.VerifyHashedPassword(user, user.Password, password);

            _logger.LogDebug("パスワード比較結果 : {result}", result);

            if (result == PasswordVerificationResult.Success) return true;
            else return false;
        }
    }
}
