using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices.Auths
{
    //認証の直前で生成する
    /*
     * var codeVerifier = PkceHelper.CreateCodeVerifier();
     * var codeChallenge = PkceHelper.CreateCodeChallenge(codeVerifier);
     */
    public static class PkceHelper
    {
        public static string CreateCodeVerifier()
        {
            var rng = RandomNumberGenerator.Create();
            var bytes = new byte[32];
            rng.GetBytes(bytes);

            return Base64UrlEncode(bytes);
        }

        public static string CreateCodeChallenge(string codeVerifier)
        {
            using var sha256 = SHA256.Create();
            var hash = sha256.ComputeHash(Encoding.UTF8.GetBytes(codeVerifier));

            return Base64UrlEncode(hash);
        }

        private static string Base64UrlEncode(byte[] input)
        {
            return Convert.ToBase64String(input)
                .Replace("+", "-")
                .Replace("/", "_")
                .Replace("=", "");
        }
    }

}
