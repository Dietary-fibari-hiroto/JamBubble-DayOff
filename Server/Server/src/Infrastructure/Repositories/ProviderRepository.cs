using Microsoft.EntityFrameworkCore;
using Server.src.Interfaces;
using Server.Data;
using Server.src.Entities;
namespace Server.src.Repositories
{
    public class ProviderRepository : IProviderRepository
    {
        private readonly AppDbContext _context;
        public ProviderRepository(AppDbContext context)
        {
            _context = context;
        }

        public async Task<List<Provider>> GetProvidersAsync()
        {
            return await _context.Providers.ToListAsync();
        }
    }
}