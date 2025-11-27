using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Components;

namespace Client.Services
{
    public  class NavService
    {
        private readonly NavigationManager _navigationManager;
        public NavService(NavigationManager navigationNamager)
        {
            _navigationManager = navigationNamager;
        }

        public void Navigate(string path)
        {
            _navigationManager.NavigateTo(path);
        }
    }
}
