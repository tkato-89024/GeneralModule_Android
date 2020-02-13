package jp.co.model.tkato.general_module.util;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import jp.co.model.tkato.general_module.base.BaseInstrumentedTest;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class HandlerUtilTest extends BaseInstrumentedTest {

    @Rule
    public ActivityScenarioRule<HandlerUtilTestActivity> rule = new ActivityScenarioRule<>(HandlerUtilTestActivity.class);

    private int count = 0;

    @Before
    public void setUp() {
        count = 0;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        HandlerUtil.cancelAllRunOnUIThread();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void test_isCurrentThreadUI() throws InterruptedException {

        // UI スレッド上で実行する処理がNullの時
        final Runnable nullpettern = null;
        assertNull(HandlerUtil.runOnUiThread(nullpettern,0));

        countSetup(1);

        // テストメソッドは非 UI スレッドでの実行なので false
        assertFalse(HandlerUtil.isCurrentThreadUI());

        // runOnUiThread 内の処理は UI スレッドなので true
        HandlerUtil.runOnUiThread(() -> {
            assertTrue(HandlerUtil.isCurrentThreadUI());

            // runOnUiThread 内の runOnUiThread 処理は、同じく UI スレッドなので true
            HandlerUtil.runOnUiThread(() -> {
                assertTrue(HandlerUtil.isCurrentThreadUI());

                HandlerUtil.runOnUiThread(() -> {
                    // ディレイ時も runOnUiThread なので true
                    assertTrue(HandlerUtil.isCurrentThreadUI());
                    getLatch().countDown();
                }, 1000);
            });
        });

        getLatch().await(2000, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("all")
    @Test
    public void test_runOnUiThread_case1_非同期処理になっていないかどうかの確認() {

        for (int i = 0; i < 10; i++) {
            HandlerUtil.runOnUiThread(() -> {
                count = 0;

                count += 1;

                HandlerUtil.runOnUiThread(() -> {
                    count += 1;
                });

                count += 1;

                assertEquals(3, count);
            });
        }
        assertEquals(0, HandlerUtil.currentRunnableCount());
    }

    @SuppressWarnings("all")
    @Test
    public void test_runOnUiThread_case2_ディレイの確認() throws InterruptedException {

        countSetup(1);

        HandlerUtil.runOnUiThread(() -> {

            getLatch().countDown();

            assertEquals(0, HandlerUtil.currentRunnableCount());

        }, 1000);

        assertEquals(1, HandlerUtil.currentRunnableCount());

        getLatch().await(2000, TimeUnit.MILLISECONDS);

        assertEquals(0, HandlerUtil.currentRunnableCount());
    }

    @SuppressWarnings("all")
    @Test
    public void test_cancelRunOnUIThread_実行されるケース() throws InterruptedException {

        // キャンセルできていなければ fail

        countSetup(1);

        final String runnableId = HandlerUtil.runOnUiThread(() -> {

            fail();

        }, 500);

        assertNotNull(runnableId);
        assertEquals(1, HandlerUtil.currentRunnableCount());

        HandlerUtil.cancelRunOnUIThread(runnableId);

        assertEquals(0, HandlerUtil.currentRunnableCount());

        getLatch().await(1000, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("all")
    @Test
    public void test_cancelRunOnUIThread_実行されないケース() throws InterruptedException {
        //  Nullパターン
        assertFalse(HandlerUtil.cancelRunOnUIThread(null));

        // 管理IDが誤っている場合
        final String notId = "hoge";
        assertFalse(HandlerUtil.cancelRunOnUIThread(notId));
    }

    @SuppressWarnings("all")
    @Test
    public void cancelAllRunOnUIThread() throws InterruptedException {

        countSetup(1);

        HandlerUtil.runOnUiThread(() -> {
            fail();
        }, 50);
        HandlerUtil.runOnUiThread(() -> {
            fail();
        }, 750);
        HandlerUtil.runOnUiThread(() -> {
            fail();
        }, 1000);

        assertEquals(3, HandlerUtil.currentRunnableCount());

        HandlerUtil.cancelAllRunOnUIThread();

        assertEquals(0, HandlerUtil.currentRunnableCount());

        getLatch().await(1500, TimeUnit.MILLISECONDS);
        
    }
}