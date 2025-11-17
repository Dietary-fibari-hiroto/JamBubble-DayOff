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

    public class FriendRequestSndDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string? ImgUrl { get; set; }
        public int State { get; set; }
        public FriendRequestSndDto(FriendRequest friendRequest)
        {
            this.Id = friendRequest.PassUserId;
            this.Name = friendRequest.PassUser!.Name;
            this.ImgUrl = friendRequest.PassUser!.ImgUrl;
            this.State = friendRequest.State;
        }
    }

    public class FriendRequestRcvDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string? ImgUrl { get; set; }
        public int State { get; set; }
        public FriendRequestRcvDto(FriendRequest friendRequest)
        {
            this.Id = friendRequest.SendUserId;
            this.Name = friendRequest.SendUser!.Name;
            this.ImgUrl = friendRequest.SendUser!.ImgUrl;
            this.State = friendRequest.State;
        }
    }

    public class FriendRequestSndRcvDto
    {
        public List<FriendRequestSndDto> SentRequests { get; set; } = new List<FriendRequestSndDto>();
        public List<FriendRequestRcvDto> ReceivedRequests { get; set; } = new List<FriendRequestRcvDto>();
        public FriendRequestSndRcvDto(List<FriendRequest> sentRequests, List<FriendRequest> receivedRequests)
        {
            foreach (var request in sentRequests)
            {
                SentRequests.Add(new FriendRequestSndDto(request));
            }
            foreach (var request in receivedRequests)
            {
                ReceivedRequests.Add(new FriendRequestRcvDto(request));
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

            if (context.Type == typeof(FriendRequestSndDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(FriendRequestSndDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(FriendRequestSndDto.Name))] = new OpenApiString("Name"),
                    [ToCamelCase(nameof(FriendRequestSndDto.ImgUrl))] = new OpenApiString("ImgUrl"),
                    [ToCamelCase(nameof(FriendRequestSndDto.State))] = new OpenApiInteger(0),
                };
            }

            if (context.Type == typeof(FriendRequestRcvDto))
            {
                schema.Example = new OpenApiObject
                {
                    [ToCamelCase(nameof(FriendRequestRcvDto.Id))] = new OpenApiInteger(1),
                    [ToCamelCase(nameof(FriendRequestRcvDto.Name))] = new OpenApiString("Name"),
                    [ToCamelCase(nameof(FriendRequestRcvDto.ImgUrl))] = new OpenApiString("ImgUrl"),
                    [ToCamelCase(nameof(FriendRequestRcvDto.State))] = new OpenApiInteger(0),
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