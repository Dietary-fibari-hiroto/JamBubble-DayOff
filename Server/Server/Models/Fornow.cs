using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class Fornow
{
    public int Id { get; set; }

    public int UserId { get; set; }

    public string MusicId { get; set; } = null!;

    public string? Message { get; set; }

    public sbyte Finished { get; set; }

    public DateTime CreatedAt { get; set; }

    public virtual User User { get; set; } = null!;

    public virtual ICollection<User> Users { get; set; } = new List<User>();
}
