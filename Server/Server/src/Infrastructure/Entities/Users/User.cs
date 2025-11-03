using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;
namespace Server.src.Entities;
public class User:TimestampedEntity {
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    [JsonIgnore]
    [Key]
    public int Id { get; set; }

    [Required]
    [StringLength(100)]
    public string Name { get; set; } = null!;

    [Required]
    public DateOnly Birthday { get; set; }

    [Required]
    [StringLength(255)]
    public string Email { get; set; } = null!;

    [Required]
    [StringLength(255)]
    public string Password { get; set; } = null!;

    [Required]
    public int Gender { get; set; } = 0;

    // TODO:ここのRequiredの必要性
    [Column("is_street_pass")]
    [Required]
    public bool IsStreetPass { get; set; } = false;

    [Column("img_url")]
    [StringLength(255)]
    public string? ImgUrl { get; set; }

    public string? Message { get; set; }

    public UserHistory? UserHistory { get; set; }
    public FavoriteMusic? FavoriteMusic { get; set; }
    public List<UserProvider>? UserProviders { get; set; }
}

