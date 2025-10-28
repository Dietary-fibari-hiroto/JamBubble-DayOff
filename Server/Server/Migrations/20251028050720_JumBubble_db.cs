using System;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Server.Migrations
{
    /// <inheritdoc />
    public partial class JumBubble_db : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "street_pass_histories",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    passed_user1_id = table.Column<int>(type: "int", nullable: false),
                    passed_user2_id = table.Column<int>(type: "int", nullable: false),
                    latitude = table.Column<double>(type: "double", nullable: false),
                    longitude = table.Column<double>(type: "double", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime(6)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_street_pass_histories", x => x.id);
                    table.ForeignKey(
                        name: "FK_street_pass_histories_users_passed_user1_id",
                        column: x => x.passed_user1_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_street_pass_histories_users_passed_user2_id",
                        column: x => x.passed_user2_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "street_pass_options",
                columns: table => new
                {
                    user_id = table.Column<int>(type: "int", nullable: false),
                    playlist_endpoint = table.Column<string>(type: "varchar(255)", maxLength: 255, nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    message = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    secret_mode = table.Column<bool>(type: "tinyint(1)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_street_pass_options", x => x.user_id);
                    table.ForeignKey(
                        name: "FK_street_pass_options_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_street_pass_histories_passed_user1_id",
                table: "street_pass_histories",
                column: "passed_user1_id");

            migrationBuilder.CreateIndex(
                name: "IX_street_pass_histories_passed_user2_id",
                table: "street_pass_histories",
                column: "passed_user2_id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "street_pass_histories");

            migrationBuilder.DropTable(
                name: "street_pass_options");
        }
    }
}
