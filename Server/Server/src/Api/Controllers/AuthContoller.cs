using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Server.src.DTOs;
using Server.src.Interfaces;
using Server.src.Services;

namespace Server.src.Api.Controllers
{
    [ApiController]
    [Route("/api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly IAuthService _authService; // 依存性の注入

        private readonly ILogger<AuthController> _logger;

        public AuthController(IAuthService authService, ILogger<AuthController> logger)
        {
            _authService = authService; // コンストラクタ

            _logger = logger;
        }

        /// <summary>
        /// ログイン
        /// </summary>
        [AllowAnonymous]
        [HttpPost("login")]
        [ProducesResponseType(typeof(TokenResponseDto), StatusCodes.Status200OK)] // 成功時のレスポンス型 
        public async Task<IActionResult> Login([FromBody] LoginRequestDto request)
        {
            _logger.LogDebug("AuthController Get /api/login");

            // 認証処理
            var token = await _authService.LoginAsync(request.Email, request.Password);
            if (token == null)
            {
                _logger.LogDebug("Token is null.");
                return Unauthorized(); // 認証失敗
            }

            _logger.LogDebug("StatuCode : 200 {token}", token);
            return Ok(new { token });
        }

    }
}