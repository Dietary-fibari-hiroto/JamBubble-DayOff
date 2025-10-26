using System;
using System.Collections.Generic;

namespace Server.Models;

public partial class StreetPassHistory
{
    public int Id { get; set; }

    public int PassedUser1Id { get; set; }

    public int PassedUser2Id { get; set; }

    public double Latitude { get; set; }

    public double Longitude { get; set; }

    public DateTime CreatedAt { get; set; }

    public virtual User PassedUser1 { get; set; } = null!;

    public virtual User PassedUser2 { get; set; } = null!;
}
