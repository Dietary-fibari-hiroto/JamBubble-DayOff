using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;
using Server.src.Entities;

namespace Server.src.DTOs
{
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
                    ["isStreetPass"] = new OpenApiBoolean(false),
                    ["imgUrl"] = new OpenApiString("")
                };
            }

            if (context.Type == typeof(LoginRequest))
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
