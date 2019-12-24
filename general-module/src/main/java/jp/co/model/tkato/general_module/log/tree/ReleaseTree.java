package jp.co.model.tkato.general_module.log.tree;

import android.util.Log;

import androidx.annotation.NonNull;

import timber.log.Timber;

public class ReleaseTree extends Timber.DebugTree {

    private int maxOneLineLength = Util.MAX_ONE_LINE_LENGTH;

    public ReleaseTree() {
    }

    public ReleaseTree(int maxOneLineLength) {
        this.maxOneLineLength = maxOneLineLength;
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {

        switch (priority) {
        case Log.VERBOSE:
        case Log.DEBUG:
            return;
        }

        final StackTraceElement[] elements = new Throwable().getStackTrace();

        final String callerInfo = Util.getCallerInfo(elements);

        if (message.length() < maxOneLineLength) {
            Util.printSingleLine(priority, tag, message, callerInfo);
        } else {
            Util.printMultipleLines(maxOneLineLength, priority, tag, message, callerInfo);
        }
    }
}
