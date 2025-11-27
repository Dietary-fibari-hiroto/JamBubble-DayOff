using System;
using Microsoft.Maui.Controls;
using System.IO;
using System.Threading.Tasks;

namespace Client.Helpers;

public static class ImageHelpers
{
    public static ImageSource ImageSourceFromBase64(string base64)
    {
        if (string.IsNullOrEmpty(base64)) return ImageSource.FromFile("default_artwork.png");
        try
        {
            var bytes = Convert.FromBase64String(base64);
            return ImageSource.FromStream(() => new MemoryStream(bytes));
        }
        catch
        {
            return ImageSource.FromFile("default_artwork.png");
        }
    }
}