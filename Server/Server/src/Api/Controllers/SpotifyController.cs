
using Microsoft.AspNetCore.Mvc;
using Server.src.Application.Services;

namespace Server.src.Api.Controllers
{
    public class SpotifyController:ControllerBase
    {
        private readonly SpotifySearchService _spotifyService;
        private readonly ILogger<SpotifyController> _logger;

        public SpotifyController(SpotifySearchService spotifySevice, ILogger<SpotifyController> logger)
        {
            _spotifyService = spotifySevice;
            _logger = logger;
        }

        [HttpGet("/api/search")]
        public async Task<IActionResult> Search([FromQuery] string query, [FromQuery] int limit = 30)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(query))
                {
                    return BadRequest("クエリパラメータが必要です。");
                }

                var results = await _spotifyService.SearchTracksAsync(query, limit);
                return Ok(results);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Spotify検索中にエラーが発生しました。");
                return StatusCode(500, "内部サーバーエラーが発生しました。");
            }
        }

        [HttpGet("/api/track/{trackId}")]
        public async Task<IActionResult> GetTrack(string trackId)
        {
            try
            {
                var track = await _spotifyService.GetTrackByIdAsync(trackId);
                if (track == null)
                {
                    return NotFound("指定されたIDのトラックが見つかりません。");
                }
                return Ok(track);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Spotifyトラック取得中にエラーが発生しました。");
                return StatusCode(500, "内部サーバーエラーが発生しました。");
            }
        }
    }
}
