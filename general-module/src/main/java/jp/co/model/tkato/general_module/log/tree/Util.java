package jp.co.model.tkato.general_module.log.tree;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

final class Util {

    static final int MAX_ONE_LINE_LENGTH = 4000;

    // region line jump function
    // LogCat にリンクを付与する際に使用
    // Timber から直接ログ出力する場合は 5 指定だが、Logger から出力する場合は 6 指定でないと、正しい位置にリンクが貼られない

    private static final String CALLER_INFO_FORMAT = " at %s / %s(%s:%s)";
    private static final int    CALLER_INFO_NUM    = 6;

    // endregion line jump function

    static void printMultipleLines(int maxLogLength, int priority, String tag, String message, String callerInfo) {

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                final int    end  = Math.min(newline, i + maxLogLength);
                final String part = message.substring(i, end);
                printSingleLine(priority, tag, part, null);
                i = end;
            } while (i < newline);
        }

        if (null != callerInfo && 0 < callerInfo.length()) {
            printSingleLine(priority, tag, null, callerInfo);
        }
    }

    @SuppressLint("LogNotTimber") // Using 'Log' instead of 'Timber'
    static void printSingleLine(int priority, @NonNull String tag, String message, String callerInfo) {

        final StringBuilder buffer = new StringBuilder();

        if (null != message && 0 < message.length()) {
            buffer.append(message);
        }
        // Logger.v("") の場合、ここまで処理されないため無意味
        // else {
        //     buffer.append("[undefined]");
        // }

        if (null != callerInfo && 0 < callerInfo.length()) {
            buffer.append(callerInfo);
        }

        if (Log.ASSERT == priority) {
            Log.wtf(tag, buffer.toString());
        } else {
            Log.println(priority, tag, buffer.toString());
        }
    }

    @NonNull
    static String getCallerInfo(StackTraceElement[] stacks) {
        if (null == stacks  || CALLER_INFO_NUM > stacks.length) {
            // are you using proguard???
            return "";
        }
        return Util.formatForLogCat(stacks[CALLER_INFO_NUM]);
    }

    private static String formatForLogCat(StackTraceElement stack) {
        final String className   = stack.getClassName();
        final String packageName = className.substring(0, className.lastIndexOf("."));
        return String.format(Util.CALLER_INFO_FORMAT, packageName, stack.getMethodName(), stack.getFileName(), stack.getLineNumber());
    }
}
