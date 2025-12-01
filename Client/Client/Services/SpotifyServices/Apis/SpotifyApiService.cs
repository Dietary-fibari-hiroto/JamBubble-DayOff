using Client.Services.SpotifyServices.Auths;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices.Apis
{
    public class SpotifyApiService
    {
        private readonly SpotifyAuthService _auth;
        private readonly HttpClient _httpClient;

        public SpotifyApiService(SpotifyAuthService auth,HttpClient http)
        {
            _auth = auth;
            _httpClient = http;
        }

        public async Task<SpotifyUserProfile?> GetUserProfileAsync()
        {
            var accessToken = await _auth.GetAccessTokenAsync();

            if (accessToken == null)
            {
                return null;
            }


            _httpClient.DefaultRequestHeaders.Authorization =
                new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", accessToken);

            var res = await _httpClient.GetAsync("https://api.spotify.com/v1/me");

            if (!res.IsSuccessStatusCode)return null;


            var json = await res.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<SpotifyUserProfile>(json);
            
        }

    }
}
