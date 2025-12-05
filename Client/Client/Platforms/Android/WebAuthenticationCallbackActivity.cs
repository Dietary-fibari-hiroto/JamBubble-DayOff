using Android.App;
using Android.Content;
using Android.Content.PM;

namespace Client.Platforms.Android
{
    [Activity(
        Exported = true,
        NoHistory = true,
        LaunchMode = LaunchMode.SingleTop
    )]
    [IntentFilter(
        new[] { Intent.ActionView },
        Categories = new[]
        {
            Intent.CategoryDefault,
            Intent.CategoryBrowsable
        },
        DataScheme = "jambubble",
        DataHost = "callback"
    )]
    public class WebAuthenticationCallbackActivity : Microsoft.Maui.Authentication.WebAuthenticatorCallbackActivity
    {
    }
}
