using Server.src.Entities;
namespace Server.src.Interfaces
{
    public interface IProviderRepository
    {
        public Task<List<Provider>> GetProvidersAsync();
    }
}
