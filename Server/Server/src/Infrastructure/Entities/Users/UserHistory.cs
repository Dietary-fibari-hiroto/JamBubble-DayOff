using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Server.src.Entities
{
    [Index(nameof(UserId),IsUnique=true)]
    public class UserHistory
    {
        [Key]
        [Required]
        public int UserId { get; set; }
        [ForeignKey(nameof(UserId))]
        public User? User { get; set; }

        [Required]
        public int SessionCount { get; set; } = 0;
    }
}
