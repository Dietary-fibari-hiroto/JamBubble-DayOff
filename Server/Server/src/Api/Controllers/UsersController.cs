using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;

namespace Server.src.Api.Controllers
{

    [ApiController]
    [Route("/api/[controller]")]
    public class TestController : ControllerBase
    {
        [HttpGet]
        public async Task<IActionResult> GetTest() =>Ok("DayOff");
    }

    [ApiController]
    [Route("/api/[controller]")]
    public class UserController:ControllerBase
    {
        private readonly IUserService _userService;
        public UserController(IUserService userService) {
            _userService = userService;
        }

        /// <summary>
        /// ユーザーのすべての情報を返す
        /// </summary>
        [Authorize]
        [HttpGet]
        [ProducesResponseType(typeof(UserAllDataResponseDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetUser()
        {
            // JWTのクレームからユーザーID取得
            var userIdString = User.Claims.FirstOrDefault(c => c.Type == System.Security.Claims.ClaimTypes.NameIdentifier)?.Value;
            if (String.IsNullOrEmpty(userIdString))
            {
                return Unauthorized("ID claim not found in token.");
            }

            // intに変換
            if(!int.TryParse(userIdString, out var userId))
            {
                return BadRequest("Invalid user ID format in token.");
            }
            
            var user = await _userService.GetUserAllDataAsync(userId);
            if (user == null)
            {
                return NotFound("User not found.");
            }

            return Ok(user);
        }

        /// <summary>
        /// ユーザー作成
        /// </summary>
        [AllowAnonymous]
        [HttpPost]
        [ProducesResponseType(typeof(UserResponseDto), StatusCodes.Status201Created)] // 成功時のレスポンス型 
        public async Task<IActionResult> Register([FromBody] User user)
        {
            var addedUser = await _userService.AddUserAsync(user);
            if (addedUser == null)
            {
                return Conflict("ユーザーの登録に失敗しました。");
            }

            //return Ok(new { addedUser });
            return CreatedAtAction(nameof(Register), new { id = addedUser.Id }, addedUser);
        }


    }
}
