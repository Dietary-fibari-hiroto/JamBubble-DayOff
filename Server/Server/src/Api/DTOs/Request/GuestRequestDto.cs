using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class RegisterGuestRequestDto
    {
        [Required]
        public required int SessionId { get; set; }
        public int? UserId { get; set; }
        public string? Name { get; set; }
        public int? Authority { get; set; }

        // Guestの型に変換
        public Guest RequestToGuest()
        {
            Guest guest = new();
            guest.SessionId = this.SessionId;
            if(this.UserId != null)
            {
                guest.UserId = this.UserId;
            }
            if(!string.IsNullOrEmpty(this.Name))
            {
                guest.Name = this.Name;
            }
            if(this.Authority != null)
            {
                guest.Authority = (int)this.Authority;
            }
            else
            {
                guest.Authority = 1;
            }
            return guest;
        }
    }
}
