using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Server.Migrations
{
    /// <inheritdoc />
    public partial class AddFriendCheck : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            // User1Id < User2Id のチェック制約を追加
            migrationBuilder.Sql(
                "ALTER TABLE friends ADD CONSTRAINT CK_friends_User1Id_LessThan_User2Id CHECK (user1_id < user2_id)"
            );
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            // User1Id < User2Id のチェック制約を削除
            migrationBuilder.Sql(
                "ALTER TABLE friends DROP CONSTRAINT CK_friends_User1Id_LessThan_User2Id"
            );
        }
    }
}
