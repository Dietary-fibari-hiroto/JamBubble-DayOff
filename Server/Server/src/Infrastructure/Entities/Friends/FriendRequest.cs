using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Server.src.Entities
{
    public class FriendRequest:TimestampedEntity
    {
        [Required]
        public int SendUserId { get; set; }
        [ForeignKey(nameof(SendUserId))]
        public User? SendUser { get; set; }

        [Required]
        public int PassUserId { get; set; }
        [ForeignKey(nameof(PassUserId))]
        public User? PassUser { get; set; }

        [Required]
        public int State { get; set; } = 0;
        
    }
}
