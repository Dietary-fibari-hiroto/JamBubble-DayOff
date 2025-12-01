using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Server.src.DTOs;
using Server.src.Interfaces;
using Server.src.Services;
using Server.src.Entities;

namespace Server.src.Api.Controllers
{
    [ApiController]
    [Route("/api/favorite-song")]
    public class FavoriteSongController : ControllerBase
    {
        private readonly IFavoriteMusicService _favoMusicService;

        public FavoriteSongController(IFavoriteMusicService favoMusicService)
        {
            _favoMusicService = favoMusicService;
        }

        /// <summary>
        /// お気に入り音楽ランキング取得
        /// </summary>
        /// <param name="n">上位からの取得件数</param>
        /// <param name="skip">スキップする件数</param>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpGet("ranking")]
        [ProducesResponseType(typeof(IEnumerable<FavoriteMusicSummary>), StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFavoriteMusicRanking([FromQuery] int n = 10, [FromQuery] int skip = 0)
        {
            var ranking = await _favoMusicService.GetFavoriteMusicRankingAsync(n, skip);
            return Ok(ranking);
        }
    }
}
