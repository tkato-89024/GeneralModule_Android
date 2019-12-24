package jp.co.model.tkato.general_module.log;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.co.model.tkato.general_module.log.factory.TreeFactory;
import jp.co.model.tkato.general_module.log.tree.DebugTree;
import jp.co.model.tkato.general_module.log.tree.ReleaseTree;
import timber.log.Timber;

import static org.junit.Assert.fail;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class LoggerTest {

    public static class TestTreeFactory extends TreeFactory {

        public TestTreeFactory(boolean isDebug) {
            super(isDebug);
        }

        @Override
        public Timber.Tree create() {
            if (isDebug()) {
                return new DebugTree(10);
            } else {
                return new ReleaseTree(10);
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        Logger.clearTree();
    }

    @SuppressWarnings("all")
    @Test
    public void test_initialize_Nullチェック() {

        Log.v(getClass().getSimpleName(), ">> start test_initialize_Nullチェック ======================================================");

        try {
            Logger.initialize(() -> null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getLocalizedMessage());
        }

        Log.v(getClass().getSimpleName(), "test_initialize_Nullチェック =============================================================");

        // LogCat を見て判断
        // null が設定され、ログは表示されなくなる

        Logger.v(""); // 表示されない
        Logger.v("test v");
        Logger.d("test d");
        Logger.i("test i");
        Logger.w("test w");
        Logger.e("test e");
        Logger.wtf(new Throwable("Test Throwable"),"test wtf");

        Log.v(getClass().getSimpleName(), "<< end test_initialize_Nullチェック ======================================================");
    }

    @Test
    public void test_log_debug() {

        // LogCat を見て判断

        Log.v(getClass().getSimpleName(), ">> start test_log_debug ======================================================");

        Logger.initialize(true);

        Logger.v("");
        Logger.v("a");
        Logger.v("test v");
        Logger.d("test d");
        Logger.i("test i");
        Logger.w("test w");
        Logger.e("test e");
        Logger.wtf(new Throwable("Test Throwable"),"test wtf");
        Logger.v("test v %d", 1);
        Logger.d("test d %d", 1);
        Logger.i("test i %d", 1);
        Logger.w("test w %d", 1);
        Logger.e("test e %d", 1);
        Logger.e(new IllegalArgumentException(), "test e %d", 1);
        Logger.wtf(new Throwable("Test Throwable"),"test wtf %d", 1);


        Log.v(getClass().getSimpleName(), "test_log_debug =============================================================");

        // 同様の結果になる

        Logger.initialize(new TreeFactory(true));

        Logger.v("");
        Logger.v("a");
        Logger.v("test v");
        Logger.d("test d");
        Logger.i("test i");
        Logger.w("test w");
        Logger.e("test e");
        Logger.wtf(new Throwable("Test Throwable"),"test wtf");
        Logger.v("test v %d", 1);
        Logger.d("test d %d", 1);
        Logger.i("test i %d", 1);
        Logger.w("test w %d", 1);
        Logger.e("test e %d", 1);
        Logger.e(new IllegalArgumentException(), "test e %d", 1);
        Logger.wtf(new Throwable("Test Throwable"),"test wtf %d", 1);

        Log.v(getClass().getSimpleName(), "<< end test_log_debug ======================================================");
    }

    @Test
    public void test_log_release() {

        // LogCat を見て判断

        Log.v(getClass().getSimpleName(), ">> start test_log_release ======================================================");

        // ReleaseTree を使用しているので、verbose, debug が表示されない

        Logger.initialize(false);

        Logger.v("test v");
        Logger.d("test d");
        Logger.i("");
        Logger.i("a");
        Logger.i("test i");
        Logger.w("test w");
        Logger.e("test e");
        Logger.wtf(new Throwable("Test Throwable"),"test wtf");
        Logger.v("test v %d", 1);
        Logger.d("test d %d", 1);
        Logger.i("test i %d", 1);
        Logger.w("test w %d", 1);
        Logger.e("test e %d", 1);
        Logger.e("test e %d", 1);
        Logger.e(new IllegalArgumentException(), "test e %d", 1);
        Logger.wtf(new Throwable("Test Throwable"),"test wtf %d", 1);

        Log.v(getClass().getSimpleName(), "test_log_debug =============================================================");

        // 同様の結果になる

        Logger.initialize(new TreeFactory(false));

        Logger.v("test v");
        Logger.d("test d");
        Logger.i("");
        Logger.i("a");
        Logger.i("test i");
        Logger.w("test w");
        Logger.e("test e");
        Logger.wtf(new Throwable("Test Throwable"),"test wtf");
        Logger.v("test v %d", 1);
        Logger.d("test d %d", 1);
        Logger.i("test i %d", 1);
        Logger.w("test w %d", 1);
        Logger.e("test e %d", 1);
        Logger.e(new IllegalArgumentException(), "test e %d", 1);
        Logger.wtf(new Throwable("Test Throwable"),"test wtf %d", 1);

        Log.v(getClass().getSimpleName(), "<< end test_log_release ======================================================");
    }

    @SuppressWarnings("all")
    @Test
    public void test_log_改行チェック() {

        Log.v(getClass().getSimpleName(), ">> start test_log_改行チェック ======================================================");

        Logger.initialize(new TestTreeFactory(false));

        Logger.v("");
        Logger.v("a");
        Logger.v("abcde");
        Logger.v("あいうえお");
        Logger.v("abcdefghijklmnopqrstu");
        Logger.v("あいうえおかきくけこさしすせそたちつてとな");
        Logger.v("あいうえお\nかきくけこ\nさしすせそたちつてとな");


        Logger.initialize(new TestTreeFactory(true));

        Logger.i("");
        Logger.i("a");
        Logger.i("abcde");
        Logger.i("あいうえお");
        Logger.i("abcdefghijklmnopqrstu");
        Logger.i("あいうえおかきくけこさしすせそたちつてとな");
        Logger.i("あいうえお\nかきくけこ\nさしすせそたちつてとな");

        Log.v(getClass().getSimpleName(), "<< end test_log_改行チェック ======================================================");
    }
}