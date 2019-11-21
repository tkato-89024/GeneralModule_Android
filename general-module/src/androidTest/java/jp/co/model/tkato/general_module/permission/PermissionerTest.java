package jp.co.model.tkato.general_module.permission;

import android.Manifest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@LargeTest // 時間のかかるテスト
@RunWith(AndroidJUnit4.class)
public class PermissionerTest implements View.OnClickListener, ActivityScenario.ActivityAction<PermissionerTestActivity> {

    // region member / setup / tearDown

    // public のみ
    @Rule
    public ActivityScenarioRule<PermissionerTestActivity> rule = new ActivityScenarioRule<>(PermissionerTestActivity.class);

    private Permissioner   permissioner;
    private CountDownLatch latch;

    @After
    public void tearDown() {
        permissioner = null;
        if (null != latch) {
            countFinish(latch);
            latch = null;
        }
    }

    // endregion member / setup / tearDown

    // region 非 UI 操作テスト

    @Test
    public void test_selfThanVersion() {

        final int selfVersion = Build.VERSION.SDK_INT;

        if (Permissioner.selfVersionLessThan(selfVersion + 1)) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @SuppressWarnings("all")
    @Test
    public void test_パラメータチェック() throws InterruptedException {

        permissioner = Permissioner.create(0);
        assertEquals(0, permissioner.getRequestCode());
        permissioner = Permissioner.create(1);
        assertEquals(1, permissioner.getRequestCode());

        permissioner
            .add(Manifest.permission.CAMERA)
        ;
        assertEquals(1, permissioner.getPermissions().size());
        assertEquals(Manifest.permission.CAMERA, permissioner.getPermissions().get(0));

        // ２重登録なし
        permissioner
            .add(Manifest.permission.CAMERA)
        ;
        assertEquals(1, permissioner.getPermissions().size());
        assertEquals(Manifest.permission.CAMERA, permissioner.getPermissions().get(0));


        permissioner
            .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ;
        assertEquals(2, permissioner.getPermissions().size());
        assertEquals(Manifest.permission.CAMERA,                 permissioner.getPermissions().get(0));
        assertEquals(Manifest.permission.WRITE_EXTERNAL_STORAGE, permissioner.getPermissions().get(1));

        permissioner
            .remove(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .remove(Manifest.permission.CAMERA)
        ;
        assertEquals(0, permissioner.getPermissions().size());
    }

    // endregion 非 UI 操作テスト

    // region UI 操作テスト

    @Test
    public void test_request_isGranted_shouldShow() throws InterruptedException {
        // テスト方法
        // 1. このテスト用にインストールされたアプリケーションを（すべてのユーザーから）アンインストール
        // 2. テスト実行
        // 3. カメラ権限、ストレージ書込権限の許可ダイアログが表示されるので、どちらも許可しない
        // 4. 失敗時のテスト発生
        // 5. もう一度、カメラ権限、ストレージ書込権限の許可ダイアログが表示されるので、どちらも許可
        // 6. テスト完了

        latch = new CountDownLatch(1);

        rule.getScenario().onActivity(activity -> {

            assertThat("Activity is running", activity.isFinishing(), Matchers.is(false));
            activity.setClickListener(this);
            activity.setTestTitle("test_request_isGranted_shouldShow");

            permissioner = Permissioner.create(0)
                .add(Manifest.permission.CAMERA)
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .success((self, activity_) -> {

                    assertTrue(Permissioner.isGranted(activity_, Manifest.permission.CAMERA));
                    assertTrue(Permissioner.isGranted(activity_, Manifest.permission.WRITE_EXTERNAL_STORAGE));

                    ((PermissionerTestActivity) activity_).getLatch().countDown();
                })
                .failure((self, activity_, allPermissionList, successPermissionList, failPermissionList) -> {

                    final boolean isGrantedCamera   = Permissioner.isGranted(activity_, Manifest.permission.CAMERA);
                    final boolean shouldShowCamera  = Permissioner.shouldShowRequest(activity, Manifest.permission.CAMERA);
                    final boolean isGrantedStorage  = Permissioner.isGranted(activity_, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    final boolean shouldShowStorage = Permissioner.shouldShowRequest(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    // !isGranted && !shouldShow の場合、パーミッションをリクエストしてもダイアログが表示されないため
                    if (   (!isGrantedCamera  && !shouldShowCamera)
                        || (!isGrantedStorage && !shouldShowStorage)
                    ) {
                        fail("アプリを（すべてのユーザーから）アンインストールして、テストをやり直してください。");
                        return;
                    }

                    self.request(activity_);
                })
            ;
            activity.setTestParameter(permissioner, latch);
            permissioner.request(activity);
        });

        latch.await();
    }

    // region 通知アクセステスト

    @Test
    public void test_isGrantedNotificationListener() throws InterruptedException {

        // 通知許可するまでテストをリトライする（5 回まで）

        latch = new CountDownLatch(5);

        rule.getScenario().onActivity((activity) -> {

            assertThat("Activity is running", activity.isFinishing(), Matchers.is(false));
            activity.setClickListener(this);
            activity.setTestTitle("test_isGrantedNotificationListener");

            final boolean isGrantedNotificationListener = Permissioner.isGrantedNotificationListener(activity);

            if (isGrantedNotificationListener) {
                assertTrue(true);
                countFinish(latch);
                return;
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Permissioner.openSettingForNotificationListener(activity, 1);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    activity.setOnResumeListener(this);
                }, 500);
            }, 1500);
        });

        latch.await();
    }

    @Override
    public void perform(PermissionerTestActivity activity) {

        activity.setOnResumeListener(null);

        final boolean isGrantedNotificationListener = Permissioner.isGrantedNotificationListener(activity);

        if (isGrantedNotificationListener) {
            assertTrue(true);
            countFinish(latch);
            return;
        }

        final long count = latch.getCount() - 1;
        if (0L >= count) {
            fail();
            return;
        }
        latch.countDown();

        final String msg = "通知アクセス未許可\n残り試行回数: " + count;
        Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        Log.w(">>>>>", msg);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Permissioner.openSettingForNotificationListener(activity, 1);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                activity.setOnResumeListener(this);
            }, 500);
        }, 1500);
    }

    // endregion 通知アクセステスト

    // endregion UI 操作テスト

    // region 非テスト（補助処理）

    @Override
    public void onClick(View view) {
        // テスト強制終了ボタンの処理
        fail();
    }

    private void countFinish(CountDownLatch latch) {
        if (null == latch) {
            return;
        }
        final int len = (int) latch.getCount();
        for (int i = 0; i < len; i++) {
            latch.countDown();
        }
    }

    // endregion 非テスト（補助処理）
}