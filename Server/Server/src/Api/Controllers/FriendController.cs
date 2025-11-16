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
    [Route("/api/friend")]
    public class FriendController : ControllerBase
    {
        private readonly IFriendService _friendService;

        public FriendController(IFriendService friendService)
        {
            _friendService = friendService;
        }

        /// <summary>
        /// フレンドの一覧を取得
        /// </summary>
        /// <param name="n">取得件数</param>
        /// <returns></returns>
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

        /// <summary>
        /// 1フレンドのセッション一覧を取得
        /// </summary>
        /// <param name="friendid">フレンドID</param>
        /// <returns></returns>
        [Authorize]
        [HttpGet("{friendid}/session")]
        [ProducesResponseType(typeof(List<SessionResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriendSessions(int friendid)
        {
            var Sessions = await _friendService.GetFriendSessionsAsync(friendid);
            return Ok(Sessions);
        }

        /// <summary>
        /// 全フレンドのFornowを取得
        /// </summary>
        /// <param name="n">取得件数</param>
        /// <returns></returns>
        [Authorize]
        [HttpGet("fornow/{n}")]
        [ProducesResponseType(typeof(List<FornowSimpResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriendFornows(int n)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }
            var fornows = await _friendService.GetFriendsFornowsAsync(userId.Value, n);
            return Ok(fornows);
        }

        /// <summary>
        /// フレンドのFornow詳細を取得
        /// </summary>
        /// <param name="friendid">フレンドのID</param>
        /// <returns></returns>
        /// <exception cref="NotImplementedException"></exception>
        [Authorize]
        [HttpGet("fornow/{friendid}/detail")]
        [ProducesResponseType(typeof(FornowDetailResponseDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriendFornowDeteil(int friendid)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var fornowDetail = await _friendService.GetFriendFornowDetailAsync(userId.Value, friendid);
            if (fornowDetail == null)
            {
                return NotFound("Fornow not found or not a friend.");
            }
            return Ok(fornowDetail);
        }

        /// <summary>
        /// フレンドのFornowにいいねを設定
        /// </summary>
        /// <param name="friendid">フレンドのID</param>
        /// <returns></returns>
        /// <exception cref="NotImplementedException"></exception>
        [Authorize]
        [HttpPost("fornow/{friendid}/like")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> SeLikeToFornow(int friendid)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var result = await _friendService.SetLikeToFornowAsync(userId.Value, friendid);
            if (!result)
            {
                return BadRequest("Failed to set like to friend's Fornow.");
            }
            return Ok();
        }
    }
}