using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class Guest
{
    public int Id { get; set; }

    public string? Name { get; set; }

    public int? UserId { get; set; }

    public int SessionId { get; set; }

    public string Authority { get; set; } = null!;

    public virtual ICollection<RequestCache> RequestCaches { get; set; } = new List<RequestCache>();

    public virtual ICollection<Request> Requests { get; set; } = new List<Request>();

    public virtual Session Session { get; set; } = null!;

    public virtual User? User { get; set; }
}
