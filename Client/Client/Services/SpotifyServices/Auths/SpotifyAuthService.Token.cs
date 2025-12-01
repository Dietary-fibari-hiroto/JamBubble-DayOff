using System;
using System.Diagnostics;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices.Auths
{
    public partial class SpotifyAuthService
    {
        //実際にトークンを取得する関数
        public async Task<SpotifyTokenResponse?> ExchangeCodeForTokenAsync(string code)
        {
            if (_codeVerifier == null)
                _codeVerifier = await SecureStorage.GetAsync("spotify_code_verifier");

            Debug.WriteLine($"[SpotifyAuth] Stored Verifier: {_codeVerifier}");

            if (string.IsNullOrEmpty(_codeVerifier))
            {
                Debug.WriteLine("[SpotifyAuth] ERROR: code_verifier 不明");
                return null;
            }

            var values = new Dictionary<string, string> {
                {"client_id", clientId },
                {"grant_type", "authorization_code" },
                {"code", code},
                {"redirect_uri", redirectUri },
                {"code_verifier", _codeVerifier }
            };
            Debug.WriteLine($"②/api/tokenリクエストvalues:" +values );

            var content = new FormUrlEncodedContent(values);
            var response = await _httpClient.PostAsync("https://accounts.spotify.com/api/token", content);
            var body = await response.Content.ReadAsStringAsync();
            Debug.WriteLine("②/api/tokenリクエストresuponseのbody:" + body);
            var tokenResult = JsonSerializer.Deserialize<SpotifyTokenResponse>(body);
            if (tokenResult?.access_token != null)
            {
                await SecureStorage.SetAsync(accessTokenStoragePath, tokenResult.access_token);

                if (!string.IsNullOrEmpty(tokenResult.refresh_token))
                    await SecureStorage.SetAsync(refreshTokenStoragePath, tokenResult.refresh_token);

                _accessToken = tokenResult.access_token;
                _refreshToken = tokenResult.refresh_token ?? await SecureStorage.GetAsync(refreshTokenStoragePath);

                _expiresAt = DateTime.UtcNow.AddSeconds(tokenResult.expires_in);

                //  ------- ここ重要 --------
                // 認証成功したので code_verifier はもう不要 →削除
                SecureStorage.Remove("spotify_code_verifier");
            }

            return tokenResult;
        }



        //リフレッシュトークンをもちいてアクセストークンを取得する関数
        public async Task<string?> RefreshSpotifyTokenAsync()
        {
            var values = new Dictionary<string, string>
            {
                {"client_id",clientId },
                {"grant_type","refresh_token" },
                {"refresh_token",_refreshToken }
            };
            var content = new FormUrlEncodedContent(values);

            var response = await _httpClient.PostAsync("https://accounts.spotify.com/api/token", content);

            //失敗したときの処理
            if (!response.IsSuccessStatusCode)
            {
                //refreshTokenが有効でない場合もあるのでログアウト処理実行かな
                return null;
            }

            var body = await response.Content.ReadAsStringAsync();

            SpotifyTokenResponse conversionData = JsonSerializer.Deserialize<SpotifyTokenResponse>(body);

            //トークンをストレージに保存
            await SecureStorage.SetAsync(accessTokenStoragePath, conversionData.access_token);
            _accessToken = conversionData.access_token;
            _expiresAt = DateTime.UtcNow.AddSeconds(conversionData.expires_in);
            return _accessToken;
        }

        public async Task<string> GetAccessTokenAsync()
        {
            // 未取得 or 有効期限切れなら再取得
            if (_accessToken == null || DateTime.UtcNow >= _expiresAt)
            {
                await RefreshSpotifyTokenAsync();
            }

            if(_accessToken == null)
            {
                return "tokenないわ";
            }
            var refresh = await SecureStorage.GetAsync(refreshTokenStoragePath);
            var access = await SecureStorage.GetAsync(accessTokenStoragePath);

            Debug.WriteLine($"Refresh: {refresh}");
            Debug.WriteLine($"Access: {access}");


            return _accessToken!;
        }

    }
}
