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

        [HttpGet]
        public async Task<IActionResult> GetAll() => Ok(await _userService.GetAllUserAsync());

        /// <summary>
        /// ユーザー作成
        /// </summary>
        [AllowAnonymous]
        [HttpPost]
        [ProducesResponseType(typeof(User), StatusCodes.Status201Created)] // 成功時のレスポンス型 
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
