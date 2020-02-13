package jp.co.model.tkato.general_module.query;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@SuppressWarnings("NonAsciiCharacters")
public class AbstractQueryOrganizerTest {

    private QueryConcrete qcok = new QueryConcrete();
    private QueryConcrete qcng = new QueryConcrete();

    // region 正常系テスト
    @Test
    public void QueryOrganizer_重複行を削除する場合_正常() {

        qcok.distinct = true;
        boolean distinct = qcok.isDistinct();
        assertTrue(distinct);
    }

    @Test
    public void QueryOrganizer_重複行を削除しない場合_正常() {

        qcok.distinct = false;
        boolean distinct = qcok.isDistinct();
        assertFalse(distinct);
    }

    @Test
    public void QueryOrganizer_列の取り出し_正常() {

        String[] provalue = {"hoge","fuga","piyo"};
        qcok.projection = new String[]{"hoge","fuga","piyo"};
        String[] prjctn = qcok.getProjection();
        for(int i = 0; i < 3; i++){
            assertEquals(prjctn[i], provalue[i]);
        }
    }

    @Test
    public void QueryOrganizer_行の取り出し_正常() {

        qcok.selection = "hoge";
        assertEquals(qcok.getSelection(),"hoge");
    }

    @Test
    public void QueryOrganizer_行取り出し時のワイルドカード用配列テスト_正常() {

        String[] slctargvalue = {"hoge","fuga","piyo"};
        qcok.selectionArgs = new String[]{"hoge","fuga","piyo"};
        String[] slctarg = qcok.getSelectionArgs();
        for(int i = 0; i < 3; i++){
            assertEquals(slctarg[i], slctargvalue[i]);
        }
    }

    @Test
    public void QueryOrganizer_クエリ結果の並び替え_正常() {

        qcok.orderBy = "hoge";
        assertEquals(qcok.getOrderBy(),"hoge");
    }

    @Test
    public void QueryOrganizer_クエリ実行時に引数を指定しなかった場合の挙動テスト_正常() {

        // クエリ実行時に引数をつけなかった場合、取得データの数<length>と開始位置<offset>を自動的にデフォルトにする(すべてのデータを取り出す)
        assertEquals(qcok.query(), qcok.query(0,0));
    }
    // endregion 正常系テスト

    // region 異常系、Nullテスト
    @Test
    public void QueryOrganizer_重複行を削除しない場合_デフォルト値() {

        assertFalse(qcng.isDistinct());
    }

    @Test
    public void QueryOrganizer_列の取り出し_Null() {

        String[] projection = qcng.getProjection();
        assertNull(projection);
    }

    @Test
    public void QueryOrganizer_列の取り出し_異常() {

        String[] provalue = {"hoge","fuga","piyo"};
        qcng.projection = new String[]{"fuga","piyo","hoge"};
        String[] prjctn = qcng.getProjection();
        for(int i = 0; i < 3; i++){
            assertNotEquals(prjctn[i], provalue[i]);
        }
    }

    @Test
    public void QueryOrganizer_行の取り出し_異常() {

        qcng.selection = "hoge";
        assertNotEquals(qcng.getSelection(),"fuga");
    }

    @Test
    public void QueryOrganizer_行の取り出し_Null() {
        final String selection = qcng.getSelection();
        assertNull(selection);
    }

    @Test
    public void QueryOrganizer_行取り出し時のワイルドカード用配列テスト_異常() {

        String[] slctargvalue = {"hoge","fuga","piyo"};
        qcng.selectionArgs = new String[]{"fuga","piyo","hoge"};
        String[] slctarg = qcng.getSelectionArgs();
        for(int i = 0; i < 3; i++){
            assertNotEquals(slctarg[i], slctargvalue[i]);
        }
    }

    @Test
    public void QueryOrganizer_行取り出し時のワイルドカード用配列テスト_Null() {
        String[] slcta = qcng.getSelectionArgs();
        assertNull(slcta);
    }

    @Test
    public void QueryOrganizer_クエリ結果の並び替え_異常() {

        qcok.orderBy = "hoge";
        assertNotEquals(qcok.getOrderBy(),"fuga");
    }

    @Test
    public void QueryOrganizer_クエリ結果の並び替え_Null() {
        String orderby = qcng.getOrderBy();
        assertNull(orderby);
    }
    // endregion 異常系、Nullテスト
}