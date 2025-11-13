using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace Server.src.Configrations
{
    public class AuthOperationFilter : IOperationFilter
    {
        void IOperationFilter.Apply(OpenApiOperation operation, OperationFilterContext context)
        {
            // [Authorize]属性を取得
            var authAttributes = context.MethodInfo
                .GetCustomAttributes(true)
                .OfType<AuthorizeAttribute>()
                .Distinct();

            // [Authorize]属性がある場合
            if (authAttributes.Any())
            {
                // レスポンスを追加
                operation.Responses.Add("401", new OpenApiResponse { Description = "Unauthorized" });
                operation.Responses.Add("403", new OpenApiResponse { Description = "Forbidden" });

                // JWT Bearer認証スキームの参照を作成
                var jwtbearerScheme = new OpenApiSecurityScheme
                {
                    Reference = new OpenApiReference
                    {
                        Type = ReferenceType.SecurityScheme,
                        Id = "Bearer"
                    }
                };

                // セキュリティ要件を追加
                operation.Security = new List<OpenApiSecurityRequirement>
                {
                    new OpenApiSecurityRequirement
                    {
                        [ jwtbearerScheme ] = Array.Empty<string>()
                    }
                };
            }
        }
    }

    // JWTからユーザーIDを取得
    // 存在しないならエラー
    // いらないかも
    public class UserIdFilter : IActionFilter
    {
        public void OnActionExecuting(ActionExecutingContext context)
        {
            var userId = context.HttpContext.User.GetUserId();
            if (userId == null)
            {
                context.Result = new UnauthorizedObjectResult("Invalid user ID format in token.");
                return;
            }
            context.HttpContext.Items["UserId"] = userId.Value;
        }

        public void OnActionExecuted(ActionExecutedContext context) { }
    }
}
