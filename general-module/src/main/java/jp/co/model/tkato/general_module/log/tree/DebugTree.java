package jp.co.model.tkato.general_module.log.tree;

import androidx.annotation.NonNull;

import timber.log.Timber;

public class DebugTree extends Timber.DebugTree {

    private int maxOneLineLength = Util.MAX_ONE_LINE_LENGTH;

    public DebugTree() {
    }

    public DebugTree(int maxOneLineLength) {
        this.maxOneLineLength = maxOneLineLength;
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {

        final StackTraceElement[] elements = new Throwable().getStackTrace();

        final String callerInfo = Util.getCallerInfo(elements);

        if (message.length() < maxOneLineLength) {
            Util.printSingleLine(priority, tag, message, callerInfo);
        } else {
            Util.printMultipleLines(maxOneLineLength, priority, tag, message, callerInfo);
        }
    }
}
