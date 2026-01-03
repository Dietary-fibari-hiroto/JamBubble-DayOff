using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Server.Migrations
{
    /// <inheritdoc />
    public partial class InitialCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "favorite_music_summaries",
                columns: table => new
                {
                    music_id = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    count = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_favorite_music_summaries", x => x.music_id);
                });

            migrationBuilder.CreateTable(
                name: "providers",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    name = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_providers", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "scenes",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    name = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_scenes", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "session_sort_settings",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    label = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    description = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_session_sort_settings", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tags",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    label = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tags", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "users",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    name = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false),
                    birthday = table.Column<DateOnly>(type: "date", nullable: false),
                    email = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    password = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    gender = table.Column<int>(type: "int", nullable: false),
                    is_street_pass = table.Column<bool>(type: "bit", nullable: false),
                    img_url = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: true),
                    message = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false),
                    updated_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_users", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "favorite_musics",
                columns: table => new
                {
                    user_id = table.Column<int>(type: "int", nullable: false),
                    music_id = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_favorite_musics", x => x.user_id);
                    table.ForeignKey(
                        name: "FK_favorite_musics_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "fornows",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    user_id = table.Column<int>(type: "int", nullable: false),
                    music_id = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    message = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    finished = table.Column<bool>(type: "bit", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_fornows", x => x.id);
                    table.ForeignKey(
                        name: "FK_fornows_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "friend_requests",
                columns: table => new
                {
                    user1_id = table.Column<int>(type: "int", nullable: false),
                    user2_id = table.Column<int>(type: "int", nullable: false),
                    send_user_id = table.Column<int>(type: "int", nullable: false),
                    pass_user_id = table.Column<int>(type: "int", nullable: false),
                    state = table.Column<int>(type: "int", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false),
                    updated_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_friend_requests", x => new { x.user1_id, x.user2_id });
                    table.ForeignKey(
                        name: "FK_friend_requests_users_pass_user_id",
                        column: x => x.pass_user_id,
                        principalTable: "users",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_friend_requests_users_send_user_id",
                        column: x => x.send_user_id,
                        principalTable: "users",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_friend_requests_users_user1_id",
                        column: x => x.user1_id,
                        principalTable: "users",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_friend_requests_users_user2_id",
                        column: x => x.user2_id,
                        principalTable: "users",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "friends",
                columns: table => new
                {
                    user1_id = table.Column<int>(type: "int", nullable: false),
                    user2_id = table.Column<int>(type: "int", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_friends", x => new { x.user1_id, x.user2_id });
                    table.ForeignKey(
                        name: "FK_friends_users_user1_id",
                        column: x => x.user1_id,
                        principalTable: "users",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_friends_users_user2_id",
                        column: x => x.user2_id,
                        principalTable: "users",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "messages",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    user_id = table.Column<int>(type: "int", nullable: false),
                    title = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    content = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    is_read = table.Column<bool>(type: "bit", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_messages", x => x.id);
                    table.ForeignKey(
                        name: "FK_messages_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "sessions",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    user_id = table.Column<int>(type: "int", nullable: false),
                    title = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    finished = table.Column<bool>(type: "bit", nullable: false),
                    finished_at = table.Column<DateTime>(type: "datetime2", nullable: true),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false),
                    provider_id = table.Column<int>(type: "int", nullable: false),
                    password = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    scene_id = table.Column<int>(type: "int", nullable: false),
                    default_sort_id = table.Column<int>(type: "int", nullable: false),
                    img_url = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    description = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    is_public = table.Column<bool>(type: "bit", nullable: false),
                    user_capacity = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_sessions", x => x.id);
                    table.ForeignKey(
                        name: "FK_sessions_providers_provider_id",
                        column: x => x.provider_id,
                        principalTable: "providers",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_sessions_scenes_scene_id",
                        column: x => x.scene_id,
                        principalTable: "scenes",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_sessions_session_sort_settings_default_sort_id",
                        column: x => x.default_sort_id,
                        principalTable: "session_sort_settings",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_sessions_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "street_pass_histories",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    passed_user1_id = table.Column<int>(type: "int", nullable: false),
                    passed_user2_id = table.Column<int>(type: "int", nullable: false),
                    latitude = table.Column<double>(type: "float", nullable: false),
                    longitude = table.Column<double>(type: "float", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_street_pass_histories", x => x.id);
                    table.ForeignKey(
                        name: "FK_street_pass_histories_users_passed_user1_id",
                        column: x => x.passed_user1_id,
                        principalTable: "users",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_street_pass_histories_users_passed_user2_id",
                        column: x => x.passed_user2_id,
                        principalTable: "users",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "street_pass_options",
                columns: table => new
                {
                    user_id = table.Column<int>(type: "int", nullable: false),
                    playlist_endpoint = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: true),
                    message = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    secret_mode = table.Column<bool>(type: "bit", nullable: false)
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
                });

            migrationBuilder.CreateTable(
                name: "user_blocks",
                columns: table => new
                {
                    user_id = table.Column<int>(type: "int", nullable: false),
                    blocked_user_id = table.Column<int>(type: "int", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_user_blocks", x => new { x.user_id, x.blocked_user_id });
                    table.ForeignKey(
                        name: "FK_user_blocks_users_blocked_user_id",
                        column: x => x.blocked_user_id,
                        principalTable: "users",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_user_blocks_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "user_histories",
                columns: table => new
                {
                    user_id = table.Column<int>(type: "int", nullable: false),
                    session_count = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_user_histories", x => x.user_id);
                    table.ForeignKey(
                        name: "FK_user_histories_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "user_providers",
                columns: table => new
                {
                    user_id = table.Column<int>(type: "int", nullable: false),
                    provider_id = table.Column<int>(type: "int", nullable: false),
                    name = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_user_providers", x => new { x.user_id, x.provider_id });
                    table.ForeignKey(
                        name: "FK_user_providers_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "fornow_likes",
                columns: table => new
                {
                    fornow_id = table.Column<int>(type: "int", nullable: false),
                    user_id = table.Column<int>(type: "int", nullable: false),
                    FornowId1 = table.Column<int>(type: "int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_fornow_likes", x => new { x.fornow_id, x.user_id });
                    table.ForeignKey(
                        name: "FK_fornow_likes_fornows_FornowId1",
                        column: x => x.FornowId1,
                        principalTable: "fornows",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_fornow_likes_fornows_fornow_id",
                        column: x => x.fornow_id,
                        principalTable: "fornows",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_fornow_likes_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "guests",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    name = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: true),
                    user_id = table.Column<int>(type: "int", nullable: true),
                    session_id = table.Column<int>(type: "int", nullable: false),
                    authority = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_guests", x => x.id);
                    table.ForeignKey(
                        name: "FK_guests_sessions_session_id",
                        column: x => x.session_id,
                        principalTable: "sessions",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_guests_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "session_invitations",
                columns: table => new
                {
                    session_id = table.Column<int>(type: "int", nullable: false),
                    user_id = table.Column<int>(type: "int", nullable: false),
                    created_at = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_session_invitations", x => new { x.session_id, x.user_id });
                    table.ForeignKey(
                        name: "FK_session_invitations_sessions_session_id",
                        column: x => x.session_id,
                        principalTable: "sessions",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_session_invitations_users_user_id",
                        column: x => x.user_id,
                        principalTable: "users",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "session_tags",
                columns: table => new
                {
                    session_id = table.Column<int>(type: "int", nullable: false),
                    tag_id = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_session_tags", x => new { x.session_id, x.tag_id });
                    table.ForeignKey(
                        name: "FK_session_tags_sessions_session_id",
                        column: x => x.session_id,
                        principalTable: "sessions",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_session_tags_tags_tag_id",
                        column: x => x.tag_id,
                        principalTable: "tags",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "request_caches",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    session_id = table.Column<int>(type: "int", nullable: false),
                    guest_id = table.Column<int>(type: "int", nullable: false),
                    music_id = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    order_index = table.Column<int>(type: "int", nullable: false),
                    SessionId1 = table.Column<int>(type: "int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_request_caches", x => x.id);
                    table.ForeignKey(
                        name: "FK_request_caches_guests_guest_id",
                        column: x => x.guest_id,
                        principalTable: "guests",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_request_caches_sessions_SessionId1",
                        column: x => x.SessionId1,
                        principalTable: "sessions",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_request_caches_sessions_session_id",
                        column: x => x.session_id,
                        principalTable: "sessions",
                        principalColumn: "id");
                });

            migrationBuilder.CreateTable(
                name: "requests",
                columns: table => new
                {
                    id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    session_id = table.Column<int>(type: "int", nullable: false),
                    guest_id = table.Column<int>(type: "int", nullable: false),
                    music_id = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    SessionId1 = table.Column<int>(type: "int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_requests", x => x.id);
                    table.ForeignKey(
                        name: "FK_requests_guests_guest_id",
                        column: x => x.guest_id,
                        principalTable: "guests",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_requests_sessions_SessionId1",
                        column: x => x.SessionId1,
                        principalTable: "sessions",
                        principalColumn: "id");
                    table.ForeignKey(
                        name: "FK_requests_sessions_session_id",
                        column: x => x.session_id,
                        principalTable: "sessions",
                        principalColumn: "id");
                });

            migrationBuilder.CreateIndex(
                name: "IX_favorite_musics_user_id",
                table: "favorite_musics",
                column: "user_id",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_fornow_likes_fornow_id_user_id",
                table: "fornow_likes",
                columns: new[] { "fornow_id", "user_id" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_fornow_likes_FornowId1",
                table: "fornow_likes",
                column: "FornowId1");

            migrationBuilder.CreateIndex(
                name: "IX_fornow_likes_user_id",
                table: "fornow_likes",
                column: "user_id");

            migrationBuilder.CreateIndex(
                name: "IX_fornows_user_id",
                table: "fornows",
                column: "user_id");

            migrationBuilder.CreateIndex(
                name: "IX_friend_requests_pass_user_id",
                table: "friend_requests",
                column: "pass_user_id");

            migrationBuilder.CreateIndex(
                name: "IX_friend_requests_send_user_id",
                table: "friend_requests",
                column: "send_user_id");

            migrationBuilder.CreateIndex(
                name: "IX_friend_requests_user2_id",
                table: "friend_requests",
                column: "user2_id");

            migrationBuilder.CreateIndex(
                name: "IX_friends_user1_id_user2_id",
                table: "friends",
                columns: new[] { "user1_id", "user2_id" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_friends_user2_id",
                table: "friends",
                column: "user2_id");

            migrationBuilder.CreateIndex(
                name: "IX_guests_session_id",
                table: "guests",
                column: "session_id");

            migrationBuilder.CreateIndex(
                name: "IX_guests_user_id",
                table: "guests",
                column: "user_id");

            migrationBuilder.CreateIndex(
                name: "IX_messages_user_id",
                table: "messages",
                column: "user_id");

            migrationBuilder.CreateIndex(
                name: "IX_request_caches_guest_id",
                table: "request_caches",
                column: "guest_id");

            migrationBuilder.CreateIndex(
                name: "IX_request_caches_session_id",
                table: "request_caches",
                column: "session_id");

            migrationBuilder.CreateIndex(
                name: "IX_request_caches_SessionId1",
                table: "request_caches",
                column: "SessionId1");

            migrationBuilder.CreateIndex(
                name: "IX_requests_guest_id",
                table: "requests",
                column: "guest_id");

            migrationBuilder.CreateIndex(
                name: "IX_requests_session_id",
                table: "requests",
                column: "session_id");

            migrationBuilder.CreateIndex(
                name: "IX_requests_SessionId1",
                table: "requests",
                column: "SessionId1");

            migrationBuilder.CreateIndex(
                name: "IX_session_invitations_session_id_user_id",
                table: "session_invitations",
                columns: new[] { "session_id", "user_id" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_session_invitations_user_id",
                table: "session_invitations",
                column: "user_id");

            migrationBuilder.CreateIndex(
                name: "IX_session_tags_session_id_tag_id",
                table: "session_tags",
                columns: new[] { "session_id", "tag_id" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_session_tags_tag_id",
                table: "session_tags",
                column: "tag_id");

            migrationBuilder.CreateIndex(
                name: "IX_sessions_default_sort_id",
                table: "sessions",
                column: "default_sort_id");

            migrationBuilder.CreateIndex(
                name: "IX_sessions_provider_id",
                table: "sessions",
                column: "provider_id");

            migrationBuilder.CreateIndex(
                name: "IX_sessions_scene_id",
                table: "sessions",
                column: "scene_id");

            migrationBuilder.CreateIndex(
                name: "IX_sessions_user_id",
                table: "sessions",
                column: "user_id");

            migrationBuilder.CreateIndex(
                name: "IX_street_pass_histories_passed_user1_id",
                table: "street_pass_histories",
                column: "passed_user1_id");

            migrationBuilder.CreateIndex(
                name: "IX_street_pass_histories_passed_user2_id",
                table: "street_pass_histories",
                column: "passed_user2_id");

            migrationBuilder.CreateIndex(
                name: "IX_user_blocks_blocked_user_id",
                table: "user_blocks",
                column: "blocked_user_id");

            migrationBuilder.CreateIndex(
                name: "IX_user_blocks_user_id_blocked_user_id",
                table: "user_blocks",
                columns: new[] { "user_id", "blocked_user_id" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_user_histories_user_id",
                table: "user_histories",
                column: "user_id",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_user_providers_user_id_provider_id",
                table: "user_providers",
                columns: new[] { "user_id", "provider_id" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_users_email",
                table: "users",
                column: "email",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "favorite_music_summaries");

            migrationBuilder.DropTable(
                name: "favorite_musics");

            migrationBuilder.DropTable(
                name: "fornow_likes");

            migrationBuilder.DropTable(
                name: "friend_requests");

            migrationBuilder.DropTable(
                name: "friends");

            migrationBuilder.DropTable(
                name: "messages");

            migrationBuilder.DropTable(
                name: "request_caches");

            migrationBuilder.DropTable(
                name: "requests");

            migrationBuilder.DropTable(
                name: "session_invitations");

            migrationBuilder.DropTable(
                name: "session_tags");

            migrationBuilder.DropTable(
                name: "street_pass_histories");

            migrationBuilder.DropTable(
                name: "street_pass_options");

            migrationBuilder.DropTable(
                name: "user_blocks");

            migrationBuilder.DropTable(
                name: "user_histories");

            migrationBuilder.DropTable(
                name: "user_providers");

            migrationBuilder.DropTable(
                name: "fornows");

            migrationBuilder.DropTable(
                name: "guests");

            migrationBuilder.DropTable(
                name: "tags");

            migrationBuilder.DropTable(
                name: "sessions");

            migrationBuilder.DropTable(
                name: "providers");

            migrationBuilder.DropTable(
                name: "scenes");

            migrationBuilder.DropTable(
                name: "session_sort_settings");

            migrationBuilder.DropTable(
                name: "users");
        }
    }
}
