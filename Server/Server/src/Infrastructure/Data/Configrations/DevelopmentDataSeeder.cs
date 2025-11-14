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
            using var scope = serviceProvider.CreateScope(); // スコープを作成
            var dbContext = scope.ServiceProvider.GetRequiredService<AppDbContext>();
            var userService = scope.ServiceProvider.GetRequiredService<IUserService>(); // サービスを解決
            
            var pasTemp = "password";

            // ランダム生成用のインスタンス
            var random = new Random();

            var addedDummyUsers = new List<User>();

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



            // ダミーデータのリスト
            var dummyUsers = new List<RegisterUserRequestDto>
            {
                new RegisterUserRequestDto
                {
                    Name = "Alice",
                    Email = "test10@test.com",
                    Password = pasTemp,
                    Gender = 1,
                    Birthday = new DateOnly(1995, 5, 15),
                    ImgUrl = "images/users/alice.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Bob",
                    Email = "test11@test.com",
                    Password = pasTemp,
                    Gender = 2,
                    Birthday = new DateOnly(1990, 3, 10),
                    ImgUrl = "images/users/bob.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Charlie",
                    Email = "test12@test.com",
                    Password = pasTemp,
                    Gender = 0,
                    Birthday = new DateOnly(1988, 7, 20),
                    ImgUrl = "images/users/charlie.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Diana",
                    Email = "test13@test.com",
                    Password = pasTemp,
                    Gender = 1,
                    Birthday = new DateOnly(1992, 11, 5),
                    ImgUrl = "images/users/diana.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Eve",
                    Email = "test14@test.com",
                    Password = pasTemp,
                    Gender = 2,
                    Birthday = new DateOnly(1997, 1, 25),
                    ImgUrl = "images/users/eve.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Frank",
                    Email = "test15@test.com",
                    Password = pasTemp,
                    Gender = 1,
                    Birthday = new DateOnly(1985, 9, 30),
                    ImgUrl = "images/users/frank.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Grace",
                    Email = "test16@test.com",
                    Password = pasTemp,
                    Gender = 2,
                    Birthday = new DateOnly(1993, 4, 18),
                    ImgUrl = "images/users/grace.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Hank",
                    Email = "test17@test.com",
                    Password = pasTemp,
                    Gender = 0,
                    Birthday = new DateOnly(1989, 6, 12),
                    ImgUrl = "images/users/hank.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Ivy",
                    Email = "test18@test.com",
                    Password = pasTemp,
                    Gender = 1,
                    Birthday = new DateOnly(1996, 8, 22),
                    ImgUrl = "images/users/ivy.png"
                },
                new RegisterUserRequestDto
                {
                    Name = "Jack",
                    Email = "test19@test.com",
                    Password = pasTemp,
                    Gender = 2,
                    Birthday = new DateOnly(1991, 12, 3),
                    ImgUrl = "images/users/jack.png"
                }
            };

            // ダミーデータを挿入
            foreach (var user in dummyUsers)
            {
                var userData = await userService.UserExistsAsync(user.Email);
                if (userData != null)
                {
                    await userService.DeleteUserAsync(userData.Id);
                }
                await userService.AddUserAsync(user);
                userData = await userService.UserExistsAsync(user.Email);
                if (userData == null) continue;
                addedDummyUsers.Add(userData);
            }

            foreach (var user in addedDummyUsers)
            {
                // ランダムなメッセージを生成
                var randomMessage = $"This is a random message {Guid.NewGuid()}";

                // ランダムな boolean 値を生成
                var randomBoolean = random.Next(0, 2) == 1;

                await userService.UpdateUserAsync(new UpdateUserAllDataRequestDto
                {
                    Message = randomMessage,
                    IsStreetPass = randomBoolean,
                    MusicId = $"music-{random.Next(1, 100)}" // ランダムな MusicId を生成
                }, user.Id);

                await userService.AddUserProviderAsync(new RegisterUserProviderRequestDto
                {
                    ProviderId = 1,
                    Name = user.Name,
                    Password = pasTemp
                }, user.Id);

                // 友達追加
                foreach (var inUser in addedDummyUsers)
                {
                    if (user.Id == inUser.Id)
                    {
                        continue;
                    }

                    var exists = await dbContext.Friends.AnyAsync(f => (f.User1Id == user.Id && f.User2Id == inUser.Id) || (f.User2Id == user.Id && f.User1Id == inUser.Id));
                    if(!exists)
                    {
                        await dbContext.Friends.AddAsync(new Friend
                        {
                            User1Id = user.Id,
                            User1 = user,
                            User2Id = inUser.Id,
                            User2 = inUser
                        });
                        await dbContext.SaveChangesAsync();
                    }
                }

                // メッセージ作成
                for(int i = 0; i < 5; i++)
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
        }
    }
}
