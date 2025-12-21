using Microsoft.EntityFrameworkCore;
using Server.src.Entities;
using System.Text;
namespace Server.Data
{
    /**
     * すべてのEntityとDBテーブルの対応関係を管理する
     * 
     * dotnet ef migrations add JamBubble
     * dotnet ef database update
     * 
     * dotnet ef database drop
     * dotnet ef database update
     */
    public class AppDbContext:DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<User> Users => Set<User>();
        public DbSet<Provider> Providers => Set<Provider>();
        public DbSet<UserProvider> UserProviders => Set<UserProvider>();
        public DbSet<UserHistory> UserHistories => Set<UserHistory>();
        public DbSet<FavoriteMusic> FavoriteMusics => Set<FavoriteMusic>();
        public DbSet<UserBlock> UserBlocks => Set<UserBlock>();

        public DbSet<Friend> Friends => Set<Friend>();
        public DbSet<FriendRequest> FriendRequests => Set<FriendRequest>();

        public DbSet<Session> Sessions => Set<Session>();
        public DbSet<Tag> Tags => Set<Tag>();
        public DbSet<SessionTag> SessionTags => Set<SessionTag>();
        public DbSet<Scene> Scenes => Set<Scene>();
        public DbSet<SessionSortSetting> SessionSortSettings => Set<SessionSortSetting>();
        public DbSet<SessionInvitation> SessionInvitations => Set<SessionInvitation>();
        public DbSet<Guest> Guests => Set<Guest>();
        public DbSet<Request> Requests => Set<Request>();
        public DbSet<RequestCache> RequestCaches => Set<RequestCache>();

        public DbSet<StreetPassOption> StreetPassOptions => Set<StreetPassOption>();
        public DbSet<StreetPassHistory> StreetPassHistories => Set<StreetPassHistory>();
        
        public DbSet<Fornow> Fornows=> Set<Fornow>();
        public DbSet<FornowLike> FornowLikes => Set<FornowLike>();

        public DbSet<Message>Messages => Set<Message>();

        public DbSet<FavoriteMusicSummary> FavoriteMusicSummaries => Set<FavoriteMusicSummary>();


    

        //Entityの構成を定義するメソッド
        protected override void OnModelCreating(ModelBuilder modelBuilder) {
            //テーブル名とカラム名を生成する際の追加処理
            foreach (var entity in modelBuilder.Model.GetEntityTypes())
            {
                //テーブル名を小文字+スネークケースに変換
                entity.SetTableName(ToSnakeCase(entity.GetTableName()!));

                //カラム名も小文字+スネークケース
                foreach (var property in entity.GetProperties())
                {
                    property.SetColumnName(ToSnakeCase(property.Name));
                }

            }
            //元のスーパークラスのないようも実行
            base.OnModelCreating(modelBuilder);


            //複合キーの定義
            modelBuilder.Entity<UserProvider>().HasKey(p => new { p.UserId, p.ProviderId });
            modelBuilder.Entity<Friend>().HasKey(p => new { p.User1Id, p.User2Id });
            modelBuilder.Entity<FriendRequest>().HasKey(p => new { p.User1Id, p.User2Id });
            modelBuilder.Entity<UserBlock>().HasKey( p=> new {p.UserId,p.BlockedUserId});

            modelBuilder.Entity<SessionTag>().HasKey(p => new { p.SessionId, p.TagId });
            modelBuilder.Entity<SessionInvitation>().HasKey(p => new { p.SessionId, p.UserId });

            modelBuilder.Entity<FornowLike>().HasKey(p => new { p.FornowId, p.UserId });

            // カスケード削除の設定
            modelBuilder.Entity<User>()
                .HasOne(u => u.UserHistory)
                .WithOne(h => h.User)
                .HasForeignKey<UserHistory>(h => h.UserId)
                .OnDelete(DeleteBehavior.Cascade);
            modelBuilder.Entity<User>()
                .HasOne(u => u.FavoriteMusic)
                .WithOne(f => f.User)
                .HasForeignKey<FavoriteMusic>(f => f.UserId)
                .OnDelete(DeleteBehavior.Cascade);
            modelBuilder.Entity<User>()
                .HasMany(u => u.UserProviders)
                .WithOne(up => up.User)
                .HasForeignKey(p => p.UserId)
                .OnDelete(DeleteBehavior.Cascade);
            modelBuilder.Entity<User>()
                .HasMany(u => u.Messages)
                .WithOne(m => m.User)
                .HasForeignKey(p => p.UserId)
                .OnDelete(DeleteBehavior.Cascade);

            // ユニーク制約の定義
            modelBuilder.Entity<User>().HasIndex(u => u.Email).IsUnique();

            // チェック制約の定義
            // .HasCheckConstraintは非推奨になっており、マイグレーション上で生のSQLで追加する方法が推奨されている
            //modelBuilder.Entity<FriendRequest>().HasCheckConstraint("CK_FriendRequest_User1Id_User2Id", "User1Id < User2Id");
        }

        //INSERTやUPDATEでDBに反映させるタイミングで呼び出される関数
        public override int SaveChanges()
        {
            UpdateTimestamps();
            return base.SaveChanges();
        }

        //INSERTやUPDATEでDBに反映させるタイミングで呼び出される関数
        public override Task<int> SaveChangesAsync(CancellationToken cancellationToken = default)
        {
            UpdateTimestamps();
            return base.SaveChangesAsync(cancellationToken);
        }

        //作成日と更新日の値を更新するための関数
        public void UpdateTimestamps()
        {
            var entries = ChangeTracker.Entries<TimestampedEntity>();
            var now = DateTime.UtcNow;

            foreach(var entry in entries)
            {
                if(entry.State == EntityState.Added)
                {
                    entry.Entity.CreatedAt = now;
                    entry.Entity.UpdatedAt = now;
                } else if(entry.State == EntityState.Modified){
                    entry.Entity.UpdatedAt = now;
                }
            }
        }


        //パスカルケースをスネークケースに変換するヘルパー関数
        private static string ToSnakeCase(string input)
        {
            //nullはそのまま返す
            if (string.IsNullOrEmpty(input)) return input;

            var sb = new StringBuilder(); //stringと違って可変。変更があったときに同じメモリ領域上で文字列を変更し、新しいインスタンスは作らない
            //文字列を一文づつ処理
            for(int i = 0; i < input.Length; i++)
            {
                char c = input[i];
                //大文字なら、戦闘以外はアンダースコアを付けて小文字に変換
                if (char.IsUpper(c))//大文字判定
                {
                    if (i > 0) sb.Append('_');
                    sb.Append(char.ToLower(c));//小文字に変換
                }
                else
                {
                    sb.Append(c);
                }
            }
            return sb.ToString();
        }
    }


}
