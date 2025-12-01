using Android.Content;
using System;

using Com.Spotify.Android.Appremote.Api;
using Com.Spotify.Protocol.Client;
using Com.Spotify.Protocol.Types;
using Java.Lang;


namespace Client.Platforms.Android.Services
{
    public class SpotifyService : ISpotifyService
    {
        private SpotifyAppRemote? _spotifyAppRemote;//Spotifyアプリ本体へのリモート接続インスタンス
        private readonly Context _context; // AndroidのContext
        private TaskCompletionSource<bool>? _connectionTcs; //接続完了を待つためのTaskCompletionSource
        private TaskCompletionSource<PlayerStateInfo>? _playerStateTcs; //プレイヤーステート取得用のTCS

        private const string CLIENT_ID = ""; 
        private const string REDIRECT_URI = "";

        public event EventHandler<SpotifyConnectionEventArgs>? ConnectionChanged; //接続状況が変化したら発火するイベント
        public event EventHandler<PlayerStateEventArgs>? PlayerStateChanged;//曲状態が変化したら発火するイベント

        public bool IsConnected => _spotifyAppRemote != null && (_spotifyAppRemote.IsConnected);//接続状態のプロパティ

        public SpotifyService()
        {
            _context = Platform.CurrentActivity ?? global::Android.App.Application.Context;//MAUIの現在のActivityを取得
        }

        //Spotifyへ非同期で接続するメソッド
        public async Task<bool> ConnectAsync()
        {
            _connectionTcs = new TaskCompletionSource<bool>(TaskCreationOptions.RunContinuationsAsynchronously);//接続完了を待つためのTCSを初期化

            var connectionParams = new ConnectionParams.Builder(CLIENT_ID)//接続パラメータを生成
                .SetRedirectUri(REDIRECT_URI)
                .Build();


            try
            {
                var listener = new ConnectionListener(this);//接続結果を受け取るリスナーを生成

                try
                {
                    var connectMethod = typeof(SpotifyAppRemote).GetMethod("Connect", new Type[] { typeof(Context), typeof(ConnectionParams), listener.GetType() });//リフレクションでConnectメソッドを取得
                    if (connectMethod != null)
                    {
                        connectMethod.Invoke(null, new object[] { _context, connectionParams, listener });//見つかったらInvokeで呼び出し
                    }
                    else
                    {
                        var factoryType = Type.GetType("Com.Spotify.Android.Appremote.Internal.SdkRemoteClientConnectorFactory, Client");//万が一のfallback処理別の方法としてFactoryクラスを探す
                        if (factoryType != null)
                        {

                        }
                    }
                }
                catch (System.Exception ex)
                {
                    System.Diagnostics.Debug.WriteLine($"Spotify Connect reflection call failed: {ex.Message}");//失敗したらログ出力
                }
            }
            catch (System.Exception ex)
            {
                _connectionTcs.TrySetException(ex);//接続失敗時にTCSに例外をセット
            }

            return await _connectionTcs.Task;//接続完了待ち
        }


        //Spotifyから切断するメソッド
        public void Disconnect()
        {
            if (_spotifyAppRemote != null)
            {
                SpotifyAppRemote.Disconnect(_spotifyAppRemote);//Spotifyから切断
                _spotifyAppRemote = null;
            }

            ConnectionChanged?.Invoke(this, new SpotifyConnectionEventArgs
            {
                IsConnected = false//切断後にイベント発火
            });
        }

        //再生するメソッド
        public void Play(string uri)
        {
            try
            {
                _spotifyAppRemote?.PlayerApi?.Play(uri);
            }
            catch (System.Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"Play error: {ex}");
            }
        }

        //一時停止するメソッド
        public void Pause()
        {
            _spotifyAppRemote?.PlayerApi?.Pause();
        }

        //再開するメソッド
        public void Resume()
        {
            _spotifyAppRemote?.PlayerApi?.Resume();
        }

        //次の曲にスキップするメソッド
        public void SkipNext()
        {
            _spotifyAppRemote?.PlayerApi?.SkipNext();
        }

        //前の曲にスキップするメソッド
        public void SkipPrevious()
        {
            _spotifyAppRemote?.PlayerApi?.SkipPrevious();
        }

        //指定した位置にシークするメソッド
        public void SeekTo(long positionMs)
        {
            _spotifyAppRemote?.PlayerApi?.SeekTo(positionMs);
        }

        //現在のプレイヤーステートを非同期で取得するメソッド
        public async Task<PlayerStateInfo> GetCurrentPlayerStateAsync()
        {
            _playerStateTcs = new TaskCompletionSource<PlayerStateInfo>(TaskCreationOptions.RunContinuationsAsynchronously);//PlayerStateのTCSを初期化

            try
            {
                var callResult = _spotifyAppRemote?.PlayerApi?.PlayerState;//PlayerStateを取得するCallResultAPIを呼び出し
                if (callResult != null)
                {
                    callResult.SetResultCallback(new PlayerStateResultCallback(this));//成功時のコールバックをセット
                    callResult.SetErrorCallback(new CallErrorCallback(this));//失敗時のコールバックをセット
                }
                else
                {
                    _playerStateTcs.TrySetException(new System.Exception("PlayerApi.PlayerState is null"));//nullチェック
                }
            }
            catch (System.Exception ex)
            {
                _playerStateTcs.TrySetException(ex);//例外発生時にTCSにセット
            }

            return await _playerStateTcs.Task;//PlayerStateの取得完了を待つ
        }

        //プレイヤーステートのサブスクリプションをセットアップするメソッド
        private void SetupPlayerStateSubscription()
        {
            try
            {
                var subscription = _spotifyAppRemote?.PlayerApi?.SubscribeToPlayerState();//PlayerStateのサブスクリプションを取得
                if (subscription != null)
                {
                    subscription.SetEventCallback(new PlayerStateEventCallback(this));//イベントコールバックをセット
                    subscription.SetErrorCallback(new SubscriptionErrorCallback(this));//エラーコールバックをセット
                }
            }
            catch (System.Exception ex)
            {
                System.Diagnostics.Debug.WriteLine($"Subscribe error: {ex}");
            }
        }


        private class ConnectionListener : Java.Lang.Object, Com.Spotify.Android.Appremote.Api.IConnector.IConnectionListener
        {
            private readonly SpotifyService _service;

            public ConnectionListener(SpotifyService service)
            {
                _service = service;
            }

            //接続成功時に呼ばれるメソッド
            public void OnConnected(SpotifyAppRemote? spotifyAppRemote)
            {
                if (spotifyAppRemote == null)
                {
                    _service._connectionTcs?.TrySetResult(false);
                    _service.ConnectionChanged?.Invoke(_service, new SpotifyConnectionEventArgs { IsConnected = false, ErrorMessage = "Null spotifyAppRemote" });
                    return;
                }
                //接続成功時の処理
                _service._spotifyAppRemote = spotifyAppRemote;//SpotifyAppRemoteインスタンスを保存
                _service.SetupPlayerStateSubscription();//プレイヤーステートのサブスクリプションをセットアップ
                _service._connectionTcs?.TrySetResult(true);//接続成功をTCSにセット
                _service.ConnectionChanged?.Invoke(_service, new SpotifyConnectionEventArgs { IsConnected = true });//接続成功イベントを発火
            }

            //接続失敗時に呼ばれるメソッド
            public void OnFailure(Java.Lang.Throwable? error)
            {
                var msg = error?.Message ?? "unknown";
                _service._connectionTcs?.TrySetResult(false);
                _service.ConnectionChanged?.Invoke(_service, new SpotifyConnectionEventArgs { IsConnected = false, ErrorMessage = msg });//接続失敗イベントを発火
            }
        }

   
        private class PlayerStateEventCallback : Java.Lang.Object, Com.Spotify.Protocol.Client.Subscription.IEventCallback
        {
            private readonly SpotifyService _service;

            public PlayerStateEventCallback(SpotifyService service)
            {
                _service = service;
            }

            //プレイヤーステートが変化したときに呼ばれるメソッド
            public void OnEvent(Java.Lang.Object? p0)
            {
                if (p0 is PlayerState state)
                {
                    var info = new PlayerStateInfo
                    {
                        TrackName = state.Track?.Name ?? "Unknown",
                        ArtistName = state.Track?.Artist?.Name ?? "Unknown",
                        IsPaused = state.IsPaused,
                        PositionMs = state.PlaybackPosition,
                        DurationMs = state.Track?.Duration ?? 0
                    };

                    _service.PlayerStateChanged?.Invoke(_service, new PlayerStateEventArgs { State = info });
                }
            }
        }


        private class PlayerStateResultCallback : Java.Lang.Object, Com.Spotify.Protocol.Client.CallResult.IResultCallback
        {
            private readonly SpotifyService _service;

            public PlayerStateResultCallback(SpotifyService service)
            {
                _service = service;
            }

            //プレイヤーステート取得成功時に呼ばれるメソッド
            public void OnResult(Java.Lang.Object? p0)
            {
                if (p0 is PlayerState state)
                {
                    var info = new PlayerStateInfo
                    {
                        TrackName = state.Track?.Name ?? "Unknown",
                        ArtistName = state.Track?.Artist?.Name ?? "Unknown",
                        IsPaused = state.IsPaused,
                        PositionMs = state.PlaybackPosition,
                        DurationMs = state.Track?.Duration ?? 0
                    };

                    _service._playerStateTcs?.TrySetResult(info);
                }
                else
                {
                    _service._playerStateTcs?.TrySetException(new System.Exception("OnResult did not return PlayerState"));
                }
            }
        }

        private class CallErrorCallback : Java.Lang.Object, Com.Spotify.Protocol.Client.IErrorCallback
        {
            private readonly SpotifyService _service;

            public CallErrorCallback(SpotifyService service)
            {
                _service = service;
            }
            //プレイヤーステート取得失敗時に呼ばれるメソッド
            public void OnError(Java.Lang.Throwable? error)
            {
                var msg = error?.Message ?? "unknown";
                _service._playerStateTcs?.TrySetException(new System.Exception(msg));
            }
        }

        private class SubscriptionErrorCallback : Java.Lang.Object, Com.Spotify.Protocol.Client.IErrorCallback
        {
            private readonly SpotifyService _service;

            public SubscriptionErrorCallback(SpotifyService service)
            {
                _service = service;
            }

            //サブスクリプションエラー時に呼ばれるメソッド
            public void OnError(Java.Lang.Throwable? error)
            {
                var msg = error?.Message ?? "subscription error";
                System.Diagnostics.Debug.WriteLine($"Subscription error: {msg}");
            }
        }
    }
}
