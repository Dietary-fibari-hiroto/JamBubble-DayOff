using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Server.src.Infrastructure.Entities.Users
{
    public class Provider
    {
        [Key]
        public int Id { get; set; }

        [StringLength(50)]
        public string Name { get; set; } = null!;

    }
}
