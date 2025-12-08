using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class LoginRequestDto
    {
        [Required]
        public required string Email { get; set; }
        [Required]
        public required string Password { get; set; }
    }

    public class SendVerificationEmailRequestDto
    {
        [Required]
        public required string Email { get; set;}
    }

    public class VerifyEmailRequestDto
    {
        [Required]
        public required string Password { get;set;}
    }

    public class AuthRequestFilter : ISchemaFilter
    {

        // キャメルケースに変換するヘルパー
        private string ToCamelCase(string str)
        {
            if (string.IsNullOrEmpty(str) || char.IsLower(str[0]))
            {
                return str;
            }
            return char.ToLowerInvariant(str[0]) + str.Substring(1);
        }

        void ISchemaFilter.Apply(OpenApiSchema schema, SchemaFilterContext context)
        {
            if (context.Type == typeof(LoginRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(LoginRequestDto.Email))] = new OpenApiString("test@test.com"),
                    [ToCamelCase(nameof(LoginRequestDto.Password))] = new OpenApiString("password")
                };
            }

            if (context.Type == typeof(SendVerificationEmailRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(SendVerificationEmailRequestDto.Email))] = new OpenApiString("test@test.com")
                };
            }

            if (context.Type == typeof(VerifyEmailRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(VerifyEmailRequestDto.Password))] = new OpenApiString("password")
                };
            }
        }
    }
}
