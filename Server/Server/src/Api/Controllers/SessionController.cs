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
    [Route("/api/session")]
    public class SessionController : ControllerBase
    {
        private readonly ISessionService _sessionService;
        public SessionController(ISessionService sessionService)
        {
            _sessionService = sessionService;
        }

        /// <summary>
        /// ユーザーIDからセッションのリストを返す
        /// </summary>
        [AllowAnonymous]
        [HttpGet("{userid}")]
        [ProducesResponseType(typeof(List<SessionResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSessions(int userid)
        {
            var sessions = await _sessionService.GetSessionsAsync(userid);
            return Ok(sessions);
        }

        /// <summary>
        /// セッションIDからセッションの詳細を返す
        /// </summary>
        [AllowAnonymous]
        [HttpGet("{sessionid}/detail")]
        [ProducesResponseType(typeof(SessionDetailResponseDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSessionDetail(int sessionid)
        {
            var session = await _sessionService.GetSessionAsync(sessionid);
            if (session == null)
            {
                return NotFound("Session not found.");
            }
            return Ok(session);
        }

        /// <summary>
        /// 人気なセッションを取得
        /// </summary>
        /// <param name="n">取得件数(必須ではない)</param>
        /// <param name="skip">スキップする件数(必須ではない)</param>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpGet("popular")]
        [ProducesResponseType(typeof(List<SessionPopularResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSessionPopular([FromQuery] int? n , [FromQuery ]int? skip)
        {
            int takeCount = n ?? 100000;
            int skipCount = skip ?? 0;
            var sessions = await _sessionService.GetSessionPopularAsync(takeCount, skipCount);
            return Ok(sessions);
        }

        /// <summary>
        /// 全フレンドのアクティブな公開セッションを取得
        /// </summary>
        /// <param name="n">取得件数(必須ではない)</param>
        /// <param name="skip">スキップする件数(必須ではない)</param>
        /// <returns></returns>
        [Authorize]
        [HttpGet("friend")]
        [ProducesResponseType(typeof(List<SessionResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSessionFriends([FromQuery] int? n, [FromQuery] int? skip)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }
            int takeCount = n ?? 100000;
            int skipCount = skip ?? 0;
            var sessions = await _sessionService.GetSessionActPubFrinedAsync(userId.Value, takeCount, skipCount);
            return Ok(sessions);
        }

        /// <summary>
        /// 保存可能なセッションの一覧を取得
        /// </summary>
        /// <returns></returns>
        [Authorize]
        [HttpGet("history")]
        [ProducesResponseType(typeof(List<SessionResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSavePossibleSessions()
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }
            var sessions = await _sessionService.GetSavePossibleSessionsAsync(userId.Value);
            return Ok(sessions);
        }

        /// <summary>
        /// 新しいセッションを作成
        /// </summary>
        /// <param name="request"></param>
        /// <returns></returns>
        [Authorize]
        [HttpPost]
        [ProducesResponseType(typeof(SessionDetailResponseDto), StatusCodes.Status201Created)]
        public async Task<IActionResult> CreateSession([FromBody] SessionRequestDto request)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }
            var session = await _sessionService.AddSessionAsync(request, userId.Value);
            if (session == null)
            {
                return BadRequest("Failed to create session.");
            }
            return CreatedAtAction(nameof(GetSessionDetail), new { sessionid = session.Id }, session);
        }
    }
}
