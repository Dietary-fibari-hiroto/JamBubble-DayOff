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
        public required string Email { get; set; }
        [Required]
        public required string Password { get; set; }
    }

    public class RegisterUserRequestDto
    {
        [Required]
        public required string Name { get; set; }
        [Required]
        public required string Email { get; set; }
        [Required]
        public required string Password { get; set; }
        [Required]
        public required int Gender { get; set; }
        [Required]
        public required DateOnly Birthday { get; set; }
        public string? ImgUrl { get; set; } = string.Empty;

        // Userの型に変換
        public User RequestToUser(User user)
        {
            user.Name = this.Name;
            user.Email = this.Email;
            user.Password = this.Password;
            user.Gender = this.Gender;
            user.Birthday = this.Birthday;

            if (!string.IsNullOrEmpty(this.ImgUrl))
            {
                user.ImgUrl = this.ImgUrl;
            }

            return user;
        }
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

            if(!string.IsNullOrEmpty(this.Password))
            {
                user.Password = this.Password;
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
            if (context.Type == typeof(User))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(User.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(User.Birthday))] = new OpenApiString("2025-11-01"),
                    [ToCamelCase(nameof(User.Email))] = new OpenApiString("test@test.com"),
                    [ToCamelCase(nameof(User.Password))] = new OpenApiString("password"),
                    [ToCamelCase(nameof(User.Gender))] = new OpenApiInteger(0),
                    [ToCamelCase(nameof(User.ImgUrl))] = new OpenApiString("")
                };
            }

            if (context.Type == typeof(LoginRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(LoginRequestDto.Email))] = new OpenApiString("test@test.com"),
                    [ToCamelCase(nameof(LoginRequestDto.Password))] = new OpenApiString("password")
                };
            }

            if (context.Type == typeof(RegisterUserRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(RegisterUserRequestDto.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(RegisterUserRequestDto.Birthday))] = new OpenApiString("2025-11-01"),
                    [ToCamelCase(nameof(RegisterUserRequestDto.Email))] = new OpenApiString("test@test.com"),
                    [ToCamelCase(nameof(RegisterUserRequestDto.Password))] = new OpenApiString("password"),
                    [ToCamelCase(nameof(RegisterUserRequestDto.Gender))] = new OpenApiInteger(0),
                    [ToCamelCase(nameof(RegisterUserRequestDto.ImgUrl))] = new OpenApiString(""),
                };
            }

            if (context.Type == typeof(UpdateFavoriteMusicRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UpdateFavoriteMusicRequestDto.MusicId))] = new OpenApiString("")
                };
            }

            if (context.Type == typeof(UpdateUserRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UpdateUserRequestDto.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(UpdateUserRequestDto.Email))] = new OpenApiString("test@test.com"),
                    [ToCamelCase(nameof(UpdateUserRequestDto.Password))] = new OpenApiString("password"),
                    [ToCamelCase(nameof(UpdateUserRequestDto.ImgUrl))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UpdateUserRequestDto.Message))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UpdateUserRequestDto.IsStreetPass))] = new OpenApiBoolean(false),

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
