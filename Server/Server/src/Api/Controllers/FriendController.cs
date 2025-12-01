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
        /// <param name="n">取得件数(必須ではない)</param>
        /// <param name="skip">スキップする件数(必須ではない)</param>
        /// <returns></returns>
        [Authorize]
        [HttpGet]
        [ProducesResponseType(typeof(List<FriendResposeDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriends([FromQuery] int? n, [FromQuery] int? skip)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            int takeCount = n ?? 100000;
            int skipCount = skip ?? 0;
            var friends = await _friendService.GetFriendsAsync(userId.Value, takeCount, skipCount);
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
        /// <param name="n">取得件数(必須ではない)</param>
        /// <param name="skip">スキップする件数(必須ではない)</param>
        /// <returns></returns>
        [Authorize]
        [HttpGet("fornow")]
        [ProducesResponseType(typeof(List<FornowSimpResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriendFornows([FromQuery] int? n, [FromQuery] int? skip)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            int takeCount = n ?? 100000;
            int skipCount = skip ?? 0;
            var fornows = await _friendService.GetFriendsFornowsAsync(userId.Value, takeCount, skipCount);
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

        /// <summary>
        /// 送信・受信したフレンドリクエスト一覧を取得
        /// </summary>
        /// <returns></returns>
        [Authorize]
        [HttpGet("requests")]
        [ProducesResponseType(typeof(FriendRequestSndRcvDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriendRequests()
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var friendRequests = await _friendService.GetfriendRequestSndRcv(userId.Value);
            return Ok(friendRequests);
        }

        /// <summary>
        /// フレンドリクエストを送信
        /// </summary>
        /// <param name="targetuserid">ターゲットのユーザーID</param>
        /// <returns></returns>
        [Authorize]
        [HttpPost("{targetuserid}/request")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> RequestFriend(int targetuserid)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var result = await _friendService.RequestFriendAsync(userId.Value, targetuserid);
            if (!result)
            {
                return BadRequest("Failed to send friend request.");
            }
            return Ok();
        }

        /// <summary>
        /// フレンドリクエストを承認
        /// </summary>
        /// <param name="requestuserid">リクエストしたユーザーID</param>
        /// <returns></returns>
        [Authorize]
        [HttpPost("{requestuserid}/accept")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> AcceptFriendRequest(int requestuserid)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }
            var result = await _friendService.ProprietyFriendAsync(userId.Value, requestuserid, true);
            if (!result)
            {
                return BadRequest("Failed to accept friend request.");
            }
            return Ok();
        }

        /// <summary>
        /// フレンドリクエストを拒否
        /// </summary>
        /// <param name="requestuserid">リクエストしたユーザーID</param>
        /// <returns></returns>
        [Authorize]
        [HttpPost("{requestuserid}/reject")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> RejectFriendRequest(int requestuserid)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }
            var result = await _friendService.ProprietyFriendAsync(userId.Value, requestuserid, false);
            if (!result)
            {
                return BadRequest("Failed to reject friend request.");
            }
            return Ok();
        }

        /// <summary>
        /// フレンドリクエストを削除(どちら側からでも可能)
        /// </summary>
        /// <param name="targetuserid">ターゲットのユーザーId</param>
        /// <returns></returns>
        [Authorize]
        [HttpDelete("{targetuserid}/request")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        public async Task<IActionResult> DeleteRequestFriend(int targetuserid)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }
            var result = await _friendService.DeleteFriendRequestAsync(userId.Value, targetuserid);
            if (!result)
            {
                return BadRequest("Failed to delete friend request.");
            }
            return NoContent();
        }
    }
}