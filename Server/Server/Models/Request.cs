using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class Request
{
    public int Id { get; set; }

    public int SessionId { get; set; }

    public int GuestId { get; set; }

    public string MusicId { get; set; } = null!;

    public virtual Guest Guest { get; set; } = null!;

    public virtual Session Session { get; set; } = null!;
}
