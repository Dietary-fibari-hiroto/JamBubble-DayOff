using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;


namespace Server.src.DTOs
{
    public class RegisterRequestCacheRequestDto
    {
        public required int SessionId { get; set; }
        public required int GuestId { get; set; }
        public required string MusicId { get; set; }
        public required int OrderIndex { get; set; }

        public RequestCache RequestToRequetCache()
        {
            RequestCache rc = new();
            rc.SessionId = this.SessionId;
            rc.GuestId = this.GuestId;
            rc.MusicId = this.MusicId;
            rc.OrderIndex = this.OrderIndex;
            return rc;
        }
    }
}