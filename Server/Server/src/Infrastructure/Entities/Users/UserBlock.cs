using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
namespace Server.src.Entities
{
    [Index(nameof(UserId),nameof(BlockedUserId),IsUnique=true)]
    public class UserBlock
    {
        [Required]
        public int UserId { get; set; }
        [ForeignKey(nameof(UserId))]
        public required User User { get; set; }

        [Required]
        public int BlockedUserId { get; set; }
        [ForeignKey(nameof(BlockedUserId))]
        public required User BlockedUser { get; set; }

        [Required]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
