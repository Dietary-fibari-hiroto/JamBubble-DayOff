using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class FriendRequest
{
    public int SendUserId { get; set; }

    public int PassUserId { get; set; }

    public string State { get; set; } = null!;

    public DateTime CreatedAt { get; set; }

    public DateTime UpdatedAt { get; set; }

    public virtual User PassUser { get; set; } = null!;

    public virtual User SendUser { get; set; } = null!;
}
