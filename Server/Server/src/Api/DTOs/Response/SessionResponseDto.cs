using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class SessionResponseDto
    {
        public int Id { get; set; }
        public string Title { get; set; } = string.Empty;
        public string ImgUrl { get; set; } = string.Empty;
        public string? Description { get; set; } = string.Empty;
        public DateTime CreatedAt { get; set; }

        public SessionResponseDto(Session session)
        {
            Id = session.Id;
            Title = session.Title;
            ImgUrl = session.ImgUrl;
            Description = session.Description;
            CreatedAt = session.CreatedAt;
        }
    }

    public class SessionTagDto
    {
        public int Id { get; set; }
        public string Label { get; set; } = string.Empty;
        public SessionTagDto(SessionTag sTag)
        {
            if(sTag.Tag != null)
            {
                Id = sTag.Tag.Id;
                Label = sTag.Tag.Label;
            }
        }
    }

    public class RequestsDto
    {
        public int Id {get; set; }
        public int GuestId {get; set; }
        public string MusicId {get; set; }
        public RequestsDto(Request request)
        {
            Id = request.Id;
            GuestId = request.GuestId;
            MusicId = request.MusicId;
        }
    }

    public class SessionDetailResponseDto
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string Title { get; set; } = string.Empty;
        public string ImgUrl { get; set; } = string.Empty;
        public string? Description { get; set; } = string.Empty;
        public int ProviderId { get; set; }
        public string ProviderName { get; set; } = string.Empty;
        public int SceneId { get; set; }
        public string SceneName { get; set; } = string.Empty;
        public int DefaultSortId { get; set; }
        public bool Finished { get; set; }
        public bool IsPublic { get; set; }
        public int UserCapacity { get; set; }
        public List<SessionTagDto>? SessionTag { get; set; }
        public DateTime CreatedAt { get; set; }
        public DateTime? FinishedAt { get; set; }
        public List<RequestsDto>? requests { get; set; } 

        public SessionDetailResponseDto(Session session)
        {
            Id = session.Id;
            UserId = session.UserId;
            Title = session.Title;
            ImgUrl = session.ImgUrl;
            Description = session.Description;
            ProviderId = session.ProviderId;
            if (session.Provider != null)
            {
                ProviderName = session.Provider.Name;
            }
            SceneId = session.SceneId;
            if (session.Scene != null)
            {
                SceneName = session.Scene.Name;
            }
            DefaultSortId = session.DefaultSortId;
            Finished = session.Finished;
            IsPublic = session.IsPublic;
            UserCapacity = session.UserCapacity;
            if (session.SessionTag != null)
            {
                SessionTag = session.SessionTag.Select(s => new SessionTagDto(s)).ToList();
            }
            CreatedAt = session.CreatedAt;
            FinishedAt = session.FinishedAt;

            if (session.Requests != null)
            {
                requests = session.Requests.OrderBy(r => r.Id).Select(r => new RequestsDto(r)).ToList();
            }

        }
    }

    public class SessionPopularDto
    {
        public Session Session { get; set; } = null!;
        public int GuestCount { get; set; }
    }

    public class SessionPopularResponseDto
    {
        public int Id { get; set; }
        public string Title { get; set; } = string.Empty;
        public string ImgUrl { get; set; } = string.Empty;
        public int UserCount { get; set; }

        public SessionPopularResponseDto(Session session, int userCount)
        {
            Id = session.Id;
            Title = session.Title;
            ImgUrl = session.ImgUrl;
            UserCount = userCount;
        }
    }

    public class SessionResponseFilter : ISchemaFilter
    {
        private string ToCamelCase(string str)
        {
            if (string.IsNullOrEmpty(str) || char.IsLower(str[0]))
            {
                return str;
            }
            return char.ToLowerInvariant(str[0]) + str.Substring(1);
        }

        public void Apply(OpenApiSchema schema, SchemaFilterContext context)
        {
            if (context.Type == typeof(SessionResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(SessionResponseDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(SessionResponseDto.Title))] = new OpenApiString("Sample Session Title"),
                    [ToCamelCase(nameof(SessionResponseDto.ImgUrl))] = new OpenApiString("https://example.com/image.png"),
                    [ToCamelCase(nameof(SessionResponseDto.Description))] = new OpenApiString("This is a sample session description."),
                    [ToCamelCase(nameof(SessionResponseDto.CreatedAt))] = new OpenApiString(DateTime.UtcNow.ToString("o")),
                };
            }

            if (context.Type == typeof(SessionDetailResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(SessionDetailResponseDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(SessionDetailResponseDto.UserId))] = new OpenApiInteger(42),
                    [ToCamelCase(nameof(SessionDetailResponseDto.Title))] = new OpenApiString("Detailed Session Title"),
                    [ToCamelCase(nameof(SessionDetailResponseDto.ImgUrl))] = new OpenApiString("https://example.com/detailed_image.png"),
                    [ToCamelCase(nameof(SessionDetailResponseDto.Description))] = new OpenApiString("This is a detailed session description."),
                    [ToCamelCase(nameof(SessionDetailResponseDto.ProviderId))] = new OpenApiInteger(2),
                    [ToCamelCase(nameof(SessionDetailResponseDto.ProviderName))] = new OpenApiString("Provider Name"),
                    [ToCamelCase(nameof(SessionDetailResponseDto.SceneId))] = new OpenApiInteger(3),
                    [ToCamelCase(nameof(SessionDetailResponseDto.SceneName))] = new OpenApiString("Scene Name"),
                    [ToCamelCase(nameof(SessionDetailResponseDto.DefaultSortId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(SessionDetailResponseDto.Finished))] = new OpenApiBoolean(false),
                    [ToCamelCase(nameof(SessionDetailResponseDto.IsPublic))] = new OpenApiBoolean(true),
                    [ToCamelCase(nameof(SessionDetailResponseDto.UserCapacity))] = new OpenApiInteger(10),
                    [ToCamelCase(nameof(SessionDetailResponseDto.CreatedAt))] = new OpenApiString(DateTime.UtcNow.ToString("o")),
                    [ToCamelCase(nameof(SessionDetailResponseDto.FinishedAt))] = new OpenApiNull(),
                    [ToCamelCase(nameof(SessionDetailResponseDto.SessionTag))] = new OpenApiArray
                    {
                        new OpenApiObject
                        {
                            [ToCamelCase(nameof(SessionTagDto.Id))] = new OpenApiInteger(1),
                            [ToCamelCase(nameof(SessionTagDto.Label))] = new OpenApiString("Sample Tag"),
                        }
                    },
                    [ToCamelCase(nameof(SessionDetailResponseDto.requests))] = new OpenApiArray
                    {
                        new OpenApiObject
                        {
                            [ToCamelCase(nameof(RequestsDto.Id))] = new OpenApiInteger(1),
                            [ToCamelCase(nameof(RequestsDto.GuestId))] = new OpenApiInteger(1),
                            [ToCamelCase(nameof(RequestsDto.MusicId))] = new OpenApiInteger(1),
                        }
                    }
                };
            }

            if (context.Type == typeof(SessionTagDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(SessionTagDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(SessionTagDto.Label))] = new OpenApiString("Sample Tag"),
                };
            }

            if (context.Type == typeof(RequestsDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(RequestsDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(RequestsDto.GuestId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(RequestsDto.MusicId))] = new OpenApiInteger(1),
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