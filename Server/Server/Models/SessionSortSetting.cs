using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class SessionSortSetting
{
    public int Id { get; set; }

    public string Label { get; set; } = null!;

    public string Description { get; set; } = null!;

    public virtual ICollection<Session> Sessions { get; set; } = new List<Session>();
}
