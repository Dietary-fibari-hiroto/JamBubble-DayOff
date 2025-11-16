using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
namespace Server.src.Entities
{
    public class Session
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Key]
        [Required]
        public int Id { get; set; }

        [Required]
        public int UserId { get; set; }
        [ForeignKey(nameof(UserId))]
        public User? User { get; set; }

        [Required]
        [StringLength(50)]
        public string Title { get; set; } = null!;

        [Required]
        public bool Finished { get; set; } = false;

        public DateTime? FinishedAt { get; set; }

        [Required]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [Required]
        public int ProviderId { get; set; }
        [ForeignKey(nameof(ProviderId))]
        public Provider? Provider { get; set; }

        [Required]
        [StringLength(16)]
        public string Password { get; set; } = null!;

        [Required]
        public int SceneId { get; set; }
        [ForeignKey(nameof(SceneId))]
        public Scene? Scene { get; set; }

        [Required]
        public int DefaultSortId { get; set; }
        [ForeignKey(nameof(DefaultSortId))]
        public SessionSortSetting? SessionSortSetting { get; set; }

        [Required]
        [StringLength(255)]
        public string ImgUrl { get; set; } = null!;

        public string? Description { get; set; }

        [Required]
        public bool IsPublic { get; set; } = false;

        [Required]
        public int UserCapacity { get; set; } = 10;

        public List<SessionTag>? SessionTag { get; set; }
    }
}

