using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Server.src.Interfaces;

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
        
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            // ModelStateでのリクエストのバリデーションチェック
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            // 認証処理
            var token = await _authService.LoginAsync(request.Email, request.Password);
            if (token == null)
                return Unauthorized(); // 認証失敗

            return Ok(new { token });
        }

    }
}