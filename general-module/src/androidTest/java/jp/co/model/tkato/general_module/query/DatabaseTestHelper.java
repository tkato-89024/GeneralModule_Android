package jp.co.model.tkato.general_module.query;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseTestHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "FRUIT_ITEM";

    // コンストラクタ
    public DatabaseTestHelper(Context context){
        super(context,TABLE_NAME, null, 1);
    }

    // ゲッター
    @SuppressWarnings("WeakerAccess")
    public static String getTableName() {
        return TABLE_NAME;
    }

    // region テスト用DB作成クラス
    // テスト用DBにデータを挿入する
    /* ●テスト用テーブル(主キー：id)
     *　 _______________
     *  ｜id　|　text　 |
     *　｜----+---------|
     *　｜ 1　|　Apple　|
     *　｜ 2　|　Banana |
     *　｜ 3　|　Lemon  |
     *　｜ 4　|　Grape  |
     *　｜ 5　|　Orange |
     *　｜ 6　|　Lemon  |　※重複確認用の行
     * 　---------------
     * */
    @Override
    public void onCreate(SQLiteDatabase db){

        db.beginTransaction();

        // DB作成
        // データベース定義
        String DB_ITEM_CREATE = "Create table "+ TABLE_NAME + "("
                + "_id integer primary key autoincrement"   // 1列目(主キー)に番号を割り振る
                //+ ",NdistinctNulle text not null)";
                + ",FruitName text not null)";
        // Create文を実行
        db.execSQL(DB_ITEM_CREATE);
        insertTestDB(db);
    }

    // 作成したDBのテーブルに行を追加する
    private void insertTestDB(SQLiteDatabase db){
        try{
            SQLiteStatement stmt = db.compileStatement("Insert into " + TABLE_NAME + " values (?,?);");
            String[] array = {"Apple","Banana","Lemon","Grape","Orange","Lemon"};
            for(String s : array){
                stmt.bindString(2, s);
                stmt.executeInsert();
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //データベースのバージョンが変わる場合に呼び出される。
        //今回は空実装
    }
    // endregion テスト用DB作成クラス
}
