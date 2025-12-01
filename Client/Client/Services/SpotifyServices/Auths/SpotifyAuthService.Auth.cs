using Microsoft.Extensions.Configuration;
using Microsoft.Maui.Authentication;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices.Auths
{
    public partial class SpotifyAuthService
    {
        private readonly IConfiguration _configuration;
        private readonly HttpClient _httpClient;
        private readonly string? clientId;
        private readonly string? redirectUri;
        private string? _codeVerifier;

        public SpotifyAuthService(IConfiguration configuration)
        {
            _configuration = configuration;
            _httpClient = new HttpClient();
            clientId = _configuration["Spotify:ClientId"];
            redirectUri = _configuration["Spotify:RedirectUri"];

            refreshTokenStoragePath = _configuration["StorageName:SpotifyRefreshTokenPath"] ?? "spotify";
            accessTokenStoragePath = _configuration["StorageName:SpotifyAccessTokenPath"] ?? "spotify";
        }

        public async Task<string?> LoginWithSpotifyAsync()
        {
            // すでにログイン済みなら終了
            if (await StartUpRestore())
                return "ログイン済み";

            // まずストレージからcode_verifierを探す
            _codeVerifier = await SecureStorage.GetAsync("spotify_code_verifier");

            // なければ生成して保存
            if (string.IsNullOrEmpty(_codeVerifier))
            {
                _codeVerifier = PkceHelper.CreateCodeVerifier();
                await SecureStorage.SetAsync("spotify_code_verifier", _codeVerifier);
                Debug.WriteLine($"[SpotifyAuth] 新しい Verifier Created: {_codeVerifier}");
            }

            // code_verifierからchallenge生成（ここでは新しくしない）
            var codeChallenge = PkceHelper.CreateCodeChallenge(_codeVerifier);
            Debug.WriteLine($"[SpotifyAuth] Challenge: {codeChallenge}");

            var authUrl = $"https://accounts.spotify.com/authorize" +
                $"?client_id={clientId}" +
                $"&response_type=code" +
                $"&redirect_uri={redirectUri}" +
                $"&scope=user-read-private%20user-read-email" +
                $"&code_challenge={codeChallenge}" +
                $"&code_challenge_method=S256";
            Debug.WriteLine($"①/authorizeリクエストurl:"+authUrl);
            var result = await WebAuthenticator.AuthenticateAsync(
                new WebAuthenticatorOptions
                {
                    Url = new Uri(authUrl),
                    CallbackUrl = new Uri(redirectUri)
                }
            );
            Debug.WriteLine($"①/authorizeリクエストresult:" + result);

            if (result?.Properties.TryGetValue("code", out var code) == true)
                return code;

            return null;
        }
    }

}
