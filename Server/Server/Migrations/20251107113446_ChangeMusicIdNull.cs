using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Server.Migrations
{
    /// <inheritdoc />
    public partial class ChangeMusicIdNull : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_user_providers_providers_provider_id",
                table: "user_providers");

            migrationBuilder.DropIndex(
                name: "IX_user_providers_provider_id",
                table: "user_providers");

            migrationBuilder.AlterColumn<string>(
                name: "music_id",
                table: "favorite_musics",
                type: "varchar(50)",
                maxLength: 50,
                nullable: true,
                oldClrType: typeof(string),
                oldType: "varchar(50)",
                oldMaxLength: 50)
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "favorite_musics",
                keyColumn: "music_id",
                keyValue: null,
                column: "music_id",
                value: "");

            migrationBuilder.AlterColumn<string>(
                name: "music_id",
                table: "favorite_musics",
                type: "varchar(50)",
                maxLength: 50,
                nullable: false,
                oldClrType: typeof(string),
                oldType: "varchar(50)",
                oldMaxLength: 50,
                oldNullable: true)
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_user_providers_provider_id",
                table: "user_providers",
                column: "provider_id");

            migrationBuilder.AddForeignKey(
                name: "FK_user_providers_providers_provider_id",
                table: "user_providers",
                column: "provider_id",
                principalTable: "providers",
                principalColumn: "id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
