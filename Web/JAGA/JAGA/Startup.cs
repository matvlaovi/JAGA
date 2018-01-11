using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(JAGA.Startup))]
namespace JAGA
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
