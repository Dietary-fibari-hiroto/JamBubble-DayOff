using Microsoft.EntityFrameworkCore;
using Server.Data;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;

namespace Server.src.Services
{
    public class MessageService : IMessageService
    {
        private readonly IMessageRepository _repo;

        public MessageService(IMessageRepository repo)
        {
            _repo = repo;
        }

        async Task<List<MessagesResponseDto>> IMessageService.GetMessagesAsync(int userId)
        {
            var messages = await _repo.GetMessagesByUserIdAsync(userId, false);
            if (messages == null || messages.Count == 0)
            {
                return new List<MessagesResponseDto>(); // 空のリスト
            }
            return messages.Select(m => new MessagesResponseDto(m)).ToList();
        }

        public async Task<bool> UpdateMessageAsync(int userId, int messageId)
        {
            var message = await _repo.GetMessageByIdAsync(userId, messageId);
            if (message == null)
            {
                return false;
            }

            message.IsRead = true;
            await _repo.UpdateMessageAsync(message);
            return true;
        }

        async Task<bool> IMessageService.DeleteMessageAsync(int userId, int messageId)
        {
            var message = await _repo.GetMessageByIdAsync(userId, messageId);
            if (message == null)
            {
                return false;
            }

            await _repo.DeleteMessageAsync(message);
            return true;
        }
    }
}
