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
    /// <summary>
    /// 開発時のダミーデータをDBに挿入するクラス
    /// ユーザー、セッション、プロバイダ、タグ、セッションソート設定、シーンなどのデータを挿入する
    /// </summary>
    public class DevelopmentDataSeeder
    {
        public static async Task SeedAsync(IServiceProvider serviceProvider)
        {
            var tagCount = 3; // タグの生成回数

            using var scope = serviceProvider.CreateScope();
            var dbContext = scope.ServiceProvider.GetRequiredService<AppDbContext>();
            var userService = scope.ServiceProvider.GetRequiredService<IUserService>();

            // マスタ作成
            await AddProviderAsync(dbContext); // プロバイダ
            await AddTagAsync(dbContext, tagCount); // セッションタグ
            await AddSessionSortSettingAsync(dbContext); // セッションソート設定
            await AddSceneAsync(dbContext); // セッションシーン

            // ユーザーの作成
            var dummyUsers = GenerateDummyUsers();
            var addedDummyUsers = await AddUsersAsync(userService, dummyUsers);

            // 作成されたユーザーを元に作成・更新
            foreach (var user in addedDummyUsers)
            {
                await UpdateUsersAsync(userService, addedDummyUsers); // ユーザー作成時に追記できなかった項目
                await AddFriendsAsync(dbContext, user, addedDummyUsers); // フレンド
                await AddMessagesAsync(dbContext, user, 5); // メッセージ
                await AddUserProviderAsync(userService, user); // ユーザプロバイダ
                await AddSessionsAsync(dbContext, user, 2); // セッション
                await AddFornowAsync(dbContext, user); // fornow
            }
        }

        /// <summary>
        /// プロバイダ作成
        /// </summary>
        /// <param name="dbContext"></param>
        /// <returns></returns>
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

        /// <summary>
        /// タグ作成
        /// </summary>
        /// <param name="dbContext"></param>
        /// <param name="count"></param>
        /// <returns></returns>
        private static async Task AddTagAsync(AppDbContext dbContext, int count)
        {
            for (int i = 1; i <= count; i++)
            {
                var tag = await dbContext.Tags.FirstOrDefaultAsync(t => t.Id == i);
                if (tag == null)
                {
                    await dbContext.Tags.AddAsync(new Tag
                    {
                        Id = i,
                        Label = $"Tag{i}",
                    });
                    await dbContext.SaveChangesAsync();
                }
            }
        }

        /// <summary>
        /// セッションソート設定作成
        /// </summary>
        /// <param name="dbContext"></param>
        /// <returns></returns>
        private static async Task AddSessionSortSettingAsync(AppDbContext dbContext)
        {
            var setting = await dbContext.SessionSortSettings.FirstOrDefaultAsync(s => s.Id == 1);
            if (setting == null)
            {
                await dbContext.SessionSortSettings.AddAsync(new SessionSortSetting
                {
                    Id = 1,
                    Label = "Default",
                    Description = "Default Sort Setting"
                });
                await dbContext.SaveChangesAsync();
            }
        }

        /// <summary>
        /// セッションシーン作成
        /// </summary>
        /// <param name="dbContext"></param>
        /// <returns></returns>
        private static async Task AddSceneAsync(AppDbContext dbContext)
        {
            var scene = await dbContext.Scenes.FirstOrDefaultAsync(s => s.Id == 1);
            if (scene == null)
            {
                await dbContext.Scenes.AddAsync(new Scene
                {
                    Id = 1,
                    Name = "None"
                });
            }
        }

        /// <summary>
        /// 作成するユーザー一覧
        /// </summary>
        /// <returns></returns>
        private static List<RegisterUserRequestDto> GenerateDummyUsers()
        {
            var pasTemp = "password";
            return new List<RegisterUserRequestDto>
            {
                new RegisterUserRequestDto { Name = "Alice", Email = "test10@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1995, 5, 15) },
                new RegisterUserRequestDto { Name = "Bob", Email = "test11@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1990, 3, 10) },
                new RegisterUserRequestDto { Name = "Charlie", Email = "test12@test.com", Password = pasTemp, Gender = 0, Birthday = new DateOnly(1988, 7, 20) },
                new RegisterUserRequestDto { Name = "Diana", Email = "test13@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1992, 11, 5) },
                new RegisterUserRequestDto { Name = "Eve", Email = "test14@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1997, 1, 25) },
                new RegisterUserRequestDto { Name = "Frank", Email = "test15@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1985, 9, 30) },
                new RegisterUserRequestDto { Name = "Grace", Email = "test16@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1993, 4, 18) },
                new RegisterUserRequestDto { Name = "Hank", Email = "test17@test.com", Password = pasTemp, Gender = 0, Birthday = new DateOnly(1989, 6, 12) },
                new RegisterUserRequestDto { Name = "Ivy", Email = "test18@test.com", Password = pasTemp, Gender = 1, Birthday = new DateOnly(1996, 8, 22) },
                new RegisterUserRequestDto { Name = "Jack", Email = "test19@test.com", Password = pasTemp, Gender = 2, Birthday = new DateOnly(1991, 12, 3) }
            };
        }

        /// <summary>
        /// ユーザ作成
        /// </summary>
        /// <param name="userService"></param>
        /// <param name="dummyUsers"></param>
        /// <returns></returns>
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

        /// <summary>
        /// ユーザ情報に追記
        /// </summary>
        /// <param name="userService"></param>
        /// <param name="users"></param>
        /// <returns></returns>
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
                    MusicId = $"music-{random.Next(1, 2)}"
                }, user.Id);
            }
        }
        
        /// <summary>
        /// フレンド追加
        /// </summary>
        /// <param name="dbContext"></param>
        /// <param name="user"></param>
        /// <param name="allUsers"></param>
        /// <returns></returns>
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

        /// <summary>
        /// メッセージ作成
        /// </summary>
        /// <param name="dbContext"></param>
        /// <param name="user"></param>
        /// <param name="messageCount"></param>
        /// <returns></returns>
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

        /// <summary>
        /// ユーザプロバイダ作成
        /// </summary>
        /// <param name="userService"></param>
        /// <param name="user"></param>
        /// <returns></returns>
        private static async Task AddUserProviderAsync(IUserService userService, User user)
        {
            await userService.AddUserProviderAsync(new RegisterUserProviderRequestDto
            {
                ProviderId = 1,
                Name = user.Name
            }, user.Id);
        }

        /// <summary>
        /// セッションタグ作成
        /// </summary>
        /// <param name="dbContext"></param>
        /// <param name="user"></param>
        /// <param name="count"></param>
        /// <returns></returns>
        private static async Task AddSessionsAsync(AppDbContext dbContext, User user, int count)
        {
            for(int i = 1; i <= count; i++)
            {
                await dbContext.Sessions.AddAsync(new Session
                {
                    UserId = user.Id,
                    Title = $"Session Title{i}",
                    ProviderId = 1,
                    Password = "password",
                    SceneId = 1,
                    DefaultSortId = 1,
                    ImgUrl = "/default/default_session_image.png",
                    Description = "This is a sample session.",
                    IsPublic = true,
                    UserCapacity = 10,
                    SessionTag = new List<SessionTag>
                    {
                        new SessionTag { TagId = 1 },
                        new SessionTag { TagId = 2 },
                        new SessionTag { TagId = 3 }
                    }
                });
            }
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Fornow作成
        /// </summary>
        /// <param name="dbContext"></param>
        /// <param name="user"></param>
        /// <returns></returns>
        private static async Task AddFornowAsync(AppDbContext dbContext, User user)
        {
            await dbContext.Fornows.AddAsync(new Fornow
            {
                UserId = user.Id,
                MusicId = $"music-{user.Id}",
                Message = "This is a sample.",
            });
            await dbContext.SaveChangesAsync();
        }
    }
}
