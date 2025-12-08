using Server.src.DTOs;
using Server.src.Entities;

namespace Server.src.Interfaces
{
    public interface IProviderService
    {
        public Task<List<Provider>> GetProvidersAsync();
    }
}