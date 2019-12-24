package jp.co.model.tkato.general_module.log.tree;

import android.util.Log;

import androidx.annotation.NonNull;

import timber.log.Timber;

public class ReleaseTree extends Timber.DebugTree {

    private static final int MAX_LOG_LENGTH = 4000;


    private boolean showLink = true;

    private String callerInfo;

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {

        switch (priority) {
        case Log.VERBOSE:
        case Log.DEBUG:
            return;
        }

        if (showLink) {
            callerInfo = Util.getCallerInfo(new Throwable().getStackTrace());
        }

        if (message.length() < MAX_LOG_LENGTH) {
            printSingleLine(priority, tag, message + callerInfo);
        } else {
            printMultipleLines(priority, tag, message);
        }
    }

    private void printMultipleLines(int priority, String tag, String message) {
        Util.printMultipleLines(MAX_LOG_LENGTH, showLink, callerInfo, priority, tag, message);
    }

    private void printSingleLine(int priority, @NonNull String tag, String message) {
        Util.printSingleLine(priority, tag, message);
    }
}
