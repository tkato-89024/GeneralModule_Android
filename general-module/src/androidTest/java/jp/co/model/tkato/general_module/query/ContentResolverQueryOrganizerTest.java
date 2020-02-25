package jp.co.model.tkato.general_module.query;

import android.content.Context;
import android.net.Uri;

import org.junit.Test;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static jp.co.model.tkato.general_module.query.ContentResolverQueryOrganizer.initialize;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ContentResolverQueryOrganizerTest {

    private Context ct = getApplicationContext();
    private final Uri imageContentUri = EXTERNAL_CONTENT_URI;

    // region OK case
    @SuppressWarnings({"NonAsciiCharacters"})
    @Test
    public void テーブル情報取得_Test_重複レコードを除外しない_引数での明示的指定なし() {
        initialize(ct);
        ContentResolverQueryOrganizer crqo = new ContentResolverQueryOrganizer(imageContentUri,null,null,null,null);
        assertEquals(imageContentUri, crqo.getUri());
        // queryでカーソルを取得できているかの確認
        assertNotNull(crqo.query(15,5));
        assertNotNull(crqo.query(1,0));
        assertNotNull(crqo.query(0,2));
        assertNotNull(crqo.query(0,0));
        // FIXME: Cursor値の比較
    }

    @SuppressWarnings({"NonAsciiCharacters"})
    @Test
    public void テーブル情報取得_Test_OK_重複レコードを除外しない_引数での明示的指定あり() {
        initialize(ct);
        ContentResolverQueryOrganizer crqo = new ContentResolverQueryOrganizer(imageContentUri, false,null,null,null,null);
        assertEquals(imageContentUri,crqo.getUri());
        // queryでカーソルを取得できているかの確認
        assertNotNull(crqo.query(15,5));
        assertNotNull(crqo.query(1,0));
        assertNotNull(crqo.query(0,2));
        assertNotNull(crqo.query(0,0));
        // FIXME: Cursor値の比較
    }

    @SuppressWarnings({"NonAsciiCharacters"})
    @Test
    public void テーブル情報取得_Test_OK_重複レコードを除外する_引数での明示的指定あり() {
        initialize(ct);
        ContentResolverQueryOrganizer crqo = new ContentResolverQueryOrganizer(imageContentUri, true,null,null,null,null);
        assertEquals(imageContentUri,crqo.getUri());
        // queryでカーソルを取得できているかの確認
        assertNotNull(crqo.query(15,5));
        assertNotNull(crqo.query(1,0));
        assertNotNull(crqo.query(0,2));
        assertNotNull(crqo.query(0,0));
        // FIXME: Cursor値の比較
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
        // 重複行を除外しない設定になっていることを確認
        assertFalse(crqo.distinct);
    }
    @SuppressWarnings({"NonAsciiCharacters", "ConstantConditions"})
    @Test
    public void テーブル情報取得_Test_NG_distinct_true() {
        initialize(null);

        ContentResolverQueryOrganizer crqo = new  ContentResolverQueryOrganizer(null, true,null, null, null, null);
        assertNull(crqo.getUri());
        // 重複行を除外する設定になっていることを確認
        assertTrue(crqo.distinct);
    }

    // endregion NG case
}