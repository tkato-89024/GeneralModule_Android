package jp.co.model.tkato.general_module.query;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@SuppressWarnings("NonAsciiCharacters")
public class AbstractQueryOrganizerTest {

    private AbstractQueryOrganizerTestHelper qcNg = new AbstractQueryOrganizerTestHelper();

    // region テスト用DB作成
    // DB作成
    private DatabaseTestHelper dbHelper = new DatabaseTestHelper(getApplicationContext());
    private SQLiteDatabase db = dbHelper.getWritableDatabase();
    // endregion テスト用DB作成

    // region 正常系テスト
    @Test
    public void QueryOrganizer_重複行を除外する場合_正常() {
        
        AbstractQueryOrganizerTestHelper qcDistinctTrue = new AbstractQueryOrganizerTestHelper();
        qcDistinctTrue.setDistinct(true);
        assertTrue(qcDistinctTrue.isDistinct());
    }

    @Test
    public void QueryOrganizer_重複行を除外しない場合_正常() {
        
        AbstractQueryOrganizerTestHelper qcDistinctFalse = new AbstractQueryOrganizerTestHelper();
        qcDistinctFalse.setDistinct(false);
        assertFalse(qcDistinctFalse.isDistinct());
    }

    @Test
    public void QueryOrganizer_列の取り出し_正常() {

        AbstractQueryOrganizerTestHelper qcOk = new AbstractQueryOrganizerTestHelper();

        String[] projectionValue = {"hoge","fuga","piyo"};
        qcOk.setProjection(projectionValue);
        String[] prjctn = qcOk.getProjection();
        for(int i = 0; i < 3; i++){
            assertEquals(prjctn[i], projectionValue[i]);
        }
    }

    @Test
    public void QueryOrganizer_行の取り出し_正常() {

        AbstractQueryOrganizerTestHelper qcOk = new AbstractQueryOrganizerTestHelper();
        qcOk.setSelection("hoge");
        assertEquals(qcOk.getSelection(),"hoge");
    }

    @Test
    public void QueryOrganizer_行取り出し時のワイルドカード用配列テスト_正常() {

        AbstractQueryOrganizerTestHelper qcOk = new AbstractQueryOrganizerTestHelper();
        String[] slctargValue = {"hoge","fuga","piyo"};
        qcOk.setSelectionArgs(slctargValue);
        String[] slctarg = qcOk.getSelectionArgs();
        for(int i = 0; i < 3; i++){
            assertEquals(slctarg[i], slctargValue[i]);
        }
    }

    @Test
    public void QueryOrganizer_クエリ結果の並び替え_正常() {

        AbstractQueryOrganizerTestHelper qcOk = new AbstractQueryOrganizerTestHelper();

        qcOk.setOrderBy("hoge");
        assertEquals(qcOk.getOrderBy(),"hoge");
    }

    @Test
    public void QueryOrganizer_クエリ実行時に引数を指定しなかった場合の挙動テスト_正常() {

        AbstractQueryOrganizerTestHelper qcOk = new AbstractQueryOrganizerTestHelper();

        // クエリ実行時に引数をつけなかった場合、取得データの数<length>と開始位置<offset>を自動的にデフォルトにする(すべてのデータを取り出す)
        assertEquals(qcOk.query(), qcOk.query(0,0));
    }
    // endregion 正常系テスト

    // region 異常系、Nullテスト
    @Test
    public void QueryOrganizer_重複行を除外しない場合_デフォルト値() {

        assertFalse(qcNg.isDistinct());
    }

    @Test
    public void QueryOrganizer_列の取り出し_Null() {

        String[] projection = qcNg.getProjection();
        assertNull(projection);
    }

    @Test
    public void QueryOrganizer_列の取り出し_異常() {

        String[] provalue = {"hoge","fuga","piyo"};
        qcNg.projection = new String[]{"fuga","piyo","hoge"};
        String[] prjctn = qcNg.getProjection();
        for(int i = 0; i < 3; i++){
            assertNotEquals(prjctn[i], provalue[i]);
        }
    }

    @Test
    public void QueryOrganizer_行の取り出し_異常() {

        qcNg.selection = "hoge";
        assertNotEquals(qcNg.getSelection(),"fuga");
    }

    @Test
    public void QueryOrganizer_行の取り出し_Null() {
        final String selection = qcNg.getSelection();
        assertNull(selection);
    }

    @Test
    public void QueryOrganizer_行取り出し時のワイルドカード用配列テスト_異常() {

        String[] slctargvalue = {"hoge","fuga","piyo"};
        qcNg.selectionArgs = new String[]{"fuga","piyo","hoge"};
        String[] slctarg = qcNg.getSelectionArgs();
        for(int i = 0; i < 3; i++){
            assertNotEquals(slctarg[i], slctargvalue[i]);
        }
    }

    @Test
    public void QueryOrganizer_行取り出し時のワイルドカード用配列テスト_Null() {
        String[] slcta = qcNg.getSelectionArgs();
        assertNull(slcta);
    }

    @Test
    public void QueryOrganizer_クエリ結果の並び替え_異常() {

        AbstractQueryOrganizerTestHelper qcOk = new AbstractQueryOrganizerTestHelper();

        qcOk.orderBy = "hoge";
        assertNotEquals(qcOk.getOrderBy(),"fuga");
    }

    @Test
    public void QueryOrganizer_クエリ結果の並び替え_Null() {
        String orderby = qcNg.getOrderBy();
        assertNull(orderby);
    }
    // endregion 異常系、Nullテスト
}