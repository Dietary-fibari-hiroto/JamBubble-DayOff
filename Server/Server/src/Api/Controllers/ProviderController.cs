using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ActionConstraints;
using Microsoft.Extensions.Logging;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Services;

namespace Server.src.Api.Controllers
{
    [ApiController]
    [Route("/api/provider")]
    public class ProviderContller : ControllerBase
    {
        private readonly IProviderService _providerService;
        public ProviderContller(IProviderService providerService)
        {
            _providerService = providerService;
        }

        /// <summary>
        /// プロバイダーの一覧取得
        /// </summary>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpGet]
        [ProducesResponseType(typeof(Provider), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetProviders()
        {
            var providers = await _providerService.GetProvidersAsync();

            return Ok(providers);
        }
    }
}