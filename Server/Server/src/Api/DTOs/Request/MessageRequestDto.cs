using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    //public class MessageRequestDto
    //{
    //}

    public class DeleteMessageRequestDto
    {
        [Required]
        public required int Id { get; set; }
    }

    public class MessageRequestFilter : ISchemaFilter
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
            if (context.Type == typeof(DeleteMessageRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(DeleteMessageRequestDto.Id))] = new OpenApiInteger(1)
                };
            }

            //if (context.Type == typeof())
            //{
            //    schema.Example = new OpenApiObject
            //    {
            //        [ToCamelCase(nameof(RegisterUserRequestDto.ImgUrl))] = new OpenApiString(""),
            //    };
            //}

            //if (context.Type == typeof())
            //{
            //    schema.Example = new OpenApiObject
            //    {
            //          
            //    };
            //}
        }
    }
}
