using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices.Auths
{
    internal interface ISpotifyAuthService
    {
        Task<string?> GetAuthorizeUrlAsync(string state = null);
        Task<SpotifyTokenResponse?> ExchangeCodeForTokenAsync(string code);
        Task<string?> RefreshAccessTokenAsync();
        Task<bool> StartUpRestoreAsync();
        Task LogoutAsync();
        Task<bool> IsAuthenticatedAsync();
        Task<string?> GetAccessTokenAsync();
    }

    //トークン等を永続化する抽象インターフェース。
    public interface ITokenStorage
    {
        Task<string?> GetAsync(string key);
        Task SetAsync(string key, string value);
        Task RemoveAsync(string key);
    }
}
