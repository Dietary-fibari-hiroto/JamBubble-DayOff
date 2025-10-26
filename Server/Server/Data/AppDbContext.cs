using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Pomelo.EntityFrameworkCore.MySql.Scaffolding.Internal;
using Server.Models;

namespace Server.Data;

public partial class AppDbContext : DbContext
{
    public AppDbContext()
    {
    }

    public AppDbContext(DbContextOptions<AppDbContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Fornow> Fornows { get; set; }

    public virtual DbSet<Friend> Friends { get; set; }

    public virtual DbSet<FriendRequest> FriendRequests { get; set; }

    public virtual DbSet<Guest> Guests { get; set; }

    public virtual DbSet<Message> Messages { get; set; }

    public virtual DbSet<Provider> Providers { get; set; }

    public virtual DbSet<Request> Requests { get; set; }

    public virtual DbSet<RequestCache> RequestCaches { get; set; }

    public virtual DbSet<Scene> Scenes { get; set; }

    public virtual DbSet<Session> Sessions { get; set; }

    public virtual DbSet<SessionSortSetting> SessionSortSettings { get; set; }

    public virtual DbSet<StreetPassHistory> StreetPassHistories { get; set; }

    public virtual DbSet<StreetPassOption> StreetPassOptions { get; set; }

    public virtual DbSet<Tag> Tags { get; set; }

    public virtual DbSet<User> Users { get; set; }

    public virtual DbSet<UserProvider> UserProviders { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder
            .UseCollation("utf8mb4_0900_ai_ci")
            .HasCharSet("utf8mb4");

        modelBuilder.Entity<Fornow>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("fornows");

            entity.HasIndex(e => e.UserId, "fk_fornows_users1_idx");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.Finished).HasColumnName("finished");
            entity.Property(e => e.Message)
                .HasColumnType("text")
                .HasColumnName("message");
            entity.Property(e => e.MusicId)
                .HasMaxLength(50)
                .HasColumnName("music_id");
            entity.Property(e => e.UserId).HasColumnName("user_id");

            entity.HasOne(d => d.User).WithMany(p => p.Fornows)
                .HasForeignKey(d => d.UserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_fornows_users1");

            entity.HasMany(d => d.Users).WithMany(p => p.FornowsNavigation)
                .UsingEntity<Dictionary<string, object>>(
                    "FornowLike",
                    r => r.HasOne<User>().WithMany()
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.ClientSetNull)
                        .HasConstraintName("fk_fornow_likes_users1"),
                    l => l.HasOne<Fornow>().WithMany()
                        .HasForeignKey("FornowId")
                        .OnDelete(DeleteBehavior.ClientSetNull)
                        .HasConstraintName("fk_fornow_likes_fornows1"),
                    j =>
                    {
                        j.HasKey("FornowId", "UserId")
                            .HasName("PRIMARY")
                            .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });
                        j.ToTable("fornow_likes");
                        j.HasIndex(new[] { "UserId" }, "fk_fornow_likes_users1_idx");
                        j.IndexerProperty<int>("FornowId").HasColumnName("fornow_id");
                        j.IndexerProperty<int>("UserId").HasColumnName("user_id");
                    });
        });

        modelBuilder.Entity<Friend>(entity =>
        {
            entity.HasKey(e => new { e.User1Id, e.User2Id })
                .HasName("PRIMARY")
                .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

            entity.ToTable("friends");

            entity.HasIndex(e => e.User2Id, "fk_friends_users2_idx");

            entity.Property(e => e.User1Id).HasColumnName("user1_id");
            entity.Property(e => e.User2Id).HasColumnName("user2_id");
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("created_at");

            entity.HasOne(d => d.User1).WithMany(p => p.FriendUser1s)
                .HasForeignKey(d => d.User1Id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_friends_users1");

            entity.HasOne(d => d.User2).WithMany(p => p.FriendUser2s)
                .HasForeignKey(d => d.User2Id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_friends_users2");
        });

        modelBuilder.Entity<FriendRequest>(entity =>
        {
            entity.HasKey(e => new { e.SendUserId, e.PassUserId })
                .HasName("PRIMARY")
                .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

            entity.ToTable("friend_requests");

            entity.HasIndex(e => e.PassUserId, "fk_friend_requests_users2_idx");

            entity.Property(e => e.SendUserId).HasColumnName("send_user_id");
            entity.Property(e => e.PassUserId).HasColumnName("pass_user_id");
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.State)
                .HasColumnType("enum('pending','accepted','denied')")
                .HasColumnName("state");
            entity.Property(e => e.UpdatedAt)
                .ValueGeneratedOnAddOrUpdate()
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("updated_at");

            entity.HasOne(d => d.PassUser).WithMany(p => p.FriendRequestPassUsers)
                .HasForeignKey(d => d.PassUserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_friend_requests_users2");

            entity.HasOne(d => d.SendUser).WithMany(p => p.FriendRequestSendUsers)
                .HasForeignKey(d => d.SendUserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_friend_requests_users1");
        });

        modelBuilder.Entity<Guest>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("guests");

            entity.HasIndex(e => e.SessionId, "fk_guests_sessions1_idx");

            entity.HasIndex(e => e.UserId, "fk_guests_users1_idx");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Authority)
                .HasColumnType("enum('Guest','Editor','Maintainer','Manager','Admin','Banned')")
                .HasColumnName("authority");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
            entity.Property(e => e.SessionId).HasColumnName("session_id");
            entity.Property(e => e.UserId).HasColumnName("user_id");

            entity.HasOne(d => d.Session).WithMany(p => p.Guests)
                .HasForeignKey(d => d.SessionId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_guests_sessions1");

            entity.HasOne(d => d.User).WithMany(p => p.Guests)
                .HasForeignKey(d => d.UserId)
                .HasConstraintName("fk_guests_users1");
        });

        modelBuilder.Entity<Message>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("messages");

            entity.HasIndex(e => e.UserId, "fk_messages_users1_idx");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Content)
                .HasColumnType("text")
                .HasColumnName("content");
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.IsRead).HasColumnName("is_read");
            entity.Property(e => e.Title)
                .HasMaxLength(50)
                .HasColumnName("title");
            entity.Property(e => e.UserId).HasColumnName("user_id");

            entity.HasOne(d => d.User).WithMany(p => p.Messages)
                .HasForeignKey(d => d.UserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_messages_users1");
        });

        modelBuilder.Entity<Provider>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("providers");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Request>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("requests");

            entity.HasIndex(e => e.GuestId, "fk_requests_guests1_idx");

            entity.HasIndex(e => e.SessionId, "fk_requests_sessions1_idx");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.GuestId).HasColumnName("guest_id");
            entity.Property(e => e.MusicId)
                .HasMaxLength(50)
                .HasColumnName("music_id");
            entity.Property(e => e.SessionId).HasColumnName("session_id");

            entity.HasOne(d => d.Guest).WithMany(p => p.Requests)
                .HasForeignKey(d => d.GuestId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_requests_guests1");

            entity.HasOne(d => d.Session).WithMany(p => p.Requests)
                .HasForeignKey(d => d.SessionId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_requests_sessions1");
        });

        modelBuilder.Entity<RequestCache>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("request_caches");

            entity.HasIndex(e => e.GuestId, "fk_request_caches_guests1_idx");

            entity.HasIndex(e => e.SessionId, "fk_request_caches_sessions1_idx");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.GuestId).HasColumnName("guest_id");
            entity.Property(e => e.MusicId)
                .HasMaxLength(50)
                .HasColumnName("music_id");
            entity.Property(e => e.OrderIndex).HasColumnName("order_index");
            entity.Property(e => e.SessionId).HasColumnName("session_id");

            entity.HasOne(d => d.Guest).WithMany(p => p.RequestCaches)
                .HasForeignKey(d => d.GuestId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_request_caches_guests1");

            entity.HasOne(d => d.Session).WithMany(p => p.RequestCaches)
                .HasForeignKey(d => d.SessionId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_request_caches_sessions1");
        });

        modelBuilder.Entity<Scene>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("scenes");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Session>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("sessions");

            entity.HasIndex(e => e.ProviderId, "fk_sessions_providers1_idx");

            entity.HasIndex(e => e.SceneId, "fk_sessions_scenes1_idx");

            entity.HasIndex(e => e.DefaultSortId, "fk_sessions_session_sort_settings1_idx");

            entity.HasIndex(e => e.UserId, "fk_sessions_users1_idx");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.DefaultSortId).HasColumnName("default_sort_id");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.Finished).HasColumnName("finished");
            entity.Property(e => e.FinishedAt)
                .HasColumnType("datetime")
                .HasColumnName("finished_at");
            entity.Property(e => e.ImgUrl)
                .HasMaxLength(255)
                .HasColumnName("img_url");
            entity.Property(e => e.Password)
                .HasMaxLength(255)
                .HasComment("Hashing")
                .HasColumnName("password");
            entity.Property(e => e.ProviderId).HasColumnName("provider_id");
            entity.Property(e => e.SceneId).HasColumnName("scene_id");
            entity.Property(e => e.Title)
                .HasMaxLength(50)
                .HasColumnName("title");
            entity.Property(e => e.UserId).HasColumnName("user_id");

            entity.HasOne(d => d.DefaultSort).WithMany(p => p.Sessions)
                .HasForeignKey(d => d.DefaultSortId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_sessions_session_sort_settings1");

            entity.HasOne(d => d.Provider).WithMany(p => p.Sessions)
                .HasForeignKey(d => d.ProviderId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_sessions_providers1");

            entity.HasOne(d => d.Scene).WithMany(p => p.Sessions)
                .HasForeignKey(d => d.SceneId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_sessions_scenes1");

            entity.HasOne(d => d.User).WithMany(p => p.Sessions)
                .HasForeignKey(d => d.UserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_sessions_users1");

            entity.HasMany(d => d.Tags).WithMany(p => p.Sessions)
                .UsingEntity<Dictionary<string, object>>(
                    "SessionTag",
                    r => r.HasOne<Tag>().WithMany()
                        .HasForeignKey("TagId")
                        .OnDelete(DeleteBehavior.ClientSetNull)
                        .HasConstraintName("fk_session_tags_tags1"),
                    l => l.HasOne<Session>().WithMany()
                        .HasForeignKey("SessionId")
                        .HasConstraintName("fk_session_tags_sessions1"),
                    j =>
                    {
                        j.HasKey("SessionId", "TagId")
                            .HasName("PRIMARY")
                            .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });
                        j.ToTable("session_tags");
                        j.HasIndex(new[] { "TagId" }, "fk_session_tags_tags1_idx");
                        j.IndexerProperty<int>("SessionId").HasColumnName("session_id");
                        j.IndexerProperty<int>("TagId").HasColumnName("tag_id");
                    });
        });

        modelBuilder.Entity<SessionSortSetting>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("session_sort_settings");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Description)
                .HasColumnType("text")
                .HasColumnName("description");
            entity.Property(e => e.Label)
                .HasMaxLength(50)
                .HasColumnName("label");
        });

        modelBuilder.Entity<StreetPassHistory>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("street_pass_history");

            entity.HasIndex(e => e.PassedUser1Id, "fk_street_pass_history_users1_idx");

            entity.HasIndex(e => e.PassedUser2Id, "fk_street_pass_history_users2_idx");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.Latitude).HasColumnName("latitude");
            entity.Property(e => e.Longitude).HasColumnName("longitude");
            entity.Property(e => e.PassedUser1Id).HasColumnName("passed_user1_id");
            entity.Property(e => e.PassedUser2Id).HasColumnName("passed_user2_id");

            entity.HasOne(d => d.PassedUser1).WithMany(p => p.StreetPassHistoryPassedUser1s)
                .HasForeignKey(d => d.PassedUser1Id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_street_pass_history_users1");

            entity.HasOne(d => d.PassedUser2).WithMany(p => p.StreetPassHistoryPassedUser2s)
                .HasForeignKey(d => d.PassedUser2Id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_street_pass_history_users2");
        });

        modelBuilder.Entity<StreetPassOption>(entity =>
        {
            entity.HasKey(e => e.UserId).HasName("PRIMARY");

            entity.ToTable("street_pass_options");

            entity.Property(e => e.UserId)
                .ValueGeneratedNever()
                .HasColumnName("user_id");
            entity.Property(e => e.Message)
                .HasColumnType("text")
                .HasColumnName("message");
            entity.Property(e => e.PlaylistEndpoint)
                .HasMaxLength(255)
                .HasColumnName("playlist_endpoint");
            entity.Property(e => e.SecretMode)
                .HasDefaultValueSql("'1'")
                .HasColumnName("secret_mode");

            entity.HasOne(d => d.User).WithOne(p => p.StreetPassOption)
                .HasForeignKey<StreetPassOption>(d => d.UserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_street_pass_options_users1");
        });

        modelBuilder.Entity<Tag>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("tags");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Label)
                .HasMaxLength(255)
                .HasColumnName("label");
        });

        modelBuilder.Entity<User>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PRIMARY");

            entity.ToTable("users");

            entity.HasIndex(e => e.Email, "email_UNIQUE").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Age).HasColumnName("age");
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("CURRENT_TIMESTAMP")
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.Email).HasColumnName("email");
            entity.Property(e => e.FavoriteMusic)
                .HasMaxLength(50)
                .HasColumnName("favorite_music");
            entity.Property(e => e.Gender)
                .HasDefaultValueSql("'Other'")
                .HasColumnType("enum('Male','Female','Other')")
                .HasColumnName("gender");
            entity.Property(e => e.ImgUrl)
                .HasMaxLength(255)
                .HasColumnName("img_url");
            entity.Property(e => e.IsStreetPass).HasColumnName("is_street_pass");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
            entity.Property(e => e.Password)
                .HasMaxLength(255)
                .HasComment("Hashing")
                .HasColumnName("password");
        });

        modelBuilder.Entity<UserProvider>(entity =>
        {
            entity.HasKey(e => new { e.UserId, e.ProviderId })
                .HasName("PRIMARY")
                .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

            entity.ToTable("user_providers");

            entity.HasIndex(e => e.ProviderId, "fk_user_providers_providers1_idx");

            entity.Property(e => e.UserId).HasColumnName("user_id");
            entity.Property(e => e.ProviderId).HasColumnName("provider_id");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .HasColumnName("name");
            entity.Property(e => e.Password)
                .HasMaxLength(255)
                .HasComment("Hashing")
                .HasColumnName("password");

            entity.HasOne(d => d.Provider).WithMany(p => p.UserProviders)
                .HasForeignKey(d => d.ProviderId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("fk_user_providers_providers1");

            entity.HasOne(d => d.User).WithMany(p => p.UserProviders)
                .HasForeignKey(d => d.UserId)
                .HasConstraintName("fk_user_providers_users1");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
