using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Extensions;
using System.Runtime.Serialization;

namespace Server.src.Api.Controllers
{
    [ApiController]
    [Route("/api/message")]
    public class MessagesController : ControllerBase
    {
        private readonly IMessageService _messageService;
        public MessagesController(IMessageService messageService)
        {
            _messageService = messageService;
        }

        /// <summary>
        /// ユーザーへのメッセージ一覧を返す
        /// </summary>
        [Authorize]
        [HttpGet]
        [ProducesResponseType(typeof(List<MessageNoContentResponseDto>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetMessages()
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var messages = await _messageService.GetMessagesAsync(userId.Value);
            return Ok(messages);
        }

        /// <summary>
        /// メッセージ内容を返す
        /// </summary>
        [Authorize]
        [HttpGet("{id}/detail")]
        [ProducesResponseType(typeof(MessageResponseDto), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetMessage(int id)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var message = await _messageService.GetMessageAsync(userId.Value, id);
            if (message == null)
            {
                return NotFound();
            }

            return Ok(message);
        }

        /// <summary>
        /// メッセージに既読をつける
        /// </summary>
        [Authorize]
        [HttpPatch("{id}/is-read")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> UpdateIsReadMessage(int id)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var result = await _messageService.UpdateMessageAsync(userId.Value, id);
            if (!result)
            {
                return NotFound();
            }
            return Ok();
        }

        /// <summary>
        /// メッセージの削除
        /// </summary>
        [Authorize]
        [HttpGet("{id}")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        public async Task<IActionResult> DeletMessage(int id)
        {
            var userId = User.GetUserId(); // JWTからIDを取得
            if (userId == null)
            {
                return Unauthorized("Invalid user ID format in token.");
            }

            var result = await _messageService.DeleteMessageAsync(userId.Value, id);
            if (!result)
            {
                return NotFound();
            }

            return NoContent();
        }
    }
}
