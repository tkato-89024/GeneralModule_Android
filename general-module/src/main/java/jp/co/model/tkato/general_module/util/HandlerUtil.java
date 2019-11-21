package jp.co.model.tkato.general_module.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class HandlerUtil {

    // region interface

    public static class InternalRunnable implements Runnable {

        private String   runnableId;
        private Runnable runnable;

        public InternalRunnable(String runnableId, Runnable runnable) {
            this.runnableId = runnableId;
            this.runnable = runnable;
        }

        @Override
        public void run() {

            if (!runnableMap.containsKey(runnableId)) {
                return;
            }
            runnableMap.remove(runnableId); // InternalRunnable 自身の参照を外す

            if (null != this.runnable) {
                this.runnable.run();
            }
        }
    }

    // endregion interface

    // region static var

    @NonNull
    private static final Handler mainLoopHandler = new Handler(Looper.getMainLooper());

    @NonNull
    private static final Object lock = new Object();

    @NonNull
    private static final Map<String, Runnable> runnableMap = Collections.synchronizedMap(new HashMap<>());

    // endregion static var

    // region static method

    /**
     * 現在のスレッドが UI スレッド上かどうか
     * @return 正否
     */
    public static boolean isCurrentThreadUI() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * 現在待機中の処理
     * @return 待機中の処理の数
     */
    public static int currentRunnableCount() {
        return runnableMap.size();
    }

    /**
     * UI スレッド上で、Runnable をディレイなしで即時実行
     * @param runnable UI スレッド上で実行する処理
     */
    public static void runOnUiThread(@NonNull final Runnable runnable) {
        runOnUiThread(runnable, -1);
    }

    /**
     * UI スレッド上で、Runnable をディレイ実行、もしくは即時実行
     * @param runnable      UI スレッド上で実行する処理
     * @param delayMilliSec 0 以上指定で、ミリ秒後に runnable をディレイ実行（負の数の場合、即時実行）
     * @return 管理 ID
     */
    @SuppressWarnings({"ConstantConditions", "UnusedReturnValue"})
    @Nullable
    public static String runOnUiThread(@NonNull final Runnable runnable, int delayMilliSec) {

        synchronized (lock) {
            if (null == runnable) {
                return null;
            }

            final String runnableId = UUID.randomUUID().toString();

            if (0 <= delayMilliSec) {
                final InternalRunnable internalRunnable = new InternalRunnable(runnableId, runnable);
                runnableMap.put(runnableId, internalRunnable);
                mainLoopHandler.postDelayed(internalRunnable, delayMilliSec);

            } else if (isCurrentThreadUI()) {
                runnable.run();

            } else {
                mainLoopHandler.post(runnable);
            }

            return runnableId;
        }
    }

    /**
     * 待機中の処理の中に、指定した管理 ID と紐付く処理があればキャンセル
     * @param runnableId 管理 ID
     * @return 紐づく処理があれば true
     */
    @SuppressWarnings({"ConstantConditions", "UnusedReturnValue"})
    public static boolean cancelRunOnUIThread(@NonNull String runnableId) {

        synchronized (lock) {
            if (   null == runnableId
                || !runnableMap.containsKey(runnableId)
            ) {
                return false;
            }

            mainLoopHandler.removeCallbacks(runnableMap.remove(runnableId));
            return true;
        }
    }

    /**
     * 待機中の全ての処理をキャンセル
     */
    public static void cancelAllRunOnUIThread() {
        synchronized (lock) {
            mainLoopHandler.removeCallbacksAndMessages(null);
            runnableMap.clear();
        }
    }

    // endregion static method

    private HandlerUtil() {}
}
