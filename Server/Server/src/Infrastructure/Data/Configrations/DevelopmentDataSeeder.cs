using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Server.Data;
using Server.src.DTOs;
using Server.src.Entities;
using Server.src.Interfaces;
using Server.src.Services;
using System;

namespace Server.src.Configrations
{
    public class DevelopmentDataSeeder
    {
        public static async Task SeedAsync(IServiceProvider serviceProvider)
        {
            var tempPas = "password";
            using var scope = serviceProvider.CreateScope();
            var dbContext = scope.ServiceProvider.GetRequiredService<AppDbContext>();
            var userService = scope.ServiceProvider.GetRequiredService<IUserService>();

            // プロバイダーの作成
            await AddProviderAsync(dbContext);

            // ユーザーの作成
            var dummyUsers = GenerateDummyUsers();
            var addedDummyUsers = await AddUsersAsync(userService, dummyUsers);

            foreach (var user in addedDummyUsers)
            {
                await UpdateUsersAsync(userService, addedDummyUsers);
                await AddFriendsAsync(dbContext, user, addedDummyUsers);
                await AddMessagesAsync(dbContext, user, 5);
                await AddUserProviderAsync(userService, user, tempPas);
            }
        }

        private static async Task AddProviderAsync(AppDbContext dbContext)
        {
            // プロバイダー挿入
            var provider = await dbContext.Providers.FirstOrDefaultAsync(p => p.Id == 1);
            if (provider == null)
            {
                await dbContext.Providers.AddAsync(new Provider
                {
                    Id = 1,
                    Name = "Spotipy"
                });
                await dbContext.SaveChangesAsync();
            }
        }

        private static List<RegisterUserRequestDto> GenerateDummyUsers()
        {
            var pasTemp = "password";
            return new List<RegisterUserRequestDto>
            {
                new RegisterUserRequestDto { Name = "Alice", Email = "test10@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1995, 5, 15), ImgUrl = "images/users/alice.png" },
                new RegisterUserRequestDto { Name = "Bob", Email = "test11@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1990, 3, 10), ImgUrl = "images/users/bob.png" },
                new RegisterUserRequestDto { Name = "Charlie", Email = "test12@test.com", Password = pasTemp, Gender = 0, Birthday = new DateOnly(1988, 7, 20), ImgUrl = "images/users/charlie.png" },
                new RegisterUserRequestDto { Name = "Diana", Email = "test13@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1992, 11, 5), ImgUrl = "images/users/diana.png" },
                new RegisterUserRequestDto { Name = "Eve", Email = "test14@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1997, 1, 25), ImgUrl = "images/users/eve.png" },
                new RegisterUserRequestDto { Name = "Frank", Email = "test15@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1985, 9, 30), ImgUrl = "images/users/frank.png" },
                new RegisterUserRequestDto { Name = "Grace", Email = "test16@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1993, 4, 18), ImgUrl = "images/users/grace.png" },
                new RegisterUserRequestDto { Name = "Hank", Email = "test17@test.com", Password = pasTemp, Gender = 0, Birthday = new DateOnly(1989, 6, 12), ImgUrl = "images/users/hank.png" },
                new RegisterUserRequestDto { Name = "Ivy", Email = "test18@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1996, 8, 22), ImgUrl = "images/users/ivy.png" },
                new RegisterUserRequestDto { Name = "Jack", Email = "test19@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1991, 12, 3), ImgUrl = "images/users/jack.png" }
            };
        }

        private static async Task<List<User>> AddUsersAsync(IUserService userService, List<RegisterUserRequestDto> dummyUsers)
        {
            var addedUsers = new List<User>();
            foreach (var user in dummyUsers)
            {
                var existingUser = await userService.UserExistsAsync(user.Email);
                if (existingUser != null)
                {
                    await userService.DeleteUserAsync(existingUser.Id);
                }
                await userService.AddUserAsync(user);
                var newUser = await userService.UserExistsAsync(user.Email);
                if (newUser != null)
                {
                    addedUsers.Add(newUser);
                }
            }
            return addedUsers;
        }

        private static async Task UpdateUsersAsync(IUserService userService, List<User> users)
        {
            // ランダム生成用のインスタンス
            var random = new Random();

            foreach (var user in users)
            {
                await userService.UpdateUserAsync(new UpdateUserAllDataRequestDto
                {
                    Message = $"This is a random message {Guid.NewGuid()}",
                    IsStreetPass = random.Next(0, 2) == 1,
                    MusicId = $"music-{random.Next(1, 100)}"
                }, user.Id);
            }
        }

        private static async Task AddFriendsAsync(AppDbContext dbContext, User user, List<User> allUsers)
        {
            foreach (var friend in allUsers)
            {
                if (user.Id == friend.Id) continue;

                var exists = await dbContext.Friends.AnyAsync(f =>
                    (f.User1Id == user.Id && f.User2Id == friend.Id) ||
                    (f.User2Id == user.Id && f.User1Id == friend.Id));

                if (!exists)
                {
                    await dbContext.Friends.AddAsync(new Friend
                    {
                        User1Id = user.Id,
                        User1 = user,
                        User2Id = friend.Id,
                        User2 = friend
                    });
                }
            }
            await dbContext.SaveChangesAsync();
        }

        private static async Task AddMessagesAsync(AppDbContext dbContext, User user, int messageCount)
        {
            for (int i = 0; i < messageCount; i++)
            {
                await dbContext.Messages.AddAsync(new Message
                {
                    UserId = user.Id,
                    User = user,
                    Title = "Dummy Message!",
                    Content = $"This is a random message {Guid.NewGuid()}"
                });
            }
            await dbContext.SaveChangesAsync();
        }

        private static async Task AddUserProviderAsync(IUserService userService, User user, string password)
        {
            await userService.AddUserProviderAsync(new RegisterUserProviderRequestDto
            {
                ProviderId = 1,
                Name = user.Name,
                Password = password
            }, user.Id);
        }
    }
}
