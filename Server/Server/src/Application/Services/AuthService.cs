using Server.src.Repositories;
using Server.src.Entities;
using Server.src.Interfaces;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Text;
using DotNetEnv;

namespace Server.src.Services
{
    public class AuthService : IAuthService
    {
        private readonly IUserRepository _userRepository;
        private readonly string _jwtKey; // JWT署名キー 
        private readonly string _jwtIssuer; // JWT発行者
        private readonly string _jwtAudience; // JWT対象者

        public AuthService(IUserRepository userRepository)
        {
            _userRepository = userRepository;
            _jwtKey = Environment.GetEnvironmentVariable("jwt__key")!;
            _jwtIssuer = Environment.GetEnvironmentVariable("jwt__Issuer")!;
            _jwtAudience = Environment.GetEnvironmentVariable("jwt__Audience")!;
        }

        public async Task<string?> LoginAsync(string email, string password)
        {
            var user = await _userRepository.GetByEmailAsync(email); // Emailでユーザーを取得

            // パスワードの検証
            if (user != null && VerifyPassword(user, password))
                return null;

            // JWTトークンの生成
            var claims = new[]
            {
                new Claim(JwtRegisteredClaimNames.Sub, user!.Id.ToString()), // ユーザーIDをサブジェクトクレームに設定
                new Claim(JwtRegisteredClaimNames.UniqueName, user.Name) // ユーザー名をユニーク名クレームに設定
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
            return user.Password == password;
        }
    }
}
