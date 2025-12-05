namespace Client.Services.SpotifyServices.Auths
{
    public class SecureTokenStorage: ITokenStorage
    {
        public Task<string?> GetAsync(string key)
        {
            return SecureStorage.GetAsync(key);
        }

        public Task SetAsync(string key,string value)
        {
            SecureStorage.SetAsync(key, value);
            return Task.CompletedTask;
        }

        public Task RemoveAsync(string key)
        {
            SecureStorage.Remove(key);
            return Task.CompletedTask;
        }
    }
}
