using System.ComponentModel.DataAnnotations;


namespace Server.src.DTOs
{
    public class LoginRequest
    {
        [Required]
        public string Email { get; set; } = string.Empty;
        [Required]
        public string Password { get; set; } = string.Empty;
    }

    public class RegisterUserRequest
    {
        [Required]
        public string Name { get; set; } = string.Empty;
        [Required]
        public string Email { get; set; } = string.Empty;
        [Required]
        public string Password { get; set; } = string.Empty;
        [Required]
        public int Gender { get; set; } = 0;
        [Required]
        public DateOnly Birthday { get; set; }
        public bool? IsStreetPass { get; set; } = false;
        public string? ImgUrl { get; set; } = null;
    }
}
