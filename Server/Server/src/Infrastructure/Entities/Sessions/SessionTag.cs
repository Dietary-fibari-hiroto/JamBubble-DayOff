using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
namespace Server.src.Entities
{
    [Index(nameof(SessionId),nameof(TagId),IsUnique=true)]
    public class SessionTag
    {
        [Required]
        public int SessionId { get; set; }
        [ForeignKey(nameof(SessionId))]
        public Session? Session { get; set; }

        [Required]
        public int TagId { get; set; }
        [ForeignKey(nameof(TagId))]
        public Tag? Tag { get; set; }
    }
}
