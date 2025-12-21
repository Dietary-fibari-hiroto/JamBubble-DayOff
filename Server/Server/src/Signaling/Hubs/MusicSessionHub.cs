using Microsoft.AspNetCore.SignalR;
using Server.src.Signaling.Models;
using Server.src.Application.Services;


namespace Server.src.Signaling.Hubs
{
    public class MusicSessionHub : Hub
    {
        private readonly SessionManager _sessionManager;
        private readonly ILogger<MusicSessionHub> _logger;

        public MusicSessionHub(SessionManager sessionManager,ILogger<MusicSessionHub> logger)
        {
            _sessionManager = sessionManager;
            _logger = logger;
        }

        //ホストがセッションを作成するメソッド
        public async Task<CreateSessionResponse> CreateSession(CreateSessionRequest request)
        {
            try
            {
                var session = _sessionManager.CreateSession(Context.ConnectionId);
                var baseUrl = "http://192.168.10.10"; //後で設定から取得するように変更予定
                var guestUrl = $"{baseUrl}/session/{session.SessionId}";

                _logger.LogInformation($"Sessionを作成しました。SessionId: {session.SessionId}, HostConnectionId: {session.HostConnectionId}");


                return new CreateSessionResponse
                {
                    SessionId = session.SessionId,
                    GuestUrl = guestUrl
                };
            }
            catch (Exception ex)
            {
                _logger.LogError($"セッション作成中にエラーが発生しました: {ex.Message}");
                throw;
            }
        }

        public async Task<bool> JoinSession(JoinSessionRequest request)
        {
            try
            {
                //セッションが存在するか確認
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null || session.Status != SessionStatus.Active)
                {
                    throw new Exception("該当のセッションが見つかりませんでした。");
                }

                var guest = new Guest
                {
                    UserId = Guid.NewGuid().ToString(),
                    Name = request.GuestName,
                    ConnectionId = Context.ConnectionId,
                    JoinedAt = DateTime.UtcNow
                };

                _sessionManager.AddGuest(request.SessionId, guest);

                //セッショングループに追加
                await Groups.AddToGroupAsync(Context.ConnectionId, request.SessionId);
                //全員に新しいゲストが参加したことを通知
                await Clients.Group(request.SessionId).SendAsync("GuestJoined", guest);
                //参加したゲストに現在のプレイリストを送信
                await Clients.Caller.SendAsync("PlaylistUpdated", session.Playlist);

                _logger.LogInformation($"ゲストがセッションに参加しました。SessionId: {request.SessionId}, GuestName: {guest.Name}, GuestConnectionId: {guest.ConnectionId}");

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError($"セッション参加中にエラーが発生しました: {ex.Message}");
                return false;
            }
        }

        //トラックをプレイリストに追加すうメソッド
        public async Task<bool> AddTrack(AddTrackRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません。");
                }

                var playlistItem = new PlaylistItem
                {
                    SpotifyTrackId = request.SpotifyTrackId,
                    TrackName = request.TrackName,
                    ArtistName = request.ArtistName,
                    AlbumName = request.AlbumName,
                    AlbumImageUrl = request.AlbumImageUrl,
                    DurationMs = request.DurationMs,
                    RequestedBy = request.RequestedBy,
                    RequestedByUserId = request.RequestedByUserId,
                    RequestedAt = DateTime.UtcNow
                };


                _sessionManager.AddTrackToPlaylist(request.SessionId, playlistItem);

                //全員にプレイリストの更新を通知
                await Clients.Group(request.SessionId).SendAsync("PlaylistUpdated", session.Playlist);

                _logger.LogInformation($"トラックがプレイリストに追加されました。SessionId: {request.SessionId}, TrackName: {playlistItem.TrackName}, RequestedBy: {playlistItem.RequestedBy}");
                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError($"トラック追加中にエラーが発生しました: {ex.Message}");
                return false;
            }
        }

        //プレイリストの並び替え(とりあえずホストのみ)
        public async Task<bool> ReorderPlaylist(ReorderPlaylistRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません。");
                }

                //ホストの並び替え可能
                if (session.HostConnectionId != Context.ConnectionId)
                {
                    throw new HubException("ホストのみ並び替えが可能です。");
                }

                _sessionManager.ReorderPlaylist(request.SessionId, request.OrderedItemIds);

                //全員にプレイリストの更新を通知
                await Clients.Group(request.SessionId).SendAsync("PlaylistUpdated", session.Playlist);
                _logger.LogInformation($"プレイリストが並び替えられました。SessionId: {request.SessionId}");

                return true;

            }
            catch (Exception ex)
            {
                _logger.LogError($"プレイリスト並び替え中にエラーが発生しました: {ex.Message}");
                return false;
            }
        }

        //トラック削除
        public async Task<bool> RemoveTrack(RemoveTrackRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません。");
                }

                var isHost = session.HostConnectionId == Context.ConnectionId;
                var success = _sessionManager.RemoveTrackFromPlaylist(
                    request.SessionId,
                    request.ItemId,
                    request.RequestedByUserId,
                    isHost
                    );

                if (success)
                {
                    throw new HubException("トラックの削除に失敗しました。");
                }

                //全員にプレイリスト更新を通知
                await Clients.Group(request.SessionId).SendAsync("PlaylistUpdated", session.Playlist);
                _logger.LogInformation($"トラックがプレイリストから削除されました。SessionId: {request.SessionId}, ItemId: {request.ItemId}");

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError($"トラック削除中にエラーが発生しました: {ex.Message}");
                return false;
            }
        }


        //セッション終了
        public async Task<bool> CloseSession(string sessionId)
        {
            try
            {
                var session = _sessionManager.GetSession(sessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません。");
                }
                if (session.HostConnectionId != Context.ConnectionId)
                {
                    throw new HubException("ホストのみセッションを終了できます。");
                }

                _sessionManager.CloseSession(sessionId);

                //全員にセッション終了を通知
                await Clients.Group(sessionId).SendAsync("SessionClosed");
                _logger.LogInformation($"セッションが終了されました。SessionId: {sessionId}");
                return true;

            }
            catch (Exception ex)
            {
                _logger.LogError($"セッション終了中にエラーが発生しました: {ex.Message}");
                return false;
            }
        }

        public override async Task OnDisconnectedAsync(Exception? exception)
        {
            //ホストが切断された場合の処理
            var sessions = new List<Session>();
            //セッション検索してホストのものを探して一定時間で終了させる処理を追加予定
            //ここはDBから取ってこよう

            _logger.LogInformation($"クライアントが切断されました。ConnectionId: {Context.ConnectionId}");
            await base.OnDisconnectedAsync(exception);

        }
    }

}
