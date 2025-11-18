using System;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Server.src.Interfaces;

namespace Server.src.Services
{
    public class TimedHostedService : IHostedService, IDisposable
    {
        private readonly IServiceProvider _serviceProvider;
        private Timer? _timer;

        public TimedHostedService(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            // 起動後2分後5分ごとに実行
            _timer = new Timer(ExecuteTask, null, TimeSpan.FromMinutes(2), TimeSpan.FromMinutes(5));
            return Task.CompletedTask;
        }

        private void ExecuteTask(object? state)
        {
            // DIコンテナはスコープ付きサービスをシングルトンから直接使えないらしい
            // スコープを作成してその中でスコープ付きサービスを使う
            // イメージとしては、長生きするサービスの中で、短命なサービスを使いたい場合、使い捨ての作業場を一時的に作る必要がある
            // 一時的な作業場を作成
            using (var scope = _serviceProvider.CreateScope())
            {
                // 短命なサービスを作成
                var favoriteMusicSummaryRepository = scope.ServiceProvider.GetRequiredService<IFavoriteMusicSummaryRepository>();

                // awaitを使わず同期的に実行
                favoriteMusicSummaryRepository.AggregeteFavoriteMusicAsync().GetAwaiter().GetResult();
            }
            // 抜けると作業場と短命なサービスを破棄
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            _timer?.Change(Timeout.Infinite, 0);
            return Task.CompletedTask;
        }

        public void Dispose()
        {
            _timer?.Dispose();
        }
    }
}