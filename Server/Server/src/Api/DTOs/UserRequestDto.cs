using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;


namespace Server.src.DTOs
{
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

    public class UpdateUserAllDataRequestDto
    {
        public string? Name { get; set; }
        public string? Email { get; set; }
        public string? Password { get; set; }
        public string? ImgUrl {  get; set; }
        public string? Message {  get; set; }
        public bool IsStreetPass { get; set; } = false;
        public string? MusicId { get; set; }

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

            if (user.FavoriteMusic != null && !string.IsNullOrEmpty(this.MusicId))
            {
                user.FavoriteMusic.MusicId = this.MusicId;
            }

            return user;
        }
    }

    public class RegisterUserProviderRequestDto
    {
        // TODO:プロバイダー登録時のレクエスト内容を考える
        [Required]
        public required int ProviderId { get; set; }
        [Required]
        public required string Name { get; set; }
        [Required]
        public required string Password { get; set; }

        public User RequestToUserProvider(User user)
        {
            // 中身が空なら作成してから
            if (user.UserProviders == null)
            {
                user.UserProviders = new List<UserProvider>();
            }

            user.UserProviders.Add(
                new UserProvider
                {
                    UserId = user.Id,
                    ProviderId = this.ProviderId,
                    Name = this.Name,
                    Password = this.Password
                }
            );
            

            return user;
        }
    }

    public class DeleteUserProviderRequestDto
    {
        [Required]
        public required int ProviderId { get; set; }
    }

    public class UserRequestFilter : ISchemaFilter
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

            if (context.Type == typeof(UpdateUserAllDataRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(UpdateUserAllDataRequestDto.Name))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UpdateUserAllDataRequestDto.Email))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UpdateUserAllDataRequestDto.Password))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UpdateUserAllDataRequestDto.ImgUrl))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UpdateUserAllDataRequestDto.Message))] = new OpenApiString(""),
                    [ToCamelCase(nameof(UpdateUserAllDataRequestDto.IsStreetPass))] = new OpenApiBoolean(false),
                    [ToCamelCase(nameof(UpdateUserAllDataRequestDto.MusicId))] = new OpenApiString("")
                };
            }

            if (context.Type == typeof(RegisterUserProviderRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(RegisterUserProviderRequestDto.ProviderId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(RegisterUserProviderRequestDto.Name))] = new OpenApiString("test"),
                    [ToCamelCase(nameof(RegisterUserProviderRequestDto.Password))] = new OpenApiString("password"),
                };
            }

            if (context.Type == typeof(DeleteUserProviderRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(DeleteUserProviderRequestDto.ProviderId))] = new OpenApiInteger(1),
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
