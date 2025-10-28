using System.ComponentModel.DataAnnotations;


namespace Server.src.Entities
{
    public class FavoriteMusicSummary
    {
        [Key]
        [StringLength(50)]
        public string MusicId { get; set; } = null!;

        [Required]
        public int Count { get; set; } = 0;

    }
}
