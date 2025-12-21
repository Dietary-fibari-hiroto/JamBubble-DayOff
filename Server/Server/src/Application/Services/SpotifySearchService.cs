using SpotifyAPI.Web;
using Server.src.Signaling.Models;

namespace Server.src.Application.Services
{
    public class SpotifySearchService
    {
        private readonly IConfiguration _configuration;
        private SpotifyClient? _spotifyClient;

        public SpotifySearchService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        //Spotifyにリクエストするためのクライアントを取得
        private async Task<SpotifyClient> GetSpotifyClientAsync()
        {
            if(_spotifyClient != null) return _spotifyClient;

            var clientId = _configuration["Spotify:ClientId"];
            var clientSecret = _configuration["Spotify:ClientSecret"];

            if(string.IsNullOrEmpty(clientId) || string.IsNullOrEmpty(clientSecret))
            {
                throw new InvalidOperationException("Spotify接続に必要な設定が不足しています。");
            }

            //Spotifyのクライアント認証を行う
            var config = SpotifyClientConfig.CreateDefault();
            //クライアント認証リクエストを作成
            var request = new ClientCredentialsRequest(clientId, clientSecret);
            //トークンをリクエスト
            var response = await new OAuthClient(config).RequestToken(request);

            //Spotifyクライアントを作成
            _spotifyClient = new SpotifyClient(config.WithToken(response.AccessToken));
            return _spotifyClient;
        }


        //Spotifyで楽曲を検索する
        public async Task<List<SpotifyTrackSearchResult>> SearchTracksAsync(string query, int limit = 30)
        {
            try
            {
                var spotify = await GetSpotifyClientAsync();
                var searchRequest = new SearchRequest(SearchRequest.Types.Track, query)
                {
                    Limit = limit
                };

                var searchResponse = await spotify.Search.Item(searchRequest);
                var results = new List<SpotifyTrackSearchResult>();

                if (searchResponse.Tracks?.Items == null) return results;

                foreach (var track in searchResponse.Tracks.Items)
                {
                    results.Add(new SpotifyTrackSearchResult
                    {
                        Id = track.Id,
                        Name = track.Name,
                        Artist = string.Join(", ", track.Artists.Select(a => a.Name)),
                        Album = track.Album.Name,
                        AlbumImageUrl = track.Album.Images.FirstOrDefault()?.Url ?? string.Empty,
                        DurationMs = track.DurationMs
                    });
                }
                return results;

            }
            catch (Exception ex)
            {
                Console.WriteLine($"Spotify検索中にエラーが発生しました: {ex.Message}");
                return new List<SpotifyTrackSearchResult>();

            }
        }

        public async Task<SpotifyTrackSearchResult?> GetTrackByIdAsync(string trackId)
        {
            try
            {
                var spotify = await GetSpotifyClientAsync();
                var track = await spotify.Tracks.Get(trackId);

                if (track == null) return null;

                return new SpotifyTrackSearchResult
                {
                    Id = track.Id,
                    Name = track.Name,
                    Artist = string.Join(", ", track.Artists.Select(a => a.Name)),
                    Album = track.Album.Name,
                    AlbumImageUrl = track.Album.Images.FirstOrDefault()?.Url ?? string.Empty,
                    DurationMs = track.DurationMs
                };

            }
            catch (Exception ex)
            {
                //エラーハンドリング
                Console.WriteLine($"Spotifyトラック取得中にエラーが発生しました: {ex.Message}");
                return null;
            }
        }


    }
}
