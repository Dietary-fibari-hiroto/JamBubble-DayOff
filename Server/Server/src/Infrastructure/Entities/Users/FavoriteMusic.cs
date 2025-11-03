using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Server.src.Entities
{
    [Index(nameof(UserId),IsUnique=true)]
    public class FavoriteMusic
    {
        [Key]
        [Required]
        public int UserId { get; set; }
        [ForeignKey(nameof(UserId))]
        public required User User { get; set; }

        [Required]
        [StringLength(50)]
        public string MusicId { get; set; }= null!;
    }
}
