using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;


namespace Server.src.Entities
{
    public class StreetPassHistory
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Key]
        public int Id { get; set; }

        [Required]
        public int PassedUser1Id { get; set; }
        [ForeignKey(nameof(PassedUser1Id))]
        public required User PassedUser1 { get; set; }

        [Required]
        public int PassedUser2Id { get; set; }
        [ForeignKey(nameof(PassedUser2Id))]
        public required User PassedUser2 { get; set; }

        [Required]
        public double Latitude { get; set; }
        [Required]
        public double Longitude { get; set; }

        [Required]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
