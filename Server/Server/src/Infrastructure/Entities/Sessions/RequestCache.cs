using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Server.src.Entities
{
    public class RequestCache
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Key]
        public int Id { get; set; }

        [Required]
        public int SessionId { get; set; }
        [ForeignKey(nameof(SessionId))]
        public Session? Session { get; set; }


        [Required]
        public int GuestId { get; set; }
        [ForeignKey(nameof(GuestId))]
        public Guest? Guest { get; set; }

        [Required]
        [StringLength(50)]
        public string MusicId { get; set; } = null!;

        [Required]
        public int OrderIndex { get; set; }
    }
}
