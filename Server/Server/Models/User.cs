//using System.ComponentModel.DataAnnotations;
//using System.ComponentModel.DataAnnotations.Schema;

namespace Server.Models
{
    //[Table("Users")]
    public class Users
    {
        //[Key]
        public int Id { get; set; }

        //[Required]
        //[MaxLength(100)]
        public required string Name { get; set; }
    }
}