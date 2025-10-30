using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Server.src.Interfaces;

namespace Server.src.Api.Controllers
{

    [ApiController]
    [Route("/api/[controller]")]
    public class TestController : ControllerBase
    {
        [HttpGet]
        public async Task<IActionResult> GetTest() =>Ok("DayOff");
    }

    [ApiController]
    [Route("/api/[controller]")]
    public class UserController:ControllerBase
    {
        private readonly IUserService _userService;
        public UserController(IUserService userService) {
            _userService = userService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll() => Ok(await _userService.GetAllUserAsync());

        //[Authorize]
        //[HttpGet]



    }
}
