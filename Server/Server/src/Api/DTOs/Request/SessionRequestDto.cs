using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;


namespace Server.src.DTOs
{
    public class SessionRequestDto
    {
        [Required]
        [StringLength(50)]
        public required string Title { get; set; }
        [Required]
        public required int ProviderId { get; set; }
        [Required]
        public required string Password { get; set; }
        [Required]
        public required int SceneId { get; set; }
        [Required]
        public int DefaultSortId { get; set; }
        [Required]
        public required string ImgUrl { get; set; }
        public string? Description { get; set; } = null;
        [Required]
        public required bool IsPublic { get; set; }
        [Required]
        public int UserCapacity { get; set; } = 10;
        public required List<SessionTagRequestDto> SessionTags { get; set; }

        public Session ToSessionEntity(int userId)
        {
            return new Session
            {
                UserId = userId,
                Title = this.Title,
                ProviderId = this.ProviderId,
                Password = this.Password,
                SceneId = this.SceneId,
                DefaultSortId = this.DefaultSortId,
                ImgUrl = this.ImgUrl,
                Description = this.Description,
                IsPublic = this.IsPublic,
                UserCapacity = this.UserCapacity,
            };
        }

        public List<SessionTag> ToSessionTagsEntities(int SessionId)
        {
            return this.SessionTags
                .Select(tagDto => tagDto.ToSessionTagEntity(SessionId))
                .ToList();
        }
    }

    public class SessionTagRequestDto
    {
        [Required]
        public required int Id { get; set; }
        public SessionTag ToSessionTagEntity(int sessionId)
        {
            return new SessionTag
            {
                SessionId = sessionId,
                TagId = this.Id,
            };
        }
    }

    public class SessionRequestFilter : ISchemaFilter
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
            if (context.Type == typeof(SessionRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(SessionRequestDto.Title))] = new OpenApiString("Sample Session"),
                    [ToCamelCase(nameof(SessionRequestDto.ProviderId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(SessionRequestDto.Password))] = new OpenApiString("Password"),
                    [ToCamelCase(nameof(SessionRequestDto.SceneId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(SessionRequestDto.DefaultSortId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(SessionRequestDto.ImgUrl))] = new OpenApiString("https://example.com/image.png"),
                    [ToCamelCase(nameof(SessionRequestDto.Description))] = new OpenApiString("This is a sample session description."),
                    [ToCamelCase(nameof(SessionRequestDto.IsPublic))] = new OpenApiBoolean(true),
                    [ToCamelCase(nameof(SessionRequestDto.UserCapacity))] = new OpenApiInteger(10),
                    [ToCamelCase(nameof(SessionRequestDto.SessionTags))] = new OpenApiArray
                    {
                        new OpenApiObject
                        {
                            [ToCamelCase(nameof(SessionTagRequestDto.Id))] = new OpenApiInteger(1)
                        },
                        new OpenApiObject
                        {
                            [ToCamelCase(nameof(SessionTagRequestDto.Id))] = new OpenApiInteger(2)
                        }
                    }
                };
            }

            if (context.Type == typeof(SessionTagRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(SessionTagRequestDto.Id))] = new OpenApiInteger(1),
                };
            }

            //if (context.Type == typeof())
            //{
            //    schema.Example = new OpenApiObject
            //    {
            //          
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
