using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class UserProvider
{
    public int UserId { get; set; }

    public int ProviderId { get; set; }

    public string Name { get; set; } = null!;

    /// <summary>
    /// Hashing
    /// </summary>
    public string Password { get; set; } = null!;

    public virtual Provider Provider { get; set; } = null!;

    public virtual User User { get; set; } = null!;
}
