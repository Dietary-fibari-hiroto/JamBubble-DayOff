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
    [Route("/api/friend")]
    public class FriendController : ControllerBase
    {
        private readonly IFriendService _friendService;

        public FriendController(IFriendService friendService)
        {
            _friendService = friendService;
        }

        [Authorize]
        [HttpGet("{n}")]
        [ProducesResponseType(typeof(List<FriendResposeDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriends(int n)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var friends = await _friendService.GetFriendsAsync(userId.Value, n);
            return Ok(friends);
        }
    }
}