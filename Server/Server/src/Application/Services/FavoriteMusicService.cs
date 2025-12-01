using System.Diagnostics;
using Microsoft.EntityFrameworkCore;
using Server.Data;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;

namespace Server.src.Services
{
    public class FavoriteMusicService : IFavoriteMusicService
    {
        private readonly IFavoriteMusicSummaryRepository _favoSumRepo;

        public FavoriteMusicService(IFavoriteMusicSummaryRepository favoSumRepo)
        {
            _favoSumRepo = favoSumRepo;
        }

        public async Task<List<FavoriteMusicSummary>> GetFavoriteMusicRankingAsync(int n, int skip)
        {
            return await _favoSumRepo.GetFavoriteMusicRankingAsync(n, skip);
        }


    }
}
