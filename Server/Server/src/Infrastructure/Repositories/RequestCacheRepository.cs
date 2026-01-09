using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;
using Server.src.DTOs;

namespace Server.src.Repositories
{
    public class RequestCacheRepository : IRequestCacheRepository
    {
        private readonly AppDbContext _context;
        public RequestCacheRepository(AppDbContext context)
        {
            _context = context;
        }

        // 追加
        public async Task<RequestCache> AddRequestCacheAsync(RequestCache rc)
        {
            await _context.AddAsync(rc);
            await _context.SaveChangesAsync();
            return rc;
        }
    }
}