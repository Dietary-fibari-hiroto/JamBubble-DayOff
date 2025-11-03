using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;


namespace Server.src.DTOs
{
    public class LoginRequestDto
    {
        [Required]
        public string Email { get; set; } = string.Empty;
        [Required]
        public string Password { get; set; } = string.Empty;
    }

    public class RegisterUserRequestDto
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

    public class RequestFilter : ISchemaFilter
    {
        void ISchemaFilter.Apply(OpenApiSchema schema, SchemaFilterContext context)
        {
            if (context.Type == typeof(User))
            {
                schema.Example = new OpenApiObject
                {
                    ["name"] = new OpenApiString("test"),
                    ["birthday"] = new OpenApiString("2025-11-01"),
                    ["email"] = new OpenApiString("test@test.com"),
                    ["password"] = new OpenApiString("password"),
                    ["gender"] = new OpenApiInteger(0),
                    ["isStreetPass"] = new OpenApiBoolean(false),
                    ["imgUrl"] = new OpenApiString("")
                };
            }

            if (context.Type == typeof(LoginRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    ["Email"] = new OpenApiString("test@test.com"),
                    ["Password"] = new OpenApiString("password")
                };
            }
        }
    }
}
