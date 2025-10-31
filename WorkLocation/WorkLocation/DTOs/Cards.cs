using WorkLocation.Enums;
namespace WorkLocation.DTOs
{
    public class SessionCardDto
    {
        public string img_url { get; set; } = "";
        public ProviderEnum provider { get; set; }= ProviderEnum.None;
        public string title { get; set; } = "";

    }
}
