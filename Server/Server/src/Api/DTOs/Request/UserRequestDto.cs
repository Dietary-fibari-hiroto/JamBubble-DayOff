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
        public IFormFile? userImage { get; set; } = null;

        // Userの型に変換
        public User RequestToUser(User user)
        {
            user.Name = this.Name;
            user.Email = this.Email;
            user.Password = this.Password;
            user.Gender = this.Gender;
            user.Birthday = this.Birthday;

            // 画像がある場合は保存してURLを設定
            if (this.userImage != null && this.userImage.Length > 0)
            {
                var userImageFolder = Path.Combine("wwwroot", "images", "users");
                if (!Directory.Exists(userImageFolder))
                {
                    Directory.CreateDirectory(userImageFolder);
                }

                var uniqueFileName = $"{Guid.NewGuid()}_{Path.GetExtension(this.userImage.FileName)}";
                var filePath = Path.Combine(userImageFolder, uniqueFileName);

                using (var stream = new FileStream(filePath, FileMode.Create))
                {
                    this.userImage.CopyTo(stream);
                }
                user.ImgUrl = $"/images/users/{uniqueFileName}";
            }else
            {
                // TODO: デフォルト画像はどうするのか？
                user.ImgUrl = "/default/default_user_image.png"; // デフォルト画像のパス
            }

            return user;
        }
    }

    public class UpdateUserAllDataRequestDto
    {
        public string? Name { get; set; }
        public string? Email { get; set; }
        public string? Password { get; set; }
        public string? Message {  get; set; }
        public bool? IsStreetPass { get; set; }
        public string? MusicId { get; set; }
        public IFormFile? userImage { get; set; } = null;

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

            if (!string.IsNullOrEmpty(this.Message))
            {
                user.Message = this.Message;
            }

            if (this.IsStreetPass != null && this.IsStreetPass != user.IsStreetPass)
            {
                user.IsStreetPass = (bool)this.IsStreetPass;
            }

            if (user.FavoriteMusic != null && !string.IsNullOrEmpty(this.MusicId))
            {
                user.FavoriteMusic.MusicId = this.MusicId;
            }

            // 画像がある場合はもとの画像を削除、新しい画像保存してURLを設定
            if (userImage != null && userImage.Length > 0)
            {
                // TODO: 古い画像を削除する処理は必要か？
                // 既存の画像を削除
                if (!string.IsNullOrEmpty(user.ImgUrl) && user.ImgUrl != "/default/default_user_image.png")
                {
                    // デフォルト画像でない場合のみ削除
                    var existingFilePath = Path.Combine("wwwroot", user.ImgUrl.TrimStart('/'));
                    if (File.Exists(existingFilePath))
                    {
                        File.Delete(existingFilePath);
                    }
                }

                var userImageFolder = Path.Combine("wwwroot", "images", "users");
                if (!Directory.Exists(userImageFolder))
                {
                    Directory.CreateDirectory(userImageFolder);
                }
                var uniqueFileName = $"{Guid.NewGuid()}_{Path.GetExtension(userImage.FileName)}";
                var filePath = Path.Combine(userImageFolder, uniqueFileName);
                using (var stream = new FileStream(filePath, FileMode.Create))
                {
                    userImage.CopyTo(stream);
                }
                user.ImgUrl = $"/images/users/{uniqueFileName}";
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
                    Name = this.Name
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
                    [nameof(RegisterUserRequestDto.Name)] = new OpenApiString("test"),
                    [nameof(RegisterUserRequestDto.Birthday)] = new OpenApiString("2025-11-01"),
                    [nameof(RegisterUserRequestDto.Email)] = new OpenApiString("test@test.com"),
                    [nameof(RegisterUserRequestDto.Password)] = new OpenApiString("password"),
                    [nameof(RegisterUserRequestDto.Gender)] = new OpenApiInteger(0)
                };
            }

            if (context.Type == typeof(UpdateUserAllDataRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [nameof(UpdateUserAllDataRequestDto.Name)] = new OpenApiString(""),
                    [nameof(UpdateUserAllDataRequestDto.Email)] = new OpenApiString(""),
                    [nameof(UpdateUserAllDataRequestDto.Password)] = new OpenApiString(""),
                    [nameof(UpdateUserAllDataRequestDto.Message)] = new OpenApiString(""),
                    [nameof(UpdateUserAllDataRequestDto.IsStreetPass)] = new OpenApiBoolean(false),
                    [nameof(UpdateUserAllDataRequestDto.MusicId)] = new OpenApiString("")
                };
            }

            if (context.Type == typeof(RegisterUserProviderRequestDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(RegisterUserProviderRequestDto.ProviderId))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(RegisterUserProviderRequestDto.Name))] = new OpenApiString("test")
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
