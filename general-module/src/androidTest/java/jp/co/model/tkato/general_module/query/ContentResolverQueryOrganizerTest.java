package jp.co.model.tkato.general_module.query;

import android.content.Context;

import org.junit.Test;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static jp.co.model.tkato.general_module.query.ContentResolverQueryOrganizer.initialize;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ContentResolverQueryOrganizerTest {
    private Context ct = getApplicationContext();

    // region OK case
    @SuppressWarnings({"NonAsciiCharacters"})
    @Test
    public void テーブル情報取得_Test_重複レコードを削除しない_引数での明示的指定なし() {
        initialize(ct);
        ContentResolverQueryOrganizer crqo = new ContentResolverQueryOrganizer(EXTERNAL_CONTENT_URI,null,null,null,null);
        assertEquals(EXTERNAL_CONTENT_URI, crqo.getUri());
        // query実行の度に異なる値を取得する為、Cursor値の比較は不可
        assertNotNull(crqo.query(15,5));
        assertNotNull(crqo.query(1,0));
        assertNotNull(crqo.query(0,2));
        assertNotNull(crqo.query(0,0));
    }

    @SuppressWarnings({"NonAsciiCharacters"})
    @Test
    public void テーブル情報取得_Test_OK_重複レコードを削除しない_引数での明示的指定あり() {
        initialize(ct);
        ContentResolverQueryOrganizer crqo = new ContentResolverQueryOrganizer(EXTERNAL_CONTENT_URI, false,null,null,null,null);
        assertEquals(EXTERNAL_CONTENT_URI,crqo.getUri());
        assertNotNull(crqo.query(15,5));
        assertNotNull(crqo.query(1,0));
        assertNotNull(crqo.query(0,2));
        assertNotNull(crqo.query(0,0));
    }

    @SuppressWarnings({"NonAsciiCharacters"})
    @Test
    public void テーブル情報取得_Test_OK_重複レコードを削除する_引数での明示的指定あり() {
        initialize(ct);
        ContentResolverQueryOrganizer crqo = new ContentResolverQueryOrganizer(EXTERNAL_CONTENT_URI, true,null,null,null,null);
        assertEquals(EXTERNAL_CONTENT_URI,crqo.getUri());
        assertNotNull(crqo.query(15,5));
        assertNotNull(crqo.query(1,0));
        assertNotNull(crqo.query(0,2));
        assertNotNull(crqo.query(0,0));
    }

    //endregion OK case

    // region NG case
    @SuppressWarnings({"NonAsciiCharacters", "ConstantConditions"})
    @Test
    public void テーブル情報取得_Test_NG_distinctなし() {
        initialize(null);

        ContentResolverQueryOrganizer crqo = new  ContentResolverQueryOrganizer(null,null, null, null, null);
        assertNull(crqo.getUri());
    }
    @SuppressWarnings({"NonAsciiCharacters", "ConstantConditions"})
    @Test
    public void テーブル情報取得_Test_NG_distinct_false() {
        initialize(null);

        ContentResolverQueryOrganizer crqo = new  ContentResolverQueryOrganizer(null, false,null, null, null, null);
        assertNull(crqo.getUri());
    }
    @SuppressWarnings({"NonAsciiCharacters", "ConstantConditions"})
    @Test
    public void テーブル情報取得_Test_NG_distinct_true() {
        initialize(null);

        ContentResolverQueryOrganizer crqo = new  ContentResolverQueryOrganizer(null, true,null, null, null, null);
        assertNull(crqo.getUri());
    }

    // endregion NG case
}