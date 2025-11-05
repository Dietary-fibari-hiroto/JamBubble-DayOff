using System.Net;
using System.Text.Json;

namespace Server.src.Middlewares
{
    // グローバル例外処理ミドルウェア
    public class ExceptionHandlingMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger<ExceptionHandlingMiddleware> _logger;

        public ExceptionHandlingMiddleware(RequestDelegate next, ILogger<ExceptionHandlingMiddleware> logger)
        {
            _next = next;
            _logger = logger;
        }

        public async Task InvokeAsync(HttpContext httpContext)
        {
            try
            {
                await _next(httpContext);
            }
            catch (Exception e)
            {
                await HandleExceptionAsync(httpContext, e);
            }

        }

        private async Task HandleExceptionAsync(HttpContext context, Exception exception)
        {
            var statusCode = HttpStatusCode.InternalServerError; // 500
            string errorMessage = "An unexpected internal server error occurred.";

            switch (exception)
            {
                case InvalidOperationException ex when ex.Message == "EmailConflict":
                    statusCode = HttpStatusCode.Conflict; // 409
                    errorMessage = "The specified Email is already in use.";
                    break;
                case InvalidOperationException ex when ex.Message == "UserProviderConflict":
                    statusCode = HttpStatusCode.Conflict; // 409
                    errorMessage = "The specified User Provider is already register.";
                    break;
            }

            _logger.LogError(exception, "An exception occurred: {Message}", exception.Message);

            context.Response.ContentType = "application/json";
            context.Response.StatusCode = (int)statusCode;

            var result = JsonSerializer.Serialize(new { error = errorMessage });
            await context.Response.WriteAsync(result);
        }
    }
}
