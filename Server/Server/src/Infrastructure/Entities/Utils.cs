using System.ComponentModel.DataAnnotations.Schema;
namespace Server.src.Entities;
public abstract class TimestampedEntity
    {
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow; 

    }

