using System.Diagnostics;
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

        public async Task<List<MessageNoContentResponseDto>> GetMessagesAsync(int userId)
        {
            var messages = await _repo.GetMessagesByUserIdAsync(userId, false);
            if (messages == null || messages.Count == 0)
            {
                return new List<MessageNoContentResponseDto>(); // 空のリスト
            }
            return messages.Select(m => new MessageNoContentResponseDto(m)).ToList();
        }

        public async Task<MessageResponseDto?> GetMessageAsync(int userId, int messageId)
        {
            var message = await _repo.GetMessageByIdAsync(userId, messageId);
            if (message == null)
            {
                return null;
            }
            return new MessageResponseDto(message);
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

        public async Task<bool> DeleteMessageAsync(int userId, int messageId)
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
