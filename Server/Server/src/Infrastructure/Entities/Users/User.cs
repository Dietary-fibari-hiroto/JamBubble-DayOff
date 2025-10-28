using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
namespace Server.src.Entities;
public class User:TimestampedEntity {
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    [Key]
    public int Id { get; set; }

    [Required]
    [StringLength(100)]
    public string Name { get; set; } = null!;

    [Required]
    public DateTime Birthday { get; set; }

    [Required]
    [StringLength(255)]
    public string Email { get; set; } = null!;

    [Required]
    [StringLength(255)]
    public string Password { get; set; } = null!;

    [Required]
    public int Gender { get; set; } = 0;

    [Column("is_street_pass")]
    [Required]
    public bool IsStreetPass { get; set; } = false;

    [Column("img_url")]
    [StringLength(255)]
    public string? ImgUrl { get; set; }


}

