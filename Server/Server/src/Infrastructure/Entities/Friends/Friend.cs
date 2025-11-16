using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
namespace Server.src.Entities
{
    [Index(nameof(User1Id),nameof(User2Id),IsUnique=true)]
    public class Friend
    {
        [Required]
        public int User1Id { get; set; }
        [ForeignKey(nameof(User1Id))]
        public User? User1 { get; set; }

        [Required]
        public int User2Id { get; set; }
        [ForeignKey(nameof(User2Id))]
        public User? User2 { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
