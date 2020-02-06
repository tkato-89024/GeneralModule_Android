package jp.co.model.tkato.general_module.query;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SuppressWarnings("ConstantConditions")
public class SQLiteDatabaseQueryOrganizerTest {

    // region テスト用DB作成
    // DB作成
    private DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
    private SQLiteDatabase db = dbHelper.getWritableDatabase();
    private SQLiteDatabaseQueryOrganizer distinctNull = new SQLiteDatabaseQueryOrganizer(db,"tbl", null,"slctn", null,"grpby","hvng","ordby");
    private SQLiteDatabaseQueryOrganizer distinctFalse = new SQLiteDatabaseQueryOrganizer(db,"tbl",false,null,"slctn", null,"grpby","hvng","ordby");
    private SQLiteDatabaseQueryOrganizer distinctTrue = new SQLiteDatabaseQueryOrganizer(db,"tbl",true,null,"slctn", null,"grpby","hvng","ordby");
    // データなしDB作成
    private SQLiteDatabaseQueryOrganizer dbNull = new SQLiteDatabaseQueryOrganizer(null,"tbl", null,"slctn", null,"grpby","hvng","ordby");
    // endregion テスト用DB作成

    // region テーブル情報取得テスト
    @SuppressWarnings("NonAsciiCharacters")
    @Test
    public void テーブル情報取得_Test_OK() {

        assertNotNull(distinctNull.getDb());

        assertEquals("tbl", distinctNull.getTable());
        assertEquals("grpby",distinctNull.getGroupBy());
        assertEquals("hvng",distinctNull.getHaving());

        assertEquals("tbl", distinctFalse.getTable());
        assertEquals("grpby",distinctFalse.getGroupBy());
        assertEquals("hvng",distinctFalse.getHaving());

        assertEquals("tbl", distinctTrue.getTable());
        assertEquals("grpby",distinctTrue.getGroupBy());
        assertEquals("hvng",distinctTrue.getHaving());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    public void テーブル情報取得_Test_NG() {

        assertNull(dbNull.getDb());

        assertNotEquals("hoge", distinctNull.getTable());
        assertNotEquals("fuga",distinctNull.getGroupBy());
        assertNotEquals("piyo",distinctNull.getHaving());

        assertNotEquals("hoge",distinctFalse.getTable());
        assertNotEquals("fuga",distinctFalse.getGroupBy());
        assertNotEquals("piyo", distinctFalse.getHaving());

        assertNotEquals("hoge",distinctTrue.getTable());
        assertNotEquals("fuga",distinctTrue.getGroupBy());
        assertNotEquals("piyo", distinctTrue.getHaving());
    }

    @Test
    public void query_Test() {

        SQLiteDatabaseQueryOrganizer ok = new SQLiteDatabaseQueryOrganizer(db,"MT_ITEM", null,"", null,"","",null);
        ok.query(2,1);
        ok.query(0,2);

        db.close();
    }

    // endregion テーブル情報取得テスト
}