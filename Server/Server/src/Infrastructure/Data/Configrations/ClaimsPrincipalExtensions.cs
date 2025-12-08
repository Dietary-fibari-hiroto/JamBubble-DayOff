using System.Security.Claims;

namespace Server.src.Configrations
{
    /// <summary>
    /// JWTの認証トークンからユーザーIDを取得するクラス
    /// </summary>
    public static class ClaimsPrincipalExtensions
    {
        public static int? GetUserId(this ClaimsPrincipal user)
        {
            if (user == null)
            {
                return null;
            }

            // JWTからユーザーIDを取得
            var userIdString = user.Claims.FirstOrDefault(
                c => c.Type == ClaimTypes.NameIdentifier
                )?.Value;

            if (string.IsNullOrEmpty(userIdString))
            {
                return null;
            }

            // Intに変換
            if (!int.TryParse(userIdString, out var userId))
            {
                return null;
            }

            return userId;
        }   
    }
}
