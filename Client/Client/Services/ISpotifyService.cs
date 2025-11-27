using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.Platforms.Android.Services
{
    public interface ISpotifyService
    {
        event EventHandler<SpotifyConnectionEventArgs> ConnectionChanged;
        event EventHandler<PlayerStateEventArgs> PlayerStateChanged;

        Task<bool> ConnectAsync();
        void Disconnect();
        bool IsConnected { get; }

        void Play(string uri);
        void Pause();
        void Resume();
        void SkipNext();
        void SkipPrevious();
        void SeekTo(long positionMs);

        Task<PlayerStateInfo> GetCurrentPlayerStateAsync();
    }

    public class SpotifyConnectionEventArgs : EventArgs
    {
        public bool IsConnected { get; set; }
        public string ErrorMessage { get; set; }
    }

    public class PlayerStateEventArgs : EventArgs
    {
        public PlayerStateInfo State { get; set; }
    }

    public class PlayerStateInfo
    {
        public string TrackName { get; set; }
        public string ArtistName { get; set; }
        public bool IsPaused { get; set; }
        public long PositionMs { get; set; }
        public long DurationMs { get; set; }
    }
}
