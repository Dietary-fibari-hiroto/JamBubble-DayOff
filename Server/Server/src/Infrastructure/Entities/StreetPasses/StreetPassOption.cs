using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Server.src.Entities
{
    public class StreetPassOption
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Key]
        public int UserId { get; set; }
        [ForeignKey(nameof(UserId))]
        public required User User { get; set; }

        [StringLength(255)]
        public string? PlaylistEndpoint { get; set; }

        public string? Message { get; set; }

        [Required]
        public bool SecretMode { get; set; } = true;
    }
}
