using Server.src.Repositories;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.DTOs;
using Microsoft.AspNetCore.Identity;

namespace Server.src.Services
{
    public class ProviderService : IProviderService
    {
        private readonly IProviderRepository _repo;
        public ProviderService(IProviderRepository repo)
        {
            _repo = repo;
        }

        public async Task<List<Provider>> GetProvidersAsync()
        {
            var providers = await _repo.GetProvidersAsync();
            if(providers == null || providers.Count == 0)
            {
                return [];// 空
            }
            return providers;
        }
    }
}