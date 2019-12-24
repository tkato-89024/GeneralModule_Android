package jp.co.model.tkato.general_module.log.tree;

import android.util.Log;

import androidx.annotation.NonNull;

final class Util {

    // region line jump function
    // LogCat にリンクを付与する際に使用
    // Timber から直接ログ出力する場合は 5 指定だが、Logger から出力する場合は 6 指定でないと、正しい位置にリンクが貼られない

    private static final String CALLER_INFO_FORMAT = " at %s(%s:%s)";
    private static final int    CALLER_INFO_NUM    = 6;

    // endregion line jump function

    static void printMultipleLines(int maxLogLength, boolean showLink, String callerInfo,
                                   int priority, String tag, String message
    ) {
        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                final int    end  = Math.min(newline, i + maxLogLength);
                final String part = message.substring(i, end);
                printSingleLine(priority, tag, part);
                i = end;
            } while (i < newline);
        }

        if (showLink && (null == callerInfo|| 0 == callerInfo.length())) {
            printSingleLine(priority, tag, callerInfo);
        }
    }

    static void printSingleLine(int priority, @NonNull String tag, String message) {
        if (Log.ASSERT == priority) {
            Log.wtf(tag, message);
        } else {
            Log.println(priority, tag, message);
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
        return String.format(Util.CALLER_INFO_FORMAT, packageName, stack.getFileName(), stack.getLineNumber());
    }
}
