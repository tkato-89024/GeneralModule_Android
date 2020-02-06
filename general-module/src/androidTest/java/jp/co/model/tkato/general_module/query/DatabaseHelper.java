package jp.co.model.tkato.general_module.query;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //コンストラクタ
    DatabaseHelper(Context context){
        super(context,"DB_TEST", null, 1);
    }

    // region テスト用DB作成クラス
    @Override
    public void onCreate(SQLiteDatabase db){
        //DB作成
        //データベース定義
        String DB_ITEM = "Create table MT_ITEM ("
                + "id integer primary key"
                + ",NdistinctNulle text not null)";
        db.execSQL(DB_ITEM);
        db.execSQL("Insert into MT_ITEM values(1 ,'Apple')");
        db.execSQL("Insert into MT_ITEM values(2,'Banana')");
        db.execSQL("Insert into MT_ITEM values(3,'Lemon')");
        db.execSQL("Insert into MT_ITEM values(4,'Grape')");
        db.execSQL("Insert into MT_ITEM values(5,'Orange')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //データベースのバージョンが変わる場合に呼び出される。
        //今回は空実装
    }
    // endregion テスト用DB作成クラス
}
