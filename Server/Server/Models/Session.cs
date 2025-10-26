using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class Session
{
    public int Id { get; set; }

    public string Title { get; set; } = null!;

    /// <summary>
    /// Hashing
    /// </summary>
    public string Password { get; set; } = null!;

    public int UserId { get; set; }

    public int ProviderId { get; set; }

    public int SceneId { get; set; }

    public int DefaultSortId { get; set; }

    public string? Description { get; set; }

    public string ImgUrl { get; set; } = null!;

    public sbyte Finished { get; set; }

    public DateTime? FinishedAt { get; set; }

    public DateTime CreatedAt { get; set; }

    public virtual SessionSortSetting DefaultSort { get; set; } = null!;

    public virtual ICollection<Guest> Guests { get; set; } = new List<Guest>();

    public virtual Provider Provider { get; set; } = null!;

    public virtual ICollection<RequestCache> RequestCaches { get; set; } = new List<RequestCache>();

    public virtual ICollection<Request> Requests { get; set; } = new List<Request>();

    public virtual Scene Scene { get; set; } = null!;

    public virtual User User { get; set; } = null!;

    public virtual ICollection<Tag> Tags { get; set; } = new List<Tag>();
}
