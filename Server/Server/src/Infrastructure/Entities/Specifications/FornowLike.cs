using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Server.src.Entities
{
    [Index(nameof(FornowId),nameof(UserId),IsUnique=true)]
    public class FornowLike
    {
        [Required]
        public int FornowId { get; set; }
        [ForeignKey(nameof(FornowId))]
        public Fornow? Fornow { get; set; }

        [Required]
        public int UserId { get; set; }
        [ForeignKey(nameof(UserId))]
        public User? User { get; set; }

    }
}
