using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Server.Migrations
{
    /// <inheritdoc />
    public partial class AddFriendRequestCheck : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {

            migrationBuilder.DropForeignKey(
                name: "FK_friend_requests_users_pass_user_id",
                table: "friend_requests");
            migrationBuilder.DropForeignKey(
                name: "FK_friend_requests_users_send_user_id",
                table: "friend_requests");

            migrationBuilder.DropPrimaryKey(
                name: "PK_friend_requests",
                table: "friend_requests");

            migrationBuilder.AddColumn<int>(
                name: "user1_id",
                table: "friend_requests",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AddColumn<int>(
                name: "user2_id",
                table: "friend_requests",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AddPrimaryKey(
                name: "PK_friend_requests",
                table: "friend_requests",
                columns: new[] { "user1_id", "user2_id" });

            migrationBuilder.CreateIndex(
                name: "IX_friend_requests_send_user_id",
                table: "friend_requests",
                column: "send_user_id");

            migrationBuilder.CreateIndex(
                name: "IX_friend_requests_user2_id",
                table: "friend_requests",
                column: "user2_id");

            migrationBuilder.AddForeignKey(
                name: "FK_friend_requests_users_user1_id",
                table: "friend_requests",
                column: "user1_id",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_friend_requests_users_user2_id",
                table: "friend_requests",
                column: "user2_id",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            // User1Id < User2Id のチェック制約を追加
            migrationBuilder.Sql(
                "ALTER TABLE friend_requests ADD CONSTRAINT CK_friend_requests_User1Id_LessThan_User2Id CHECK (user1_id < user2_id)"
            );
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_friend_requests_users_user1_id",
                table: "friend_requests");
            migrationBuilder.DropForeignKey(
                name: "FK_friend_requests_users_user2_id",
                table: "friend_requests");

            migrationBuilder.DropPrimaryKey(
                name: "PK_friend_requests",
                table: "friend_requests");


            migrationBuilder.DropIndex(
                name: "IX_friend_requests_send_user_id",
                table: "friend_requests");
            migrationBuilder.DropIndex(
                name: "IX_friend_requests_user2_id",
                table: "friend_requests");


            migrationBuilder.DropColumn(
                name: "user1_id",
                table: "friend_requests");
            migrationBuilder.DropColumn(
                name: "user2_id",
                table: "friend_requests");


            // User1Id < User2Id のチェック制約を削除
            migrationBuilder.Sql(
                "ALTER TABLE friend_requests DROP CONSTRAINT CK_friend_requests_User1Id_LessThan_User2Id"
            );

            migrationBuilder.AddForeignKey(
                name: "FK_friend_requests_users_pass_user_id",
                table: "friend_requests",
                column: "pass_user_id",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
            migrationBuilder.AddForeignKey(
                name: "FK_friend_requests_users_send_user_id",
                table: "friend_requests",
                column: "send_user_id",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddPrimaryKey(
                name: "PK_friend_requests",
                table: "friend_requests",
                columns: new[] { "send_user_id", "pass_user_id" });
        }
    }
}
