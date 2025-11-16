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
    }
}
