using System;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Server.Migrations
{
    /// <inheritdoc />
    public partial class FixOneToOneRelationships : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_favorite_musics_users_user_id1",
                table: "favorite_musics");

            migrationBuilder.DropForeignKey(
                name: "FK_user_histories_users_user_id1",
                table: "user_histories");

            migrationBuilder.DropIndex(
                name: "IX_user_histories_user_id1",
                table: "user_histories");

            migrationBuilder.DropIndex(
                name: "IX_favorite_musics_user_id1",
                table: "favorite_musics");

            migrationBuilder.DropColumn(
                name: "user_id1",
                table: "user_histories");

            migrationBuilder.DropColumn(
                name: "user_id1",
                table: "favorite_musics");

            migrationBuilder.AlterColumn<int>(
                name: "user_id",
                table: "user_histories",
                type: "int",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int")
                .OldAnnotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AlterColumn<int>(
                name: "user_id",
                table: "favorite_musics",
                type: "int",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int")
                .OldAnnotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AddForeignKey(
                name: "FK_favorite_musics_users_user_id",
                table: "favorite_musics",
                column: "user_id",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_user_histories_users_user_id",
                table: "user_histories",
                column: "user_id",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_favorite_musics_users_user_id",
                table: "favorite_musics");

            migrationBuilder.DropForeignKey(
                name: "FK_user_histories_users_user_id",
                table: "user_histories");

            migrationBuilder.AlterColumn<int>(
                name: "user_id",
                table: "user_histories",
                type: "int",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int")
                .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AddColumn<int>(
                name: "user_id1",
                table: "user_histories",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AlterColumn<int>(
                name: "user_id",
                table: "favorite_musics",
                type: "int",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int")
                .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AddColumn<int>(
                name: "user_id1",
                table: "favorite_musics",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_user_histories_user_id1",
                table: "user_histories",
                column: "user_id1");

            migrationBuilder.CreateIndex(
                name: "IX_favorite_musics_user_id1",
                table: "favorite_musics",
                column: "user_id1");

            migrationBuilder.AddForeignKey(
                name: "FK_favorite_musics_users_user_id1",
                table: "favorite_musics",
                column: "user_id1",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_user_histories_users_user_id1",
                table: "user_histories",
                column: "user_id1",
                principalTable: "users",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
