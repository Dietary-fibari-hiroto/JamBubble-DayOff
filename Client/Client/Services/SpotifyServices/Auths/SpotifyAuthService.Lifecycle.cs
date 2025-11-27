using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices.Auths
{
    public partial class SpotifyAuthService
    {
        private readonly string refreshTokenStoragePath;
        private readonly string accessTokenStoragePath;

        private static string _refreshToken;
        private static string _accessToken;
        private DateTime _expiresAt;


        //アプリ起動時とかに呼び出して、ストレージにトークンを探しに行く
        public async Task<bool> StartUpRestore()
        {
            _refreshToken = _refreshToken ?? await SecureStorage.GetAsync(refreshTokenStoragePath);
            _accessToken = _accessToken ?? await SecureStorage.GetAsync(accessTokenStoragePath);

            // RefreshToken あるなら更新
            if (!string.IsNullOrEmpty(_refreshToken))
            {
                var newToken = await RefreshSpotifyTokenAsync();
                return newToken != null;
            }

            return !string.IsNullOrEmpty(_accessToken);
        }


    }
}
