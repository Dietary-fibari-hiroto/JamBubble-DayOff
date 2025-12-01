using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices
{
    public class SpotifyTokenResponse
    {
        public string access_token { get; set; } = string.Empty;
        public string token_type { get; set; } = string.Empty;
        public int expires_in { get; set; }
        public string refresh_token { get; set; } = string.Empty;
        public string scope { get; set; } = string.Empty;
    }
    public class SpotifyUserProfile
    {
        public string id { get; set; } = string.Empty;
        public string display_name { get; set; } = string.Empty;
        public string email { get; set; } = string.Empty;
        public string country { get; set; } = string.Empty;
        public string product { get; set; } = string.Empty;
        public object? images { get; set; }
    }
}
