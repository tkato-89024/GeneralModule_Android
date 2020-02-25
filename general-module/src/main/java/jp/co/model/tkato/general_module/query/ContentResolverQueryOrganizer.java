package jp.co.model.tkato.general_module.query;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContentResolverQueryOrganizer extends AbstractQueryOrganizer {

    // region initialize

    @NonNull
    protected static Context context;

    public static void initialize(@NonNull final Context appContext) {
        context = appContext;
    }

    // endregion initialize

    protected Uri uri;

    public Uri getUri() {
        return uri;
    }

    public ContentResolverQueryOrganizer(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        this.distinct      = false;
        this.uri           = uri;
        this.projection    = projection;
        this.selection     = selection;
        this.selectionArgs = selectionArgs;
        this.orderBy       = orderBy;
    }

    public ContentResolverQueryOrganizer(@NonNull Uri uri, boolean distinct, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        this.distinct      = distinct;
        this.uri           = uri;
        this.projection    = projection;
        this.selection     = selection;
        this.selectionArgs = selectionArgs;
        this.orderBy       = orderBy;
    }

    //@Override
    public Cursor query(long length, long offset) {

        final String distinct = String.valueOf(this.distinct);

        if (length > 0) {
            return context.getContentResolver().query(uri.buildUpon().appendQueryParameter("DISTINCT", distinct).build(), projection, selection, selectionArgs, orderBy + " LIMIT " + length + " OFFSET " + offset);
        } else {
            return context.getContentResolver().query(uri.buildUpon().appendQueryParameter("DISTINCT", distinct).build(), projection, selection, selectionArgs, orderBy);
        }
    }
}
