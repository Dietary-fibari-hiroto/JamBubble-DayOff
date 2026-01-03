using System.ComponentModel.DataAnnotations.Schema;
namespace Server.src.Entities;

public abstract class TimestampedEntity
{
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
}
