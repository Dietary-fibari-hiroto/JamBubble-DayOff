using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace Server.src.DTOs
{
    public class TokenResponseDto
    {
        public string Token { get; set; } = string.Empty;
    }

    public class AuthResponseFilter : ISchemaFilter
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
            if (context.Type == typeof(TokenResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(TokenResponseDto.Token))] = new OpenApiString("sample.jwt.token")
                };
            }
        }
    }
}