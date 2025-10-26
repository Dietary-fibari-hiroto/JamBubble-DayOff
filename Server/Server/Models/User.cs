using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class User
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public int Age { get; set; }

    public string Email { get; set; } = null!;

    /// <summary>
    /// Hashing
    /// </summary>
    public string Password { get; set; } = null!;

    public string Gender { get; set; } = null!;

    public sbyte IsStreetPass { get; set; }

    public string ImgUrl { get; set; } = null!;

    public string? FavoriteMusic { get; set; }

    public DateTime CreatedAt { get; set; }

    public virtual ICollection<Fornow> Fornows { get; set; } = new List<Fornow>();

    public virtual ICollection<FriendRequest> FriendRequestPassUsers { get; set; } = new List<FriendRequest>();

    public virtual ICollection<FriendRequest> FriendRequestSendUsers { get; set; } = new List<FriendRequest>();

    public virtual ICollection<Friend> FriendUser1s { get; set; } = new List<Friend>();

    public virtual ICollection<Friend> FriendUser2s { get; set; } = new List<Friend>();

    public virtual ICollection<Guest> Guests { get; set; } = new List<Guest>();

    public virtual ICollection<Message> Messages { get; set; } = new List<Message>();

    public virtual ICollection<Session> Sessions { get; set; } = new List<Session>();

    public virtual ICollection<StreetPassHistory> StreetPassHistoryPassedUser1s { get; set; } = new List<StreetPassHistory>();

    public virtual ICollection<StreetPassHistory> StreetPassHistoryPassedUser2s { get; set; } = new List<StreetPassHistory>();

    public virtual StreetPassOption? StreetPassOption { get; set; }

    public virtual ICollection<UserProvider> UserProviders { get; set; } = new List<UserProvider>();

    public virtual ICollection<Fornow> FornowsNavigation { get; set; } = new List<Fornow>();
}
