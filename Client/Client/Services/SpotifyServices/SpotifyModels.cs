using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.Services.SpotifyServices
{
    public class SpotifyTokenResponse
    {
        public string access_token { get; set; }
        public string token_type { get; set; }
        public int expires_in { get; set; }
        public string refresh_token { get; set; }
        public string scope { get; set; }
    }
    public class SpotifyUserProfile
    {
        public string id { get; set; }
        public string display_name { get; set; }
        public string email { get; set; }
        public string country { get; set; }
        public string product { get; set; }
        public object images { get; set; }
    }
}
