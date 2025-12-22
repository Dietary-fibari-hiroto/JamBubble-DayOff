using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Configrations;

namespace Server.src.Api.Controllers
{
    [ApiController]
    [Route("/api/user")]
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
        /// ユーザーのプロフィール情報を返す
        /// </summary>
        [Authorize]
        [HttpGet("{targetid}")]
        [ProducesResponseType(typeof(UserProfileResponseDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetProfile(int targetid)
        {
            var userId = User.GetUserId(); // JWTからIDを取得

            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var profile = await _userService.GetUserProfileAsync(userId.Value, targetid);
            if (profile == null)
            {
                return NotFound("User not found.");
            }

            return Ok(profile);
        }

        /// <summary>
        /// ユーザー作成
        /// </summary>
        [AllowAnonymous]
        [HttpPost]
        [ProducesResponseType(typeof(UserAllDataResponseDto), StatusCodes.Status201Created)]
        public async Task<IActionResult> RegisterUser([FromForm] RegisterUserRequestDto user)
        {
            var addedUser = await _userService.AddUserAsync(user);

            return CreatedAtAction(nameof(RegisterUser), addedUser);
        }

        /// <summary>
        /// ユーザー更新
        /// </summary>
        [Authorize]
        [HttpPut]
        [ProducesResponseType(typeof(UserAllDataResponseDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> UpdateUser([FromForm] UpdateUserAllDataRequestDto user)
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
        public async Task<IActionResult> DeleteUser()
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

        /// <summary>
        /// ユーザーの加入しているプロバイダーを取得
        /// </summary>
        [Authorize]
        [HttpGet("provider")]
        [ProducesResponseType(typeof(List<UserProviderResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetUserProviders()
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID in token.");
            }

            var userProviders = await _userService.GetUserProvidersAsync(userId.Value);
            if (userProviders == null)
            {
                return NotFound();
            }

            return Ok(userProviders);
        }

        /// <summary>
        /// プロバイダーの紐づけ
        /// </summary>
        [Authorize]
        [HttpPost("provider")]
        [ProducesResponseType(typeof(UserProviderResponseDto), StatusCodes.Status201Created)]
        public async Task<IActionResult> RegisterUserProvider([FromBody] RegisterUserProviderRequestDto RequestDto)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID in token.");
            }

            var result = await _userService.AddUserProviderAsync(RequestDto, userId.Value);
            if (!result)
            {
                return NotFound();
            }

            return CreatedAtAction(nameof(RegisterUserProvider), RequestDto);
        }

        /// <summary>
        /// 紐づけしたプロバイダーを削除
        /// </summary>
        [Authorize]
        [HttpDelete("provider")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        public async Task<IActionResult> DeleteUserProvider([FromBody] DeleteUserProviderRequestDto requestDto)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID in token.");
            }

            var result = await _userService.DeleteUserProviderAsync(requestDto, userId.Value);
            if (!result)
            {
                return NotFound();
            }

            return NoContent();
        }
    }
}
