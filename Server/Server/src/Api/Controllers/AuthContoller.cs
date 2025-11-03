using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Server.src.Interfaces;
using Server.src.DTOs;

namespace Server.src.Api.Controllers
{
    [ApiController]
    [Route("/api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly IAuthService _authService; // 依存性の注入
        
        public AuthController(IAuthService authService)
        {
            _authService = authService; // コンストラクタ
        }

        /// <summary>
        /// ログイン
        /// </summary>
        [AllowAnonymous]
        [HttpPost("login")]
        [ProducesResponseType(typeof(TokenResponseDto), StatusCodes.Status200OK)] // 成功時のレスポンス型 
        public async Task<IActionResult> Login([FromBody] LoginRequestDto request)
        {
            // 認証処理
            var token = await _authService.LoginAsync(request.Email, request.Password);
            if (token == null)
                return Unauthorized(); // 認証失敗

            return Ok(new { token });
        }

    }
}