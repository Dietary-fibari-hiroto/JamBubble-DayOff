using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;

namespace Server.src.Repositories
{
    public class MessageRepository : IMessageRepository
    {
        public readonly AppDbContext _context;
        public MessageRepository(AppDbContext context)
        {
            _context = context;
        }

        public async Task<List<Message>> GetMessagesByUserIdAsync(int userId, bool asTracking = true)
        {
            IQueryable<Message> query = _context.Messages
                .Where(m => m.UserId == userId)
                .OrderBy(m => m.CreatedAt);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }

            return await query.ToListAsync();
        }

        public async Task<Message?> GetMessageByIdAsync(int userId, int messageId, bool asTracking = true)
        {
            IQueryable<Message> query = _context.Messages
                .Where(m => m.UserId == userId && m.Id == messageId);

            if (!asTracking)
            {
                query = query.AsNoTracking(); // 追跡オフ 
            }

            return await query.FirstOrDefaultAsync();
        }


        public async Task<Message?> AddMessageAsync(Message message)
        {
            throw new NotImplementedException();
        }

        public async Task UpdateMessageAsync(Message message)
        {
            await _context.SaveChangesAsync();
        }

        public async Task DeleteMessageAsync(Message message)
        {
            _context.Messages.Remove(message);
            await _context.SaveChangesAsync();
        }
    }
}
