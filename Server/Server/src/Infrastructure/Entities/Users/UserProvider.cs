using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Server.src.Entities
{
    [Index(nameof(UserId),nameof(ProviderId), IsUnique = true)]
    public class UserProvider
    {
        [Required]
        public int UserId { get; set; }
        public required User User { get; set; }

        [Required]
        public int ProviderId { get; set; }
        public required Provider Provider { get; set; }

        [Required]
        [StringLength(255)]
        public string Name { get; set; } = null!;

        [Required]
        [StringLength(255)]
        public string Password { get; set; } = null!;

    }
}
