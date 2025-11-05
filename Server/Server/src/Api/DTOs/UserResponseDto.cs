using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class UserResponseDto
    {
        public string Name { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
        public int Gender { get; set; }
        public DateOnly Birthday { get; set; }
        public bool IsStreetPass { get; set; }
        public string? ImgUrl { get; set; } = string.Empty;
        public string? Message { get; set; } = string.Empty;
        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { set; get; }

        // コンストラクタ
        public UserResponseDto(User user)
        {
            Name = user.Name;
            Email = user.Email;
            Gender = user.Gender;
            Birthday = user.Birthday;
            IsStreetPass = user.IsStreetPass;
            ImgUrl = user.ImgUrl;
            Message = user.Message;
            CreatedAt = user.CreatedAt;
            UpdatedAt = user.UpdatedAt;
        }
    }

    public class UserHistoryResponseDto
    {
        public int SessionCount { get; set; }
        public UserHistoryResponseDto(UserHistory userHistory)
        {
            SessionCount = userHistory.SessionCount;
        }
    }

    public class FavoriteMusicResponseDto
    {
        public string MusicId { get; set; } = string.Empty;
        public FavoriteMusicResponseDto(FavoriteMusic favoriteMusic)
        {
            MusicId = favoriteMusic.MusicId;
        }
    }

    public class UserProviderResponseDto
    {
        public int ProviderId { get; set; }
        public string ProviderName { get; set; }
        public string Name { get; set; } = string.Empty;
        public string Password { get; set; } = string.Empty;
        public UserProviderResponseDto(UserProvider userProvider)
        {
            ProviderId = userProvider.ProviderId;
            ProviderName = userProvider.Provider.Name;
            Name = userProvider.Name;
            Password = userProvider.Password;
        }
    }
    public class UserAllDataResponseDto
    {
        public UserResponseDto User { get; set; }
        public UserHistoryResponseDto? UserHistory { get; set; } = null;
        public FavoriteMusicResponseDto? FavoriteMusic { get; set; } = null;

        public UserAllDataResponseDto(User user)
        {
            User = new UserResponseDto(user);

            if (user.UserHistory != null)
            {
                UserHistory = new UserHistoryResponseDto(user.UserHistory);
            }

            if (user.FavoriteMusic != null)
            {
                FavoriteMusic = new FavoriteMusicResponseDto(user.FavoriteMusic);
            }
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
            if (context.Type == typeof(UserResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UserResponseDto.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(UserResponseDto.Birthday))] = new OpenApiString("2025-11-01"),
                    [ToCamelCase(nameof(UserResponseDto.Email))] = new OpenApiString("test@test.com"),
                    [ToCamelCase(nameof(UserResponseDto.Gender))] = new OpenApiInteger(0),
                    [ToCamelCase(nameof(UserResponseDto.IsStreetPass))] = new OpenApiBoolean(false),
                    [ToCamelCase(nameof(UserResponseDto.ImgUrl))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UserResponseDto.Message))] = new OpenApiString(""),
                };
            }

            if (context.Type == typeof(UserHistoryResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UserHistoryResponseDto.SessionCount))] = new OpenApiInteger(1),
                };
            }

            if (context.Type == typeof(FavoriteMusicResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(FavoriteMusicResponseDto.MusicId))] = new OpenApiInteger(0)
                };
            }

            if (context.Type == typeof(UserProviderResponseDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UserProviderResponseDto.ProviderName))] = new OpenApiString("Spotify"),
                    [ToCamelCase(nameof(UserProviderResponseDto.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(UserProviderResponseDto.Password))] = new OpenApiString("password")
                };
            }
        }
    }
}
