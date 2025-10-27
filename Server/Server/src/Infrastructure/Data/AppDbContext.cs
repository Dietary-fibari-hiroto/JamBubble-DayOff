using Microsoft.EntityFrameworkCore;
using Server.src.Entities;
using System.Text;
namespace Server.Data
{
    /**
     * すべてのEntityとDBテーブルの対応関係を管理する
     * 
     * dotnet ef migrations add JumBubble
     * dotnet ef database update
     * 
     * dotnet ef database drop
     * dotnet ef database update
     */
    public class AppDbContext:DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<User> Users => Set<User>();

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
            base.OnModelCreating(modelBuilder);


            //複合キーの設定
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
