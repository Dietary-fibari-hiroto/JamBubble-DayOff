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
        public User? User { get; set; } = null; 

        [Required]
        public int ProviderId { get; set; }

        [Required]
        [StringLength(255)]
        public string Name { get; set; } = null!;

        [Required]
        [StringLength(255)]
        public string Password { get; set; } = null!;

    }
}
