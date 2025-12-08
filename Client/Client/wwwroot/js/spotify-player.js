let player;
let dotNetRef;
let currentAccessToken;

// Spotify Web Playback SDK を初期化
window.initializeSpotifyPlayer = function () {
    if (window.Spotify) {
        console.log('Spotify SDK already loaded');
        return;
    }

    const script = document.createElement('script');
    script.src = 'https://sdk.scdn.co/spotify-player.js';
    script.async = true;
    document.head.appendChild(script);

    console.log('Loading Spotify Web Playback SDK...');
};

// SDKが読み込まれたら自動的に呼ばれる
window.onSpotifyWebPlaybackSDKReady = () => {
    console.log('✅ Spotify Web Playback SDK Ready');
};

// .NET側からの参照を保存
window.setDotNetReference = function (ref) {
    dotNetRef = ref;
    console.log('DotNet reference set');
};

// プレイヤーに接続
window.connectSpotifyPlayer = async function (accessToken) {
    currentAccessToken = accessToken;

    if (!window.Spotify) {
        console.error('❌ Spotify SDK not loaded yet');
        return false;
    }

    try {
        player = new Spotify.Player({
            name: 'DayOff Client Player',
            getOAuthToken: cb => { cb(accessToken); },
            volume: 0.5
        });

        // エラーハンドリング
        player.addListener('initialization_error', ({ message }) => {
            console.error('❌ Initialization error:', message);
            notifyError('Initialization failed: ' + message);
        });

        player.addListener('authentication_error', ({ message }) => {
            console.error('❌ Authentication error:', message);
            notifyError('Authentication failed: ' + message);
        });

        player.addListener('account_error', ({ message }) => {
            console.error('❌ Account error:', message);
            notifyError('Spotify Premium account required!');
        });

        player.addListener('playback_error', ({ message }) => {
            console.error('❌ Playback error:', message);
            notifyError('Playback failed: ' + message);
        });

        // プレイヤー準備完了
        player.addListener('ready', ({ device_id }) => {
            console.log('✅ Player ready with Device ID:', device_id);

            if (dotNetRef) {
                dotNetRef.invokeMethodAsync('OnPlayerReady', device_id);
            }
        });

        // プレイヤーが切断された
        player.addListener('not_ready', ({ device_id }) => {
            console.log('⚠️ Player disconnected:', device_id);
        });

        // プレイヤー状態の変更を監視
        player.addListener('player_state_changed', state => {
            if (!state) {
                console.log('No state available');
                return;
            }

            console.log('Player state changed:', state);

            const track = state.track_window.current_track;
            const isPlaying = !state.paused;
            const position = state.position;
            const duration = state.duration;

            // Blazorに通知
            if (dotNetRef) {
                dotNetRef.invokeMethodAsync('OnPlayerStateChanged',
                    track.name,
                    track.artists.map(a => a.name).join(', '),
                    track.album.name,
                    track.uri,
                    isPlaying,
                    position,
                    duration
                );
            }
        });

        // 接続
        const connected = await player.connect();

        if (connected) {
            console.log('✅ Successfully connected to Spotify');
            return true;
        } else {
            console.error('❌ Failed to connect');
            return false;
        }
    } catch (error) {
        console.error('❌ Error connecting player:', error);
        return false;
    }
};

// プレイヤーを切断
window.disconnectSpotifyPlayer = function () {
    if (player) {
        player.disconnect();
        player = null;
        console.log('Player disconnected');
    }
};

// 再生/一時停止トグル
window.togglePlayback = function () {
    if (player) {
        player.togglePlay().then(() => {
            console.log('Toggled playback');
        });
    }
};

// 次の曲
window.skipToNext = function () {
    if (player) {
        player.nextTrack().then(() => {
            console.log('Skipped to next track');
        });
    }
};

// 前の曲
window.skipToPrevious = function () {
    if (player) {
        player.previousTrack().then(() => {
            console.log('Skipped to previous track');
        });
    }
};

// ボリューム設定 (0.0 - 1.0)
window.setPlayerVolume = function (volume) {
    if (player) {
        player.setVolume(volume).then(() => {
            console.log('Volume set to', volume);
        });
    }
};

// シーク (ミリ秒)
window.seekToPosition = function (positionMs) {
    if (player) {
        player.seek(positionMs).then(() => {
            console.log('Seeked to', positionMs);
        });
    }
};

// URIを再生
window.playSpotifyUri = async function (uri) {
    if (!currentAccessToken) {
        console.error('No access token available');
        return;
    }

    try {
        const response = await fetch('https://api.spotify.com/v1/me/player/play', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${currentAccessToken}`
            },
            body: JSON.stringify({
                uris: [uri]
            })
        });

        if (response.ok) {
            console.log('✅ Started playing:', uri);
        } else {
            const error = await response.text();
            console.error('❌ Failed to play URI:', error);
        }
    } catch (error) {
        console.error('❌ Error playing URI:', error);
    }
};

// プレイリストやアルバムを再生
window.playSpotifyContext = async function (contextUri) {
    if (!currentAccessToken) {
        console.error('No access token available');
        return;
    }

    try {
        const response = await fetch('https://api.spotify.com/v1/me/player/play', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${currentAccessToken}`
            },
            body: JSON.stringify({
                context_uri: contextUri
            })
        });

        if (response.ok) {
            console.log('✅ Started playing context:', contextUri);
        } else {
            const error = await response.text();
            console.error('❌ Failed to play context:', error);
        }
    } catch (error) {
        console.error('❌ Error playing context:', error);
    }
};

// エラーを.NETに通知
function notifyError(message) {
    if (dotNetRef) {
        dotNetRef.invokeMethodAsync('OnError', message);
    }
}