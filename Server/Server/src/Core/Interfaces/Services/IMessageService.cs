using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IMessageService
    {
        public Task<List<MessagesResponseDto>> GetMessagesAsync(int userId);
        public Task<bool> UpdateMessageAsync(int userId, int messageId);
        public Task<bool> DeleteMessageAsync(int userId, int messageId);
    }
}
