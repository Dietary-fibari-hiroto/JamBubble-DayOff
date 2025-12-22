using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Server.src.Entities
{
    public class FriendRequest:TimestampedEntity
    {
        [Required]
        public int User1Id { get; set; }
        [ForeignKey(nameof(User1Id))]
        public User? User1 { get; set; }

        [Required]
        public int User2Id { get; set; }
        [ForeignKey(nameof(User2Id))]
        public User? User2 { get; set; }

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

        public void SetIds()
        {
            if (PassUserId < SendUserId)
            {
                User1Id = PassUserId;
                User2Id = SendUserId;
            }
            else
            {
                User1Id = SendUserId;
                User2Id = PassUserId;
            }
        }
    }
}
