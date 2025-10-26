using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class StreetPassOption
{
    public int UserId { get; set; }

    public string? PlaylistEndpoint { get; set; }

    public string? Message { get; set; }

    public sbyte SecretMode { get; set; }

    public virtual User User { get; set; } = null!;
}
