using MusicSession.Server.Models;
using System.Collections.Concurrent;

namespace MusicSession.Server.Services;

public class SessionManager
{
    private readonly ConcurrentDictionary<string, Session> _sessions = new();
    private readonly IConfiguration _configuration;
    // TODO: DB永続化の場合は ISessionService を注入
    // private readonly ISessionService _sessionService;

    public SessionManager(IConfiguration configuration)
    {
        _configuration = configuration;
    }

    public Session CreateSession(string hostConnectionId)
    {
        var sessionId = GenerateSessionId();
        var session = new Session
        {
            SessionId = sessionId,
            HostConnectionId = hostConnectionId,
            CreatedAt = DateTime.UtcNow,
            LastActivity = DateTime.UtcNow,
            Status = SessionStatus.Active
        };

        _sessions.TryAdd(sessionId, session);

        // TODO: DB保存
        // await _sessionService.CreateSessionAsync(hostConnectionId);

        return session;
    }

    public Session? GetSession(string sessionId)
    {
        _sessions.TryGetValue(sessionId, out var session);
        return session;
    }

    public bool UpdateSession(Session session)
    {
        session.LastActivity = DateTime.UtcNow;
        _sessions[session.SessionId] = session;

        // TODO: DB更新
        // await _sessionService.UpdateSessionAsync(session);

        return true;
    }

    public bool CloseSession(string sessionId)
    {
        if (_sessions.TryRemove(sessionId, out var session))
        {
            session.Status = SessionStatus.Closed;

            // TODO: DB更新
            // await _sessionService.UpdateSessionAsync(session);

            return true;
        }
        return false;
    }

    public void AddGuest(string sessionId, Guest guest)
    {
        if (_sessions.TryGetValue(sessionId, out var session))
        {
            session.Guests.Add(guest);
            session.LastActivity = DateTime.UtcNow;

            // TODO: DB保存
            // await _guestService.AddGuestAsync(sessionId, guest);
        }
    }

    public void RemoveGuest(string sessionId, string userId)
    {
        if (_sessions.TryGetValue(sessionId, out var session))
        {
            session.Guests.RemoveAll(g => g.UserId == userId);
            session.LastActivity = DateTime.UtcNow;

            // TODO: DB削除
            // await _guestService.RemoveGuestAsync(sessionId, userId);
        }
    }

    public void AddTrackToPlaylist(string sessionId, PlaylistItem item)
    {
        if (_sessions.TryGetValue(sessionId, out var session))
        {
            item.Order = session.Playlist.Count;
            session.Playlist.Add(item);
            session.LastActivity = DateTime.UtcNow;

            // TODO: DB保存
            // await _playlistService.AddTrackAsync(sessionId, item);
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

            // TODO: DB削除
            // await _playlistService.RemoveTrackAsync(sessionId, itemId);

            return true;
        }
        return false;
    }

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

            // TODO: DB更新
            // await _playlistService.ReorderPlaylistAsync(sessionId, orderedItemIds);

            return true;
        }
        return false;
    }

    private void ReorderPlaylist(Session session)
    {
        for (int i = 0; i < session.Playlist.Count; i++)
        {
            session.Playlist[i].Order = i;
        }
    }

    public void CheckInactiveSessions()
    {
        var timeoutMinutes = _configuration.GetValue<int>("SessionSettings:InactivityTimeoutMinutes", 30);
        var cutoffTime = DateTime.UtcNow.AddMinutes(-timeoutMinutes);

        foreach (var session in _sessions.Values.Where(s => s.Status == SessionStatus.Active))
        {
            if (session.LastActivity < cutoffTime)
            {
                session.Status = SessionStatus.Inactive;
                // TODO: DB更新
                // await _sessionService.UpdateSessionAsync(session);
            }
        }
    }


    private string GenerateSessionId()
    {
        return Guid.NewGuid().ToString("N").Substring(0, 8).ToUpper();
    }
}