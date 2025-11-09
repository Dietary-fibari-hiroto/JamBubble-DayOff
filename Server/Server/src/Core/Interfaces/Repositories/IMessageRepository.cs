using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IMessageRepository
    {
        Task<List<Message>> GetMessagesByUserIdAsync(int userId, bool asTracking = true);
        Task<Message?> GetMessageByIdAsync(int userId, int messageId, bool asTracking = true);
        Task<Message?> AddMessageAsync(Message message);
        Task UpdateMessageAsync(Message message);
        Task DeleteMessageAsync(Message message);
    }
}
