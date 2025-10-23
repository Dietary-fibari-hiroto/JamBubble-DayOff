## 2025/10/23
- 参考サイト
    - Entity Framework Coreを利用してMySQLに接続する
        - https://qiita.com/okayu__11/items/9f4785b1b54305cce133
    - mysql_native_passwordのMySQLが欲しいときのdocker-compose
        - https://qiita.com/k8uwall/items/92e09e74f40ba8e5f61f
    - 
- MySQLの認証プラグインが`caching_sha2_password`だと`Pomelo.EntityFrameworkCore.MySql`が対応していないためかエラーが出る
    - `mysql-native-password`という古い方法なら行ける
    - ユーザーのプラグイン確認
        - ``` SELECT user, host, plugin FROM mysql.user WHERE user = 'jb_user'; ```
- DBの接続がcomposeからの環境変数での接続ができなかった

