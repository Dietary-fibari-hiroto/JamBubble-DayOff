using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class Tag
{
    public int Id { get; set; }

    public string Label { get; set; } = null!;

    public virtual ICollection<Session> Sessions { get; set; } = new List<Session>();
}
