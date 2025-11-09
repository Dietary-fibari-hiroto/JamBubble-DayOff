using Microsoft.Extensions.DependencyInjection;
using Microsoft.OpenApi.Models;
using Server.src.DTOs;

namespace Server.src.Configrations
{
    public static class SwaggerConfigExtensions
    {
        public static void AddCustomSwagger(this IServiceCollection services)
        {
            services.AddSwaggerGen(options =>
                {
                    options.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
                    {
                        Description = "JWT認証用トークンを入力してください。例: Bearer {token}",
                        Name = "Authorization",
                        In = ParameterLocation.Header,
                        Type = SecuritySchemeType.Http,
                        Scheme = "bearer",
                        BearerFormat = "JWT"
                    });

                    options.OperationFilter<AuthOperationFilter>();

                    // XMLの読み込み
                    var xmlFilename = $"{System.Reflection.Assembly.GetExecutingAssembly().GetName().Name}.xml";
                    options.IncludeXmlComments(Path.Combine(AppContext.BaseDirectory, xmlFilename));

                    // 作成したスキーマフィルターを登録
                    options.SchemaFilter<UserRequestFilter>();
                    options.SchemaFilter<UserResponseFilter>();
                    options.SchemaFilter<AuthRequestFilter>();
                    options.SchemaFilter<AuthResponseFilter>();
                    options.SchemaFilter<MessageRequestFilter>();
                    options.SchemaFilter<MessageResponseFilter>();

                });
        }
    }
}