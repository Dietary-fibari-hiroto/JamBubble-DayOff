using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class Message
{
    public int Id { get; set; }

    public int UserId { get; set; }

    public string Title { get; set; } = null!;

    public string Content { get; set; } = null!;

    public sbyte IsRead { get; set; }

    public DateTime CreatedAt { get; set; }

    public virtual User User { get; set; } = null!;
}
