using Android.App;
using Android.Content;
using Android.Content.PM;
using Microsoft.Maui.Authentication;

namespace Client.Platforms.Android
{

    [Activity(
        Exported = true,
        LaunchMode = LaunchMode.SingleTask,
        NoHistory = true)]
    [IntentFilter(
        new[] { Intent.ActionView },
        Categories = new[] {
        Intent.CategoryDefault,
        Intent.CategoryBrowsable },
        DataScheme = "jambubble",
        DataHost = "callback")]
    public class SpotifyCallbackActivity
        : WebAuthenticatorCallbackActivity
    {
    }


}
