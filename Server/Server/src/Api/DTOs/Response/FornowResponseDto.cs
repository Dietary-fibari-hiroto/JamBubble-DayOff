using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{ 
    public class FornowSimpResponseDto
    {
        public int Id { get; set; }
        public int UserId { get; set; }

        public FornowSimpResponseDto(Fornow fornow)
        {
            Id = fornow.Id;
            UserId = fornow.UserId;
        }
    }

    public class FornowDetailResponseDto
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string Name { get; set; } = string.Empty;
        public string ImgUrl { get; set; } = string.Empty;
        public string MusicId { get; set; } = string.Empty;
        public string? Message { get; set; }
        public DateTime CreatedAt { get; set; }
        public int LikeCount { get; set; }
        public bool IsLikedByUser { get; set; }

        public FornowDetailResponseDto(Fornow fornow, int likes, bool isLiked)
        {
            Id = fornow.Id;
            UserId = fornow.UserId;
            Name = fornow.User?.Name ?? string.Empty;
            ImgUrl = fornow.User?.ImgUrl ?? string.Empty;
            MusicId = fornow.MusicId;
            Message = fornow.Message;
            CreatedAt = fornow.CreatedAt;
            LikeCount = likes;
            IsLikedByUser = isLiked;
        }
    }

    // SwaggerUI上でのExample Valueの設定
    public class FornowResponseFilter : ISchemaFilter
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
            if (context.Type == typeof(FornowSimpResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(FornowSimpResponseDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(FornowSimpResponseDto.UserId))] = new OpenApiInteger(1)
                };
            }

            if (context.Type == typeof(FornowDetailResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(FornowDetailResponseDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(FornowDetailResponseDto.UserId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(FornowDetailResponseDto.Name))] = new OpenApiString("User Name"),
                    [ToCamelCase(nameof(FornowDetailResponseDto.ImgUrl))] = new OpenApiString("https://example.com/image.jpg"),
                    [ToCamelCase(nameof(FornowDetailResponseDto.MusicId))] = new OpenApiString("music123"),
                    [ToCamelCase(nameof(FornowDetailResponseDto.Message))] = new OpenApiString("This is a sample message."),
                    [ToCamelCase(nameof(FornowDetailResponseDto.CreatedAt))] = new OpenApiString(DateTime.UtcNow.ToString("o")),
                    [ToCamelCase(nameof(FornowDetailResponseDto.LikeCount))] = new OpenApiInteger(10),
                    [ToCamelCase(nameof(FornowDetailResponseDto.IsLikedByUser))] = new OpenApiBoolean(true)

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
