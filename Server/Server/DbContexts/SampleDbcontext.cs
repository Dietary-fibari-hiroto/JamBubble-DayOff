using Microsoft.EntityFrameworkCore;
using Server.Models;

namespace Server.DbContexts
{
    public class SampleDbcontext : DbContext
    {
        public SampleDbcontext(DbContextOptions<SampleDbcontext> options) : base(options) { }
        public DbSet<Users> Users { get; set; }

    }
}
