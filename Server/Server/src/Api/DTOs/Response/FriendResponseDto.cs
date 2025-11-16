using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Server.src.Entities;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.ComponentModel.DataAnnotations;

namespace Server.src.DTOs
{
    public class FriendResposeDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string ImgUrl { get; set; } = string.Empty;

        public FriendResposeDto(User user)
        {
            this.Id = user.Id;
            this.Name = user.Name;
            if (user.ImgUrl != null)
            {
                this.ImgUrl = user.ImgUrl;
            }
        }
    }

    // SwaggerUI上でのExample Valueの設定
    public class FriendResponseFilter : ISchemaFilter
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
            if (context.Type == typeof(FriendResposeDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(FriendResposeDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(FriendResposeDto.Name))] = new OpenApiString("Name"),
                    [ToCamelCase(nameof(FriendResposeDto.ImgUrl))] = new OpenApiString("ImgUrl"),
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