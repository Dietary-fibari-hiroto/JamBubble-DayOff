using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Server.src.Entities
{
    public class SessionSortSetting
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Key]
        public int Id { get; set; }

        [Required]
        [StringLength(50)]
        public String Label { get; set; } = null!;

        [Required]
        public String Description { get; set; } = null!;

    }
}
