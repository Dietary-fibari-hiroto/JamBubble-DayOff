using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class Provider
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<Session> Sessions { get; set; } = new List<Session>();

    public virtual ICollection<UserProvider> UserProviders { get; set; } = new List<UserProvider>();
}
