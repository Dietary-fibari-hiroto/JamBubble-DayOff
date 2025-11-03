using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class TokenResponseDto
    {
        public string Token { get; set; } = string.Empty;
    }

    public class UserResponseDto
    {
        public string Name { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
        public int Gender { get; set; }
        public DateOnly Birthday { get; set; }
        public bool IsStreetPass { get; set; }
        public string ImgUrl { get; set; } = string.Empty;
        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { set; get; }
    }

    public class ResponseFilter : ISchemaFilter
    {
        void ISchemaFilter.Apply(OpenApiSchema schema, SchemaFilterContext context)
        {
            throw new NotImplementedException();
        }
    }
}
