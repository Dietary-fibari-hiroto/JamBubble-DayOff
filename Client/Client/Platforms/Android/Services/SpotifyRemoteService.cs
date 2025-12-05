using System;
using System.Threading.Tasks;
using Android.Content;
using Com.Spotify.Protocol.Types;
using Com.Spotify.Protocol.Client;

namespace Client.Platforms.Android.Services
{
    /// <summary>
    /// Spotify App Remote SDK を使用したサービス
    /// あなたのKotlinコードをC#に移植
    /// </summary>
    public class SpotifyRemoteService
    {
        private const string CLIENT_ID = "";
        private const string REDIRECT_URI = "";

        private Java.Lang.Object _spotifyAppRemote; // 最初は汎用型で
        private readonly Context _context;
        private TaskCompletionSource<bool> _connectionTcs;

        // イベント
        public event EventHandler<bool> ConnectionChanged;
        public event EventHandler<TrackInfo> TrackChanged;
        public event EventHandler<bool> PlaybackStateChanged;

        public bool IsConnected => _spotifyAppRemote != null;

        public SpotifyRemoteService()
        {
            _context = Microsoft.Maui.ApplicationModel.Platform.CurrentActivity
                      ?? global::Android.App.Application.Context;
        }

        /// <summary>
        /// Spotifyアプリに接続
        /// </summary>
        public async Task<bool> ConnectAsync()
        {
            _connectionTcs = new TaskCompletionSource<bool>();

            try
            {
                // リフレクションで SpotifyAppRemote と ConnectionParams を取得
                var spotifyAppRemoteType = Type.GetType("Com.Spotify.Android.Appremote.Api.SpotifyAppRemote, Mono.Android");
                var connectionParamsType = Type.GetType("Com.Spotify.Android.Appremote.Api.ConnectionParams, Mono.Android");
                var builderType = Type.GetType("Com.Spotify.Android.Appremote.Api.ConnectionParams+Builder, Mono.Android");

                if (spotifyAppRemoteType == null || builderType == null)
                {
                    System.Diagnostics.Debug.WriteLine("[SpotifyRemote] SpotifyAppRemote types not found");
                    return false;
                }

                System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Creating ConnectionParams...");

                // ConnectionParams.Builder を作成
                var builderConstructor = builderType.GetConstructor(new[] { typeof(string) });
                var builder = builderConstructor?.Invoke(new object[] { CLIENT_ID });

                if (builder == null)
                {
                    System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Failed to create Builder");
                    return false;
                }

                // SetRedirectUri
                var setRedirectUriMethod = builderType.GetMethod("SetRedirectUri");
                builder = setRedirectUriMethod?.Invoke(builder, new object[] { REDIRECT_URI });

                // ShowAuthView
                var showAuthViewMethod = builderType.GetMethod("ShowAuthView");
                builder = showAuthViewMethod?.Invoke(builder, new object[] { true });

                // Build
                var buildMethod = builderType.GetMethod("Build");
                var connectionParams = buildMethod?.Invoke(builder, null);

                System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Connecting with reflection...");

                // Connect
                var connectMethod = spotifyAppRemoteType.GetMethod("Connect",
                    new[] { typeof(Context), connectionParamsType, Type.GetType("Com.Spotify.Android.Appremote.Api.IConnector+IConnectionListener, Mono.Android") });

                if (connectMethod == null)
                {
                    System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Connect method not found");
                    return false;
                }

                connectMethod.Invoke(null, new[] { _context, connectionParams, new ConnectionListener(this) });

                return await _connectionTcs.Task;
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Connect error: {ex}");
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Stack trace: {ex.StackTrace}");
                return false;
            }
        }

        /// <summary>
        /// 接続解除
        /// </summary>
        public void Disconnect()
        {
            if (_spotifyAppRemote != null)
            {
                try
                {
                    var spotifyAppRemoteType = _spotifyAppRemote.GetType();
                    var disconnectMethod = spotifyAppRemoteType.GetMethod("Disconnect",
                        System.Reflection.BindingFlags.Public | System.Reflection.BindingFlags.Static);

                    disconnectMethod?.Invoke(null, new[] { _spotifyAppRemote });
                    _spotifyAppRemote = null;

                    ConnectionChanged?.Invoke(this, false);
                    System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Disconnected");
                }
                catch (Exception ex)
                {
                    System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Disconnect error: {ex}");
                }
            }
        }

        /// <summary>
        /// PlayerApiを取得
        /// </summary>
        private Java.Lang.Object GetPlayerApi()
        {
            try
            {
                if (_spotifyAppRemote == null) return null;

                var playerApiProperty = _spotifyAppRemote.GetType().GetProperty("PlayerApi");
                return playerApiProperty?.GetValue(_spotifyAppRemote) as Java.Lang.Object;
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] GetPlayerApi error: {ex}");
                return null;
            }
        }

        /// <summary>
        /// 次の曲
        /// </summary>
        public void SkipNext()
        {
            try
            {
                var playerApi = GetPlayerApi();
                if (playerApi == null) return;

                var skipNextMethod = playerApi.GetType().GetMethod("SkipNext");
                skipNextMethod?.Invoke(playerApi, null);

                System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Skip next");
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] SkipNext error: {ex}");
            }
        }

        /// <summary>
        /// 前の曲
        /// </summary>
        public void SkipPrevious()
        {
            try
            {
                var playerApi = GetPlayerApi();
                if (playerApi == null) return;

                var skipPreviousMethod = playerApi.GetType().GetMethod("SkipPrevious");
                skipPreviousMethod?.Invoke(playerApi, null);

                System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Skip previous");
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] SkipPrevious error: {ex}");
            }
        }

        /// <summary>
        /// URIで曲を再生
        /// </summary>
        public void Play(string uri)
        {
            try
            {
                var playerApi = GetPlayerApi();
                if (playerApi == null) return;

                var playMethod = playerApi.GetType().GetMethod("Play", new[] { typeof(string) });
                playMethod?.Invoke(playerApi, new object[] { uri });

                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Playing: {uri}");
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Play error: {ex}");
            }
        }

        /// <summary>
        /// 一時停止
        /// </summary>
        public void Pause()
        {
            try
            {
                var playerApi = GetPlayerApi();
                if (playerApi == null) return;

                var pauseMethod = playerApi.GetType().GetMethod("Pause", Type.EmptyTypes);
                pauseMethod?.Invoke(playerApi, null);

                System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Paused");
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Pause error: {ex}");
            }
        }

        /// <summary>
        /// 再開
        /// </summary>
        public void Resume()
        {
            try
            {
                var playerApi = GetPlayerApi();
                if (playerApi == null) return;

                var resumeMethod = playerApi.GetType().GetMethod("Resume", Type.EmptyTypes);
                resumeMethod?.Invoke(playerApi, null);

                System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Resumed");
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Resume error: {ex}");
            }
        }

        /// <summary>
        /// 再生/一時停止トグル
        /// </summary>
        public void TogglePlayPause()
        {
            try
            {
                var playerApi = GetPlayerApi();
                if (playerApi == null) return;

                var playerStateProperty = playerApi.GetType().GetProperty("PlayerState");
                var playerState = playerStateProperty?.GetValue(playerApi);

                if (playerState != null)
                {
                    var setResultCallbackMethod = playerState.GetType().GetMethod("SetResultCallback");
                    setResultCallbackMethod?.Invoke(playerState, new object[] { new TogglePlayPauseCallback(playerApi) });
                }
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] TogglePlayPause error: {ex}");
            }
        }

        /// <summary>
        /// プレイヤーの状態を購読
        /// </summary>
        private void SubscribeToPlayerState()
        {
            try
            {
                var playerApi = GetPlayerApi();
                if (playerApi == null) return;

                var subscribeMethod = playerApi.GetType().GetMethod("SubscribeToPlayerState", Type.EmptyTypes);
                var subscription = subscribeMethod?.Invoke(playerApi, null);

                if (subscription != null)
                {
                    var setEventCallbackMethod = subscription.GetType().GetMethod("SetEventCallback");
                    setEventCallbackMethod?.Invoke(subscription, new object[] { new PlayerStateCallback(this) });

                    System.Diagnostics.Debug.WriteLine("[SpotifyRemote] Subscribed to player state");
                }
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Subscribe error: {ex}");
            }
        }

        // 接続リスナー - 汎用的な実装
        private class ConnectionListener : Java.Lang.Object
        {
            private readonly SpotifyRemoteService _service;

            public ConnectionListener(SpotifyRemoteService service)
            {
                _service = service;
            }

            [Android.Register("onConnected", "(Ljava/lang/Object;)V", "")]
            public void OnConnected(Java.Lang.Object remote)
            {
                _service._spotifyAppRemote = remote;
                _service._connectionTcs?.TrySetResult(true);
                _service.ConnectionChanged?.Invoke(_service, true);

                System.Diagnostics.Debug.WriteLine("[SpotifyRemote] ✅ Connected!");

                _service.SubscribeToPlayerState();
            }

            [Android.Runtime.Register("onFailure", "(Ljava/lang/Throwable;)V", "")]
            public void OnFailure(Java.Lang.Throwable error)
            {
                _service._connectionTcs?.TrySetResult(false);
                _service.ConnectionChanged?.Invoke(_service, false);

                System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] ❌ Connection failed: {error.Message}");
            }
        }

        // プレイヤー状態コールバック
        private class PlayerStateCallback : Java.Lang.Object
        {
            private readonly SpotifyRemoteService _service;

            public PlayerStateCallback(SpotifyRemoteService service)
            {
                _service = service;
            }

            [Android.Runtime.Register("onEvent", "(Ljava/lang/Object;)V", "")]
            public void OnEvent(Java.Lang.Object data)
            {
                try
                {
                    if (data is PlayerState state)
                    {
                        var track = state.Track;
                        if (track != null)
                        {
                            var trackInfo = new TrackInfo
                            {
                                Name = track.Name ?? "Unknown Track",
                                Artist = track.Artist?.Name ?? "Unknown Artist",
                                Album = track.Album?.Name ?? "Unknown Album",
                                Uri = "",
                                Duration = track.Duration
                            };

                            _service.TrackChanged?.Invoke(_service, trackInfo);
                        }

                        var isPlaying = !state.IsPaused;
                        _service.PlaybackStateChanged?.Invoke(_service, isPlaying);
                    }
                }
                catch (Exception ex)
                {
                    System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] PlayerStateCallback error: {ex}");
                }
            }
        }

        // 再生/一時停止トグル用コールバック
        private class TogglePlayPauseCallback : Java.Lang.Object
        {
            private readonly Java.Lang.Object _playerApi;

            public TogglePlayPauseCallback(Java.Lang.Object playerApi)
            {
                _playerApi = playerApi;
            }

            [Android.Runtime.Register("onResult", "(Ljava/lang/Object;)V", "")]
            public void OnResult(Java.Lang.Object data)
            {
                try
                {
                    if (data is PlayerState state)
                    {
                        var methodName = state.IsPaused ? "Resume" : "Pause";
                        var method = _playerApi.GetType().GetMethod(methodName, Type.EmptyTypes);
                        method?.Invoke(_playerApi, null);

                        System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] {methodName}");
                    }
                }
                catch (Exception ex)
                {
                    System.Diagnostics.Debug.WriteLine($"[SpotifyRemote] Toggle error: {ex}");
                }
            }
        }
    }

    /// <summary>
    /// トラック情報
    /// </summary>
    public class TrackInfo
    {
        public string Name { get; set; }
        public string Artist { get; set; }
        public string Album { get; set; }
        public string Uri { get; set; }
        public long Duration { get; set; }
    }
}