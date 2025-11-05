using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Extensions;

namespace Server.src.Api.Controllers
{
    [ApiController]
    [Route("/api/[controller]")]
    public class UserController : ControllerBase
    {
        private readonly IUserService _userService;
        public UserController(IUserService userService)
        {
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
            var userId = User.GetUserId(); // JWTからIDを取得

            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var user = await _userService.GetUserAllDataAsync(userId.Value);
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
        [ProducesResponseType(typeof(UserResponseDto), StatusCodes.Status201Created)]
        public async Task<IActionResult> Register([FromBody] RegisterUserRequestDto user)
        {
            var addedUser = await _userService.AddUserAsync(user);

            return CreatedAtAction(nameof(Register), addedUser);
        }

        /// <summary>
        /// ユーザー更新
        /// </summary>
        [Authorize]
        [HttpPut]
        [ProducesResponseType(typeof(UserAllDataResponseDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> Update([FromBody] UpdateUserAllDataRequestDto user)
        {
            var userId = User.GetUserId(); // JWTからIDを取得

            if (userId == null)
            {
                return Unauthorized("Invalid user ID in token.");
            }

            var updatedUser = await _userService.UpdateUserAsync(user, userId.Value);
            if (updatedUser == null)
            {
                return NotFound("User not found.");
            }

            return Ok(updatedUser);
        }

        /// <summary>
        /// ユーザー削除
        /// </summary>
        [Authorize]
        [HttpDelete]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        public async Task<IActionResult> Delete()
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID in token.");
            }

            var result = await _userService.DeleteUserAsync(userId.Value);
            if (!result)
            {
                return NotFound();
            }

            return NoContent();
        }
    }
}
