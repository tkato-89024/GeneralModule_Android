package jp.co.model.tkato.general_module.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class SQLiteDatabaseQueryOrganizerTest {

    // region テスト用DB作成
    // DB作成
    private DatabaseTestHelper dbHelper = new DatabaseTestHelper(getApplicationContext());
    private SQLiteDatabase db = dbHelper.getWritableDatabase();
    // 重複行を除外しないインスタンスを生成(distinctの引数による指定なし)
    private SQLiteDatabaseQueryOrganizer distinctNull = new SQLiteDatabaseQueryOrganizer(db,"tbl", null,"slctn", null,"grpby","hvng","ordby");
    // 重複行を除外しないインスタンスを生成(distinct：Falseで明示的に除外を行わないようにする)
    private SQLiteDatabaseQueryOrganizer distinctFalse = new SQLiteDatabaseQueryOrganizer(db,"tbl",false,null,"slctn", null,"grpby","hvng","ordby");
    // 重複行を除外したインスタンスを生成(distinct：True)
    private SQLiteDatabaseQueryOrganizer distinctTrue = new SQLiteDatabaseQueryOrganizer(db,"tbl",true,null,"slctn", null,"grpby","hvng","ordby");
    // データなしDB作成
    private SQLiteDatabaseQueryOrganizer dbNull = new SQLiteDatabaseQueryOrganizer(null,"tbl", null,"slctn", null,"grpby","hvng","ordby");
    // endregion テスト用DB作成

    // region ゲッター単独の動作確認
    // ゲッターの値取得ができるかのみを確認。クエリ実行関連テストは「テーブル情報取得テスト」で実施
    @SuppressWarnings("NonAsciiCharacters")
    @Test
    public void テーブル情報SQL_ゲッター動作チェック_正常系() {

        assertNotNull(distinctNull.getDb());

        assertEquals(distinctNull.getTable(),"tbl");
        assertEquals(distinctNull.getGroupBy(),"grpby");
        assertEquals(distinctNull.getHaving(),"hvng");

        assertEquals(distinctFalse.getTable(),"tbl");
        assertEquals(distinctFalse.getGroupBy(),"grpby");
        assertEquals(distinctFalse.getHaving(),"hvng");

        assertEquals(distinctTrue.getTable(),"tbl");
        assertEquals(distinctTrue.getGroupBy(),"grpby");
        assertEquals(distinctTrue.getHaving(),"hvng");

        assertFalse(distinctFalse.distinct);

        assertTrue(distinctTrue.distinct);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    public void テーブル情報SQL_ゲッター動作チェック_異常系() {

        assertNull(dbNull.getDb());

        assertNotEquals(distinctNull.getTable(),"hoge");
        assertNotEquals(distinctNull.getGroupBy(),"fuga");
        assertNotEquals(distinctNull.getHaving(),"piyo");

        assertNotEquals(distinctFalse.getTable(),"hoge");
        assertNotEquals(distinctFalse.getGroupBy(),"fuga");
        assertNotEquals(distinctFalse.getHaving(),"piyo");

        assertNotEquals(distinctTrue.getTable(),"hoge");
        assertNotEquals(distinctTrue.getGroupBy(),"fuga");
        assertNotEquals( distinctTrue.getHaving(),"piyo");
    }

    // endregion ゲッター単独の動作確認

    // region テーブル情報取得テスト
    @SuppressWarnings({"AccessStaticViaInstance", "NonAsciiCharacters"})
    @Test
    public void query_テーブル情報取得() {

        SQLiteDatabase testDb = dbHelper.getWritableDatabase();

        String tableName = dbHelper.getTableName();
        SQLiteDatabaseQueryOrganizer ok = new SQLiteDatabaseQueryOrganizer(testDb, tableName, null,"", null,"","",null);

        // テスト１：1行目のデータを1件取り出す
        Cursor c = ok.query(1,0);
        c.moveToFirst();
        if(c.moveToFirst()){
            do{
                String bookmark = c.getString(c.getColumnIndex("FruitName"));
                assertEquals(bookmark,"Apple");
            }while(c.moveToNext());
            c.moveToFirst();
        }

        // テスト２：テスト１の位置から3つカーソルを下に移動させた時(offsetの値)のデータを1件取り出す
        c = ok.query(1,3);
        if(c.moveToFirst()){
            do{
                String bookmark = c.getString(c.getColumnIndex("FruitName"));
                assertEquals(bookmark,"Grape");
            }while(c.moveToNext());
            c.moveToFirst();
        }

        // テスト３：データを取り出す件数をqueryの引数で指定しない(0にする)
        SQLiteDatabaseQueryOrganizer okNoLength = new SQLiteDatabaseQueryOrganizer(testDb, tableName, null,"_id < 2", null,"","","_id");

        c = okNoLength.query(0,0);
        c.moveToFirst();
        if(c.moveToFirst()){
            do{
                String bookmark = c.getString(c.getColumnIndex("FruitName"));
                assertEquals(bookmark,"Apple");
            }while(c.moveToNext());
            c.moveToFirst();
        }

        // テスト４：重複データを引数で指定して抽出時に除外する。全データ6件中にLemonが2件あるので、重複を除外すると5件になる　※DatabaseTestHelper参照
        SQLiteDatabaseQueryOrganizer okDistinctOn = new SQLiteDatabaseQueryOrganizer(testDb, tableName, true, new String[] {"FruitName"},"", null,null,null,null);
        c = okDistinctOn.query(0,0);
        assertEquals(c.getCount(),5);

        // テスト５：重複データを引数で指定して抽出時に除外しない
        SQLiteDatabaseQueryOrganizer okDistinctOff = new SQLiteDatabaseQueryOrganizer(testDb, tableName, false, new String[] {"FruitName"},"", null,null,null,null);
        c = okDistinctOff.query(0,0);
        assertEquals(c.getCount(),6);
    }
    // endregion テーブル情報取得テスト
}