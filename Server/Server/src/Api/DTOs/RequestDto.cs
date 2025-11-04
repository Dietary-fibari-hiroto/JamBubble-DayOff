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
    }

    public class UpdateUserRequestDto
    {
        public string? Name { get; set; }
        public string? Email { get; set; }
        public string? Password { get; set; }
        public string? ImgUrl {  get; set; }
        public string? Message {  get; set; }
        public bool IsStreetPass { get; set; } = false;

        // Userの型に変換
        public User RequestToUser(User user)
        {
            if (!string.IsNullOrEmpty(this.Name))
            {
                user.Name = this.Name;
            }

            if (!string.IsNullOrEmpty(this.Email))
            { 
                user.Email = this.Email;
            }

            if (!string.IsNullOrEmpty(this.ImgUrl))
            {
                user.ImgUrl = this.ImgUrl;
            }

            if (!string.IsNullOrEmpty(this.Message))
            {
                user.Message = this.Message;
            }

            if (this.IsStreetPass != user.IsStreetPass)
            {
                user.IsStreetPass = this.IsStreetPass;
            }

            return user;
        }
    }

    public class UpdateFavoriteMusicRequestDto
    {
        public string? MusicId { get; set; }

        // FavoriteMusicの型に変換
        public FavoriteMusic RequestToFavoriteMusic(FavoriteMusic favoriteMusic)
        {
            if (!string.IsNullOrEmpty (this.MusicId))
            {
                favoriteMusic.MusicId = this.MusicId;
            }

            return favoriteMusic;
        }
    }

    public class UpdateUserAllDataRequestDto
    {
        public UpdateUserRequestDto? userDto { get; set; }
        public UpdateFavoriteMusicRequestDto? FavoriteMusicDto { get; set; }

        public User RequestDtoToEntitie(User user)
        {
            if(this.userDto != null)
            {
                user = this.userDto.RequestToUser(user);
            }

            if(this.FavoriteMusicDto != null && user.FavoriteMusic != null)
            {
                user.FavoriteMusic = this.FavoriteMusicDto.RequestToFavoriteMusic(user.FavoriteMusic);
            }

            return user;

        }
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
