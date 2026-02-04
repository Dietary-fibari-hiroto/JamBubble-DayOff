<p align="center">
  <img src="DayoffWhiteLogo.png" alt="DayOff Logo" width="300"/>
</p>

<p align="center">
  <img src="jumbubblelogo.png" alt="JamBubble Logo" width="200"/>
</p>

# JamBubble-DayOff

「車内でひとつのプレイリストを、"みんなで奏でる" 体験」を提供するシステム&アプリケーション

## 🚀 プロジェクト概要

**JamBubble**は、友達やサークル仲間とのドライブや、宅飲み、ホームパーティーなど、少人数での音楽体験をより楽しく、共有できるようにするためのアプリです。

通常、車のBluetoothスピーカーに接続できる端末は一台だけなので、音楽の選曲はその端末の所有者に偏りがちです。JamBubbleでは、スピーカーに接続しているホスト端末からQRコードやリンクを発行し、ゲストはログイン不要でブラウザからアクセスするだけで、自分の聴きたい曲をプレイリストにリクエストできます。

## 📱 Android Client セットアップ

### 必要な環境

- Android Studio (最新版推奨)
- Android SDK
- JDK 17以上

### セットアップ手順

1. **リポジトリをクローン**
   ```bash
   git clone https://github.com/Dietary-fibari-hiroto/JamBubble-DayOff.git
   ```

2. **Android Studioでプロジェクトを開く**
   - Android Studioを起動
   - `Open` → クローンしたリポジトリ内の `AndroidClient` フォルダを選択

3. **`local.properties` を設定**
   
   プロジェクトルート（`AndroidClient/`直下）に `local.properties` ファイルを作成し、以下の項目を設定してください：

   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   
   # API Base URL
   BASE_URL=https://your-server-url.com
   
   # 初期ログイン用（開発・テスト用）
   INIT_LOGIN_EMAIL_SPATH=your-test-email@example.com
   INIT_LOGIN_PASS_SPATH=your-test-password
   
   # 認証トークン
   ACCESS_TOKEN=your-access-token
   
   # Spotify API 設定
   SPOTIFY_ACCESS_TOKEN=your-spotify-access-token
   SPOTIFY_REFRESH_TOKEN=your-spotify-refresh-token
   SPOTIFY_CLIENT_ID=your-spotify-client-id
   ```

   | プロパティ | 説明 |
   |-----------|------|
   | `sdk.dir` | Android SDKのパス |
   | `BASE_URL` | バックエンドサーバーのURL |
   | `INIT_LOGIN_EMAIL_SPATH` | テスト用ログインメールアドレス |
   | `INIT_LOGIN_PASS_SPATH` | テスト用ログインパスワード |
   | `ACCESS_TOKEN` | API認証用アクセストークン |
   | `SPOTIFY_ACCESS_TOKEN` | Spotify APIアクセストークン |
   | `SPOTIFY_REFRESH_TOKEN` | Spotify APIリフレッシュトークン |
   | `SPOTIFY_CLIENT_ID` | Spotify Developer Client ID |

4. **プロジェクトをビルド・実行**
   - Gradle Syncが完了したら、エミュレータまたは実機で実行

### ⚠️ サーバーについて

バックエンドサーバーはAzure上にデプロイ済みです。基本的にローカルでサーバーを立ち上げる必要はありません。

> **Note:** コスト節約のため、サーバーが停止状態になっている場合があります。その際はお知らせください。<br/>
> 連絡先(Instagram):https://www.instagram.com/derara1185ba/

## 🛠 技術スタック

### Android Client
- **言語:** Kotlin
- **UI:** Jetpack Compose
- **通信:** SignalR, Retrofit

### Server
- **フレームワーク:** ASP.NET Core
- **リアルタイム通信:** SignalR
- **デプロイ:** Azure Container Apps

### Web Client
- **フレームワーク:** BlazorServer

## 📂 プロジェクト構成

```
JamBubble-DayOff/
├── AndroidClient/     # Androidネイティブアプリ (Kotlin)
├── Server/            # ASP.NET Core バックエンド + Blazor
├── Sql/               # データベーススキーマ
└── jambubble-official/ # 公式サイト
```

## 📝 ライセンス

このプロジェクトはプライベートリポジトリです。

---

<p align="center">
  <b>🎵 みんなで一緒に作る音楽体験 🎵</b>
</p>
