## 2025/10/23
- 参考サイト
    - Entity Framework Coreを利用してMySQLに接続する
        - https://qiita.com/okayu__11/items/9f4785b1b54305cce133
    - mysql_native_passwordのMySQLが欲しいときのdocker-compose
        - https://qiita.com/k8uwall/items/92e09e74f40ba8e5f61f
    - Docker環境でMySQLに接続しようとしたら「Public Key Retrieval is not allowed」と出力された
        - https://qiita.com/yuuuka/items/2f54b07a0d8489574660
- MySQLの認証プラグインが`caching_sha2_password`だと`Pomelo.EntityFrameworkCore.MySql`が対応していないためかエラーが出る
    - `mysql-native-password`という古い方法なら行ける
    - ユーザーのプラグイン確認
        - ``` SELECT user, host, plugin FROM mysql.user WHERE user = 'jb_user'; ```
    - 接続文字列に`AllowPublicKeyRetrieval=True`を追加するとエラーが出なくなった
        - 接続設定で公開鍵取得が許可されていないと接続できないみたい

- DBの接続がcomposeからの環境変数での接続ができなかった

