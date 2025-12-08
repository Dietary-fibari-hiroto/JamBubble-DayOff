using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace Server.src.Configrations
{
    /// <summary>
    /// Swagger の API ドキュメント生成時のみ処理されるクラス
    /// [Authorize] 属性が付与されたエンドポイントに対して、認証が必要であることを示すレスポンスを返す
    /// </summary>
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
}
