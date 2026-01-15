using Server.src.Signaling.Models;
using System.Collections.Concurrent;

namespace Server.src.Signaling.Hubs
{
    public class SessionManager
    {
        private readonly ConcurrentDictionary<string, Session> _sessions = new();
        private readonly IConfiguration _configuration;
        /*---後でServiceも初期化するよん---*/

        public SessionManager(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public Session CreateSession(string hosyConnectionId)
        {
            //セッションIDを生成
            var sessionId = GenerateSessionId();
            var session = new Session
            {
                SessionId = sessionId,
                HostConnectionId = hosyConnectionId,
                CreatedAt = DateTime.UtcNow,
                LastActivity = DateTime.UtcNow,
                Status = SessionStatus.Active
            };

            _sessions.TryAdd(sessionId, session);

            /*セッション情報をDBに保存する処理を後で書く*/


            return session;
        }


        //SessionIdからセッションを取得する関数
        public Session? GetSession(string sessionId)
        {
            _sessions.TryGetValue(sessionId, out var session);
            return session;
        }

        //セッション情報を更新する関数
        public bool UpdateSession(Session session)
        {
            session.LastActivity = DateTime.UtcNow;
            _sessions[session.SessionId] = session;

            /*セッション情報を更新するService*/

            return true;
        }

        //セッションを閉じる関数
        public bool CloseSession(string sessionId)
        {
            if(_sessions.TryRemove(sessionId,out var session))
            {
                session.Status = SessionStatus.Closed;


                /*Service処理*/

                return true;
            }


            return false;
        }

        //ゲストをセッションに追加する
        public void AddGuest(string sessionId,Guest guest)
        {
            if(_sessions.TryGetValue(sessionId,out var session))
            {
                session.Guests.Add(guest);
                session.LastActivity = DateTime.UtcNow;

                /*Service処理*/
            }
        }

        //ゲストの退出処理
        public bool RemoveGuest(LeaveSessionRequest request)
        {
            if(_sessions.TryGetValue(request.SessionId,out var session))
            {
                session.Guests.RemoveAll(g => g.UserId == request.UserId);
                session.LastActivity = DateTime.UtcNow;
                return true;
                /*Service処理*/
            }
            return false;
        }


        //プレイリストにトラックを追加する
        public void AddTrackToPlaylist(string sessionId,PlaylistItem item)
        {
            if(_sessions.TryGetValue(sessionId,out var session))
            {
                item.Order = session.Playlist.Count;
                session.Playlist.Add(item);
                session.LastActivity = DateTime.UtcNow;

                /*Service処理*/
            }
        }

        public bool RemoveTrackFromPlaylist(string sessionId, string itemId, string requestedByUserId, bool isHost)
        {
            if (_sessions.TryGetValue(sessionId, out var session))
            {
                var item = session.Playlist.FirstOrDefault(p => p.Id == itemId);
                if (item == null)
                    return false;

                // ホストは全削除可能、ゲストは自分のリクエストのみ削除可能
                if (!isHost && item.RequestedByUserId != requestedByUserId)
                    return false;

                session.Playlist.Remove(item);
                ReorderPlaylist(session);
                session.LastActivity = DateTime.UtcNow;

                /*Service処理*/

                return true;
            }
            return false;
        }


        //指定された順番で並び替える
        public bool ReorderPlaylist(string sessionId, List<string> orderedItemIds)
        {
            if (_sessions.TryGetValue(sessionId, out var session))
            {
                var newPlaylist = new List<PlaylistItem>();
                foreach (var itemId in orderedItemIds)
                {
                    var item = session.Playlist.FirstOrDefault(p => p.Id == itemId);
                    if (item != null)
                    {
                        item.Order = newPlaylist.Count;
                        newPlaylist.Add(item);
                    }
                }

                session.Playlist = newPlaylist;
                session.LastActivity = DateTime.UtcNow;

                /*Service処理*/
                return true;
            }
            return false;
        }

        //今ある順番を正として Order を振り直す
        private void ReorderPlaylist(Session session)
        {
            for(int i = 0;i < session.Playlist.Count; i++)
            {
                session.Playlist[i].Order = i;
            }
        }

        //非アクティブなセッションをチェックして状態を更新する関数
        public void CheckInactiveSessions()
        {
            var timeoutMinutes = _configuration.GetValue<int>("SessionSettings:InactivityTimeoutMinutes", 30);
            var cutoffTime = DateTime.UtcNow.AddMinutes(-timeoutMinutes);

            foreach(var session in _sessions.Values.Where(s=>s.Status == SessionStatus.Active))
            {
                if(session.LastActivity < cutoffTime)
                {
                    session.Status = SessionStatus.Inactive;

                    /*Service処理*/
                }
            }
        }

        /**
         * セッションのIDを生成する関数
         * 
         * 俺がわかりやすいように8文字の大文字英数字にしてるけど
         * ユーザーに見せる必要がないならもっと長くしても良いかも
         */
        private string GenerateSessionId()
        {
            return Guid.NewGuid().ToString("N").Substring(0, 8).ToUpper();
        }

    }
}
