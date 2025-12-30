
namespace Server.src.Signaling.Models
{

    //セッション事態のモデル
    public class Session
    {
        public string SessionId { get; set; } = string.Empty;
        public string HostConnectionId { get; set; } = string.Empty;
        public DateTime CreatedAt { get; set; }
        public DateTime LastActivity { get; set; }
        public List<PlaylistItem> Playlist { get; set; } = new();
        public List<Guest> Guests { get; set; } = new();
        public SessionStatus Status { get; set; }

    }

    //プレイリストのアイテムモデル
    public class PlaylistItem
    {
        public string Id { get; set; } = Guid.NewGuid().ToString();
        public string SpotifyTrackId { get; set; } = string.Empty;
        public string TrackName { get; set; } = string.Empty;
        public string ArtistName { get; set; } = string.Empty;
        public string AlbumName { get; set; } = string.Empty;
        public string AlbumImageUrl { get; set; } = string.Empty;
        public int DurationMs { get; set; }
        public string RequestedBy { get; set; } = string.Empty;
        public string RequestedByUserId { get; set; } = string.Empty;
        public DateTime RequestedAt { get; set; }
        public int Order { get; set; }
        public PlaybackStatus Status { get; set; } = PlaybackStatus.Pending;
    }

    //ゲストモデル
    public class Guest
    {
        public string UserId { get; set; } = string.Empty;
        public string Name { get; set; } = string.Empty;
        public string ConnectionId { get; set; } = string.Empty;
        public DateTime JoinedAt { get; set; }
    }

    //セッションの状態を表す列挙型
    public enum SessionStatus
    {
        Active,
        Inactive,
        Closed
    }

    //セッション作成リクエスト
    public class CreateSessionRequest
    {
        public string HostDeviceId { get; set; } = string.Empty;
    }

    //セッション作成レスポンス
    public class CreateSessionResponse
    {
        public string SessionId { get; set; } = string.Empty;
        public string GuestUrl { get; set; } = string.Empty;
    }

    //セッション参加リクエスト
    public class JoinSessionRequest
    {
        public string SessionId { get; set; } = string.Empty;
        public string GuestName { get; set; } = string.Empty;
    }

    //トラック追加リクエスト
    public class AddTrackRequest
    {
        public string SessionId { get; set; } = string.Empty;
        public string SpotifyTrackId { get; set; } = string.Empty;
        public string TrackName { get; set; } = string.Empty;
        public string ArtistName { get; set; } = string.Empty;
        public string AlbumName { get; set; } = string.Empty;
        public string AlbumImageUrl { get; set; } = string.Empty;
        public int DurationMs { get; set; }
        public string RequestedBy { get; set; } = string.Empty;
        public string RequestedByUserId { get; set; } = string.Empty;
    }

    //トラック並び替えリクエスト
    public class ReorderPlaylistRequest
    {
        public string SessionId { get; set; } = string.Empty;
        public List<string> OrderedItemIds { get; set; } = new();
    }

    //トラック削除リクエスト
    public class RemoveTrackRequest
    {
        public string SessionId { get; set; } = string.Empty;
        public string ItemId { get; set; } = string.Empty;
        public string RequestedByUserId { get; set; } = string.Empty;
    }
    //トラック更新リクエスト
    public class UpdateTrackStatusRequest
    {
        public string SessionId { get; set; } = string.Empty;
        public string ItemId { get; set; } = string.Empty;
        public PlaybackStatus Status { get; set; }
    }

    //Spotifyトラック検索結果モデル
    public class SpotifyTrackSearchResult
    {
        public string Id { get; set; } = string.Empty;
        public string Name { get; set; } = string.Empty;
        public string Artist { get; set; } = string.Empty;
        public string Album { get; set; } = string.Empty;
        public string AlbumImageUrl { get; set; } = string.Empty;
        public int DurationMs { get; set; }
    }

    public enum PlaybackStatus
    {
        Pending,    // 未再生
        Playing,    // 再生中
        Completed   // 再生済み
    }

}
