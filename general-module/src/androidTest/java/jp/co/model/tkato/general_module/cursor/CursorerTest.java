package jp.co.model.tkato.general_module.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import java.util.Objects;

import jp.co.model.tkato.general_module.query.DatabaseTestHelper;
import jp.co.model.tkato.general_module.query.IQueryOrganizer;
import jp.co.model.tkato.general_module.query.SQLiteDatabaseQueryOrganizer;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("NonAsciiCharacters")
public class CursorerTest {

    // region テスト用データ作成

    private Cursorer cs = new Cursorer();
    private Cursorer csnul = new Cursorer();

    // テスト用DB作成
    private DatabaseTestHelper dbHelper = new DatabaseTestHelper(getApplicationContext());
    private SQLiteDatabase db = dbHelper.getWritableDatabase();
    private SQLiteDatabaseQueryOrganizer ok = new SQLiteDatabaseQueryOrganizer(db,"MT_ITEM", null,"", null,"","",null);

    private IQueryOrganizer iq = new IQueryOrganizer() {

        @Override
        public Cursor query() {
            return null;
        }

        @Override
        public Cursor query(long length, long offset) {
            return null;
        }
    };

    private Cursorer newcs = new Cursorer(iq);

    // endregion テスト用データ作成

    // region 正常系テスト

    @Test
    public void Cursorer_Test_正常系(){

        // TODO:Cursorer.javaのorganizerをprivateにした際に修正

        /*cs.organizer = iq;
        assertEquals(cs.getOrganizer(), iq);

        cs.cursor = ok.query();
        assertEquals(cs.getCursor(), cs.cursor);

        int countval = Objects.requireNonNull(cs.cursor).getCount();
        assertEquals(cs.getCountByCursor(), countval);
        assertEquals(cs.getCountNewCursor(iq), countval);

        newcs.getCountNewCursor(iq);
        newcs.getCountByCursor();

        assertEquals(newcs.query(), newcs.query(0,0));

        db.close();*/
    }

    // endregion 正常系テスト

    // region 異常系テスト

    @SuppressWarnings("ConstantConditions")
    @Test
    public void Cursorer_Test_異常系() {

        assertEquals(cs.getCountNewCursor(null),-1);

        assertEquals(csnul.getCountByCursor(),-1);

        Cursorer sm = new Cursorer(null);
        assertEquals(sm.query(), sm);

        db.close();
    }

    // endregion 異常系テスト
}