using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class MessageResponseDto
    {
        public int Id { get; set; }
        public string Title { get; set; } = null!;
        public string Content { get; set; } = null!;
        public bool IsRead { get; set; } = false;
        public DateTime CreatedAt { get; set; }

        public MessageResponseDto(Message message)
        {
            this.Id = message.Id;
            this.Title = message.Title;
            this.Content = message.Content;
            this.IsRead = message.IsRead;
            this.CreatedAt = message.CreatedAt;
        }
    }

    public class MessageNoContentResponseDto
    {
        public int Id { get; set; }
        public string Title { get; set; } = null!;
        public bool IsRead { get; set; } = false;
        public DateTime CreatedAt { get; set; }

        public MessageNoContentResponseDto(Message message)
        {
            this.Id = message.Id;
            this.Title = message.Title;
            this.IsRead = message.IsRead;
            this.CreatedAt = message.CreatedAt;
        }
    }

    // SwaggerUI上でのExample Valueの設定
    public class MessageResponseFilter : ISchemaFilter
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
            if (context.Type == typeof(MessageResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(MessageResponseDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(MessageResponseDto.Title))] = new OpenApiString("Title"),
                    [ToCamelCase(nameof(MessageResponseDto.Content))] = new OpenApiString("Content"),
                    [ToCamelCase(nameof(MessageResponseDto.IsRead))] = new OpenApiBoolean(false),
                    [ToCamelCase(nameof(MessageResponseDto.CreatedAt))] = new OpenApiDateTime(DateTime.UtcNow),
                };
            }

            if (context.Type == typeof(MessageNoContentResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(MessageNoContentResponseDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(MessageNoContentResponseDto.Title))] = new OpenApiString("Title"),
                    [ToCamelCase(nameof(MessageNoContentResponseDto.IsRead))] = new OpenApiBoolean(false),
                    [ToCamelCase(nameof(MessageNoContentResponseDto.CreatedAt))] = new OpenApiDateTime(DateTime.UtcNow),
                };
            }

            //if (context.Type == typeof())
            //{
            //    schema.Example = new OpenApiObject
            //    {
            //        [ToCamelCase(nameof(UserProviderResponseDto.ProviderId))] = new OpenApiInteger(1),
            //    };
            //}

            //if (context.Type == typeof())
            //{
            //    schema.Example = new OpenApiObject
            //    {
            //        [ToCamelCase(nameof(UserProviderResponseDto.ProviderId))] = new OpenApiInteger(1),
            //    };
            //}

        }
    }
}
