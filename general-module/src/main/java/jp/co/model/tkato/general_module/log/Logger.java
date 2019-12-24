package jp.co.model.tkato.general_module.log;

import androidx.annotation.NonNull;

import jp.co.model.tkato.general_module.log.factory.ITreeFactory;
import jp.co.model.tkato.general_module.log.factory.TreeFactory;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public final class Logger {

    @SuppressWarnings("ConstantConditions")
    public static void initialize(@NonNull ITreeFactory treeFactory) {

        if (null == treeFactory) {
            return;
        }

        clearTree();

        final Timber.Tree tree = treeFactory.create();
        if (null != tree) {
            Timber.plant(tree);
        }
    }

    public static void initialize(boolean isDebug) {
        initialize(new TreeFactory(isDebug));
    }

    public static void clearTree() {
        Timber.uprootAll();
    }

    // region log

    /**
     * verbose ログ<br/>
     * 一時的なデバッグログに使用<br/>
     * 使用箇所は削除するようにする
     * @param message メッセージ
     * @param args 引数
     */
    public static void v(String message, Object... args) {
        Timber.v(message, args);
    }

    /**
     * debug ログ<br/>
     * 定常的なデバッグログに使用<br/>
     * onClick 等のログを記す
     * @param message メッセージ
     * @param args 引数
     */
    public static void d(String message, Object... args) {
        Timber.d(message, args);
    }

    /**
     * info ログ<br/>
     * ユーザーに情報を残すように使用<br/>
     * デバッグ情報は載せない
     * @param message メッセージ
     * @param args 引数
     */
    public static void i(String message, Object... args) {
        Timber.i(message, args);
    }

    /**
     * warning ログ<br/>
     * 本来、起こってはいけないが、場合によっては起こりえるエラーに使用
     * @param message メッセージ
     * @param args 引数
     */
    public static void w(String message, Object... args) {
        Timber.w(message, args);
    }

    /**
     * error ログ<br/>
     * プログラムの構造上起こりえないエラーが発生した時に使用
     * @param message メッセージ
     * @param args 引数
     */
    public static void e(String message, Object... args) {
        Timber.e(message, args);
    }

    public static void e(Throwable t ,String message, Object... args) {
        Timber.e(t, message, args);
    }

    /**
     * assert ログ（What a Terrible Failure）<br/>
     * 基本的には使用しない
     * @param t 例外情報
     * @param message メッセージ
     * @param args 引数
     */
    public static void wtf(Throwable t, String message, Object... args) {
        Timber.wtf(t, message, args);
    }

    // endregion log
}
