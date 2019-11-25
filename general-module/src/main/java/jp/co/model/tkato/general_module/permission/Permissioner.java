/*
 GeneralModule_Android Permissioner

 Copyright (c) 2019 tkato

 This software is released under the MIT License.
 http://opensource.org/licenses/mit-license.php
 */

package jp.co.model.tkato.general_module.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "WeakerAccess", "unused"})
public final class Permissioner {

    // region interface

    public interface OnSuccessListener {
        void run(@NonNull final Permissioner self, Activity activity);
    }

    public interface OnFailureListener {
        void run(@NonNull final Permissioner self, Activity activity, @NonNull final List<String> allPermissionList, @NonNull final List<String> successPermissionList, @NonNull final List<String> failPermissionList);
    }

    // endregion interface

    // region static method

    // region check version

    public static boolean selfVersionLessThan(final int version) {
        final int selfVersion = Build.VERSION.SDK_INT;
        return selfVersion < version;
    }

    public static boolean selfVersionLessThanM() {
        return selfVersionLessThan(Build.VERSION_CODES.M);
    }

    // endregion check version

    @SuppressWarnings({"ConstantConditions", "unused"})
    public static boolean isGranted(@NonNull final Context context, @NonNull final String permission) {
        if (null == context || null == permission) {
            return false;
        }
        final int result = ContextCompat.checkSelfPermission(context, permission);
        return 0 <= result;
    }

    /**
     * 権限が許可されていない場合 true を返し、許可済の場合は false を返す。
     * 以下の 2 種類の例外では false を返す。
     * 1. ActivityCompat.requestPermissions の初回実行前までの間は false。
     * 2. 権限ダイアログで「今後表示しない」を選択後は永久に false。
     * @param activity 対象の Activity
     * @param permission アプリが要求する許可
     * @return 結果
     */
    @SuppressWarnings("unused")
    public static boolean shouldShowRequest(@NonNull final Activity activity, @NonNull final String permission) {
        try {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 権限許可リクエストを行うインスタンスを作成
     * @param requestCode 識別番号
     * @return 新しいインスタンス
     */
    @SuppressWarnings("unused")
    public static Permissioner create(final int requestCode) {
        return new Permissioner(requestCode);
    }

    // endregion static method

    // region var

    private static final Object lock = new Object();

    private int requestCode;

    @NonNull
    private List<String> permissionList = new ArrayList<>();

    private OnSuccessListener successListener;
    private OnFailureListener failureListener;

    /**
     * リクエストを識別するコード
     * 主に Activity.onRequestPermissionsResult にて使用される
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     *
     * @return パーミッションリスト
     */
    @NonNull
    public List<String> getPermissions() {
        return permissionList;
    }

    /**
     * forEach で全て true を返した時に、ここで設定したリスナ－が実行される
     * @param listener リスナー
     * @return 自身のインスタンス
     */
    public Permissioner success(@Nullable final OnSuccessListener listener) {
        successListener = listener;
        return this;
    }

    /**
     * forEach で一度 false を返した時に、ここで設定したリスナ－が実行される
     * @param listener リスナー
     * @return 自身のインスタンス
     */
    public Permissioner failure(@Nullable final OnFailureListener listener) {
        failureListener = listener;
        return this;
    }

    // endregion var

    // region Permissioner

    private Permissioner(final int requestCode) {
        // 0 未満だと、リクエストを呼んだ際に
        // Can reqeust only one set of permissions at a time
        // という warnig ログが表示される
        this.requestCode = 0 > requestCode ? 0 : requestCode;
    }

    /**
     * request を実行した時にリクエストされるパーミッション
     * Manifest.permission から始まる定数を追加
     * @param permission  アプリが要求する許可
     * @return 自身のインスタンス
     */
    public Permissioner add(@NonNull final String permission) {
        synchronized (lock) {
            if (0 > permissionList.indexOf(permission)) {
                permissionList.add(permission);
            }
            return this;
        }
    }
    /**
     * add したパーミッションの削除
     * @param permission  アプリが要求する許可を削除
     * @return 自身のインスタンス
     */
    public Permissioner remove(@NonNull final String permission) {
        synchronized (lock) {
            if (0 <= permissionList.indexOf(permission)) {
                permissionList.remove(permission);
            }
            return this;
        }
    }

    /**
     * 追加した権限をまとめてリクエスト
     * 追加した順番にダイアログが表示される
     * @param activity 対象の Activity
     * @return 成否
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean request(@NonNull final Activity activity) {

        // 1. Android6 未満では requestPermission に反応がない（処理は実装されているが）
        // 2. ActivityCompat.requestPermissions 内の処理は、Android 6 未満の場合
        //    OnRequestPermissionsResultCallback が実装されていない場合は機能しないので
        //    その時はリスナーを直接実行する
        // 3. リスナーの Null チェック

        if (   selfVersionLessThanM()
            && !(activity instanceof ActivityCompat.OnRequestPermissionsResultCallback)
            && null != successListener
        ) {
            // UI スレッド上での実行
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                successListener.run(this, activity);
            } else {
                new Handler(Looper.getMainLooper()).post(() -> successListener.run(this, activity));
            }
            return true;
        }

        try {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[0]), requestCode);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * onRequestPermissionsResult で呼び出す
     * リクエストコードが適合した場合に戻り値 true を返し、
     * forEach で設定した内容を実行する
     * @param requestCode 識別番号
     * @param permissions  アプリが要求する許可（配列）
     * @param grantResults 許可の結果（配列）
     * @return リクエストコードが合っているかどうか
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean onRequestPermissionsResult(final Activity activity, final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {

        if (requestCode != this.requestCode) {
            return false;
        }

        // 追加されたパーミッションリクエストを全て調べ、
        // 許可されていないものが１つでもあれば失敗リスナーを実行
        final List<String> successList = new ArrayList<>();
        final List<String> failList    = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            final String  permission  = permissions[i];
            final int     grantResult = grantResults[i];
            final boolean isGrant     = PackageManager.PERMISSION_GRANTED == grantResult;

            if (isGrant) {
                successList.add(permission);
            } else {
                failList.add(permission);
            }
        }
        if (0 < failList.size() && null != failureListener) {
            failureListener.run(this, activity, Arrays.asList(permissions), successList, failList);
            return true;
        }

        if (null != successListener) {
            successListener.run(this, activity);
        }
        return true;
    }

    /**
     * 通知リスナーコンポーネントが有効になっている Service があれば true
     * @param context コンテキスト
     * @return 結果
     */
    public static boolean isGrantedNotificationListener(Context context) {
        return NotificationManagerCompat
            .getEnabledListenerPackages(context)
            .contains(context.getPackageName())
            ;
    }

    public static void openSettingForNotificationListener(Activity activity, int requestCode) {

        // Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
        // 設定画面を開いて設定してもらう
        final Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivityForResult(intent, requestCode);
    }

    // endregion Permissioner
}
