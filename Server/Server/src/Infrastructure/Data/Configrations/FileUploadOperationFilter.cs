using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace Server.src.Configrations
{
    public class FileUploadOperationFilter : IOperationFilter
    {
        /// <summary>
        /// SwaggerでAPIドキュメント生成時のみ処理される
        /// IFromFileを受け付けるエンドポイントのリクエスト使用を自動でmultipart/form-dataに設定するクラス
        /// 動作していないかもしれない
        /// </summary>
        /// <param name="operation"></param>
        /// <param name="context"></param>
        public void Apply(OpenApiOperation operation, OperationFilterContext context)
        {
            var fileParameters = context.MethodInfo.GetParameters()
                .Where(p => p.ParameterType == typeof(IFormFile) || p.ParameterType == typeof(IFormFileCollection))
                .ToList();

            if (fileParameters.Any())
            {
                operation.RequestBody = new OpenApiRequestBody
                {
                    Content = new Dictionary<string, OpenApiMediaType>
                    {
                        ["multipart/form-data"] = new OpenApiMediaType
                        {
                            Schema = new OpenApiSchema
                            {
                                Type = "object",
                                Properties = fileParameters.ToDictionary(
                                    p => p.Name,
                                    p => new OpenApiSchema { Type = "string", Format = "binary" }
                                ),
                                Required = fileParameters.Select(p => p.Name).ToHashSet()
                            }
                        }
                    }
                };
            }
        }
    }
}
