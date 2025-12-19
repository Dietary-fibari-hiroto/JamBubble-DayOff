using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class UserAllDataResponseDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
        public int Gender { get; set; }
        public DateOnly Birthday { get; set; }
        public bool IsStreetPass { get; set; }
        public string? ImgUrl { get; set; } = string.Empty;
        public string? Message { get; set; } = string.Empty;
        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { set; get; }
        public int SessionCount { get; set; }
        public string? MusicId { get; set; } = string.Empty;

        // コンストラクタ
        public UserAllDataResponseDto(User user)
        {
            this.Id = user.Id;
            this.Name = user.Name;
            this.Email = user.Email;
            this.Gender = user.Gender;
            this.Birthday = user.Birthday;
            this.IsStreetPass = user.IsStreetPass;
            this.ImgUrl = user.ImgUrl;
            this.Message = user.Message;
            this.CreatedAt = user.CreatedAt;
            this.UpdatedAt = user.UpdatedAt;
            if (user.UserHistory != null)
            {
                this.SessionCount = user.UserHistory.SessionCount;
            }
            if (user.FavoriteMusic != null)
            {
                this.MusicId = user.FavoriteMusic!.MusicId;
            }
        }
    }

    public class UserProviderResponseDto
    {
        public int ProviderId { get; set; }
        public string Name { get; set; } = string.Empty;
        public UserProviderResponseDto(UserProvider userProvider)
        {
            this.ProviderId = userProvider.ProviderId;
            this.Name = userProvider.Name;
        }
    }


    public class UserProfileResponseDto
    {
        public string Name { get; set; }
        public int Gender { get; set; }
        public string? ImgUrl { get; set; }
        public string? Message { get; set; }
        public int SessionCount { get; set; }
        public string? MusicId { get; set; }

        public UserProfileResponseDto(User user)
        {
            this.Name = user.Name;
            this.Gender = user.Gender;
            this.ImgUrl = user.ImgUrl;
            this.Message = user.Message;
            this.SessionCount = user.UserHistory!.SessionCount;
            this.MusicId = user.FavoriteMusic!.MusicId;
        }
    }



    // SwaggerUI上でのExample Valueの設定
    public class UserResponseFilter : ISchemaFilter
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
            if (context.Type == typeof(UserAllDataResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UserAllDataResponseDto.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(UserAllDataResponseDto.Birthday))] = new OpenApiString("2025-11-01"),
                    [ToCamelCase(nameof(UserAllDataResponseDto.Email))] = new OpenApiString("test@test.com"),
                    [ToCamelCase(nameof(UserAllDataResponseDto.Gender))] = new OpenApiInteger(0),
                    [ToCamelCase(nameof(UserAllDataResponseDto.IsStreetPass))] = new OpenApiBoolean(false),
                    [ToCamelCase(nameof(UserAllDataResponseDto.ImgUrl))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UserAllDataResponseDto.Message))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UserAllDataResponseDto.SessionCount))] = new OpenApiInteger(0),
                    [ToCamelCase(nameof(UserAllDataResponseDto.MusicId))] = new OpenApiString(""),

                };
            }

            if (context.Type == typeof(UserProviderResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UserProviderResponseDto.ProviderId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(UserProviderResponseDto.Name))] = new OpenApiString("test")
                };
            }

            if (context.Type == typeof(UserProfileResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UserProfileResponseDto.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(UserAllDataResponseDto.Gender))] = new OpenApiInteger(0),
                    [ToCamelCase(nameof(UserAllDataResponseDto.ImgUrl))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UserAllDataResponseDto.Message))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UserAllDataResponseDto.SessionCount))] = new OpenApiInteger(0),
                    [ToCamelCase(nameof(UserAllDataResponseDto.MusicId))] = new OpenApiString("Test"),
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
