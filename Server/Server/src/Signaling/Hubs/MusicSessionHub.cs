using Microsoft.AspNetCore.SignalR;
using Server.src.Signaling.Models;
using Server.src.Application.Services;


namespace Server.src.Signaling.Hubs
{
    public class MusicSessionHub : Hub
    {
        private readonly SessionManager _sessionManager;
        private readonly ILogger<MusicSessionHub> _logger;

        public MusicSessionHub(SessionManager sessionManager, ILogger<MusicSessionHub> logger)
        {
            _sessionManager = sessionManager;
            _logger = logger;
        }

        // ホストがセッションを作成
        public async Task<CreateSessionResponse> CreateSession(CreateSessionRequest request)
        {
            try
            {
                var session = _sessionManager.CreateSession(Context.ConnectionId);

                // TODO: 設定ファイル（appsettings.json 等）から取得する
                var baseUrl = Environment.GetEnvironmentVariable("BASE_URL");
                var guestUrl = $"{baseUrl}/session/{session.SessionId}";

                // ホストをセッショングループに追加
                await Groups.AddToGroupAsync(Context.ConnectionId, session.SessionId);

                _logger.LogInformation(
                    "セッションが作成されました。SessionId: {SessionId}, HostConnectionId: {ConnectionId}",
                    session.SessionId,
                    Context.ConnectionId
                );

                return new CreateSessionResponse
                {
                    SessionId = session.SessionId,
                    GuestUrl = guestUrl
                };
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "セッション作成中にエラーが発生しました");
                throw new HubException("セッションの作成に失敗しました");
            }
        }


        // ゲストがセッションに参加
        public async Task<bool> JoinSession(JoinSessionRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null || session.Status != SessionStatus.Active)
                {
                    throw new HubException("セッションが存在しない、または非アクティブです");
                }

                var guest = new Guest
                {
                    UserId = Guid.NewGuid().ToString(),
                    Name = request.GuestName,
                    ConnectionId = Context.ConnectionId,
                    JoinedAt = DateTime.UtcNow
                };

                // ゲストをセッションに追加
                _sessionManager.AddGuest(request.SessionId, guest);

                // セッショングループに追加
                await Groups.AddToGroupAsync(Context.ConnectionId, request.SessionId);

                // セッション参加者全員にゲスト参加を通知
                await Clients.Group(request.SessionId).SendAsync("GuestJoined", guest);

                // 参加したゲストに現在のプレイリストを送信
                await Clients.Caller.SendAsync("PlaylistUpdated", session.Playlist);

                _logger.LogInformation(
                    "ゲストがセッションに参加しました。SessionId: {SessionId}, GuestName: {GuestName}",
                    request.SessionId,
                    guest.Name
                );

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "セッション参加中にエラーが発生しました");
                throw new HubException("セッションへの参加に失敗しました");
            }
        }


        // トラックをプレイリストに追加
        public async Task<bool> AddTrack(AddTrackRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません");
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

                // プレイリストに曲を追加
                _sessionManager.AddTrackToPlaylist(request.SessionId, playlistItem);

                // セッション参加者全員にプレイリスト更新を通知
                await Clients.Group(request.SessionId).SendAsync("PlaylistUpdated", session.Playlist);

                _logger.LogInformation(
                    "曲が追加されました。SessionId: {SessionId}, TrackName: {TrackName}",
                    request.SessionId,
                    request.TrackName
                );

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "曲追加中にエラーが発生しました");
                throw new HubException("曲の追加に失敗しました");
            }
        }

        // プレイリストの並び替え（ホストのみ）
        public async Task<bool> ReorderPlaylist(ReorderPlaylistRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません");
                }

                // ホストのみ並び替え可能
                if (session.HostConnectionId != Context.ConnectionId)
                {
                    throw new HubException("プレイリストの並び替えはホストのみ可能です");
                }

                _sessionManager.ReorderPlaylist(request.SessionId, request.OrderedItemIds);

                // セッション参加者全員にプレイリスト更新を通知
                await Clients.Group(request.SessionId).SendAsync("PlaylistUpdated", session.Playlist);

                _logger.LogInformation(
                    "プレイリストが並び替えられました。SessionId: {SessionId}",
                    request.SessionId
                );

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "プレイリスト並び替え中にエラーが発生しました");
                throw new HubException("プレイリストの並び替えに失敗しました");
            }
        }

        // ホストが曲の再生を開始したとき
        public async Task<bool> UpdateTrackStatus(UpdateTrackStatusRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません");
                }

                var item = session.Playlist.FirstOrDefault(i => i.Id == request.ItemId);
                if (item == null)
                {
                    _logger.LogWarning(
                        "トラックが見つかりません。SessionId: {SessionId}, ItemId: {ItemId}",
                        request.SessionId,
                        request.ItemId
                    );
                    return false;
                }


                item.Status = request.Status;

                //セッション参加者全員にプレイリスト更新を通知
                await Clients.Group(request.SessionId).SendAsync("PlaylistUpdated", session.Playlist);

                _logger.LogInformation(
                    "✅ トラック状態が更新されました。SessionId: {SessionId}, ItemId: {ItemId}, Status: {Status}",
                    request.SessionId,
                    request.ItemId,
                    request.Status
                );

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "トラック状態更新中にエラーが発生しました");
                throw new HubException("トラック状態の更新に失敗しました");
            }
        }

        // トラックを削除
        public async Task<bool> RemoveTrack(RemoveTrackRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません");
                }

                var isHost = session.HostConnectionId == Context.ConnectionId;

                // 曲削除（ホスト or リクエスト本人のみ）
                var success = _sessionManager.RemoveTrackFromPlaylist(
                    request.SessionId,
                    request.ItemId,
                    request.RequestedByUserId,
                    isHost
                );

                if (!success)
                {
                    throw new HubException("曲を削除できませんでした");
                }

                // セッション参加者全員にプレイリスト更新を通知
                await Clients.Group(request.SessionId).SendAsync("PlaylistUpdated", session.Playlist);

                _logger.LogInformation(
                    "曲が削除されました。SessionId: {SessionId}, ItemId: {ItemId}",
                    request.SessionId,
                    request.ItemId
                );

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "曲削除中にエラーが発生しました");
                throw new HubException("曲の削除に失敗しました");
            }
        }

        // セッションを終了（ホストのみ）
        public async Task<bool> CloseSession(string sessionId)
        {
            try
            {
                var session = _sessionManager.GetSession(sessionId);
                if (session == null)
                {
                    throw new HubException("セッションが見つかりません");
                }

                // ホストのみセッション終了可能
                if (session.HostConnectionId != Context.ConnectionId)
                {
                    throw new HubException("セッションを終了できるのはホストのみです");
                }

                _sessionManager.CloseSession(sessionId);

                // セッション参加者全員に終了通知
                await Clients.Group(sessionId).SendAsync("SessionClosed");

                _logger.LogInformation(
                    "セッションが終了しました。SessionId: {SessionId}",
                    sessionId
                );

                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "セッション終了中にエラーが発生しました");
                throw new HubException("セッションの終了に失敗しました");
            }
        }

        //ゲストのセッション退出関数
        public async Task<bool> RemoveGuest(LeaveSessionRequest request)
        {
            try
            {
                var session = _sessionManager.GetSession(request.SessionId);
                if(session  == null)
                {
                    throw new HubException("セッションが見つかりません。");
                }

                var removed = _sessionManager.RemoveGuest(request);

                if (!removed)
                {
                    _logger.LogWarning("ゲストが見つかりませんでした。");
                    return false;
                }

                await Groups.RemoveFromGroupAsync(Context.ConnectionId, request.SessionId);
                await Clients.Group(request.SessionId).SendAsync("GuestLeft", Context.ConnectionId);
                return true;

            }catch(Exception ex)
            {
                _logger.LogError(ex, "セッションの退出中にエラーが発生しました。");
                throw new HubException("セッションからの退出に失敗しました。");
            }
        }

        public Task<List<Guest>> GetMembers(string sessionId)
        {
            var session = _sessionManager.GetSession(sessionId);
            if(session == null)
            {
                return Task.FromResult(new List<Guest>());
            }
            return Task.FromResult(session.Guests.ToList());
        }

        public override async Task OnDisconnectedAsync(Exception? exception)
        {
            // 接続切断時の処理（ホスト切断時などを想定）
            // TODO: より効率的な方法で対象セッションを特定する
            // 現在は簡易実装として全セッションをチェックする想定

            _logger.LogInformation(
                "接続が切断されました。ConnectionId: {ConnectionId}",
                Context.ConnectionId
            );

            await base.OnDisconnectedAsync(exception);
        }

    }

}
