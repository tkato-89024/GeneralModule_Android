package jp.co.model.tkato.general_module.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SQLiteDatabaseQueryOrganizer extends AbstractQueryOrganizer {

    private SQLiteDatabase db;
    private String table;
    private String groupBy;
    private String having;

    SQLiteDatabase getDb() {
        return db;
    }

    String getTable() {
        return table;
    }

    String getGroupBy() {
        return groupBy;
    }

    String getHaving() {
        return having;
    }

    SQLiteDatabaseQueryOrganizer(@NonNull SQLiteDatabase db, @NonNull String table, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy) {
        this.db            = db;
        this.table         = table;
        this.distinct      = false;
        this.projection    = projection;
        this.selection     = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy       = groupBy;
        this.having        = having;
        this.orderBy       = orderBy;
    }

    SQLiteDatabaseQueryOrganizer(@NonNull SQLiteDatabase db, @NonNull String table, boolean distinct, String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy) {
        this.db            = db;
        this.table         = table;
        this.distinct      = distinct;
        this.projection    = projection;
        this.selection     = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy       = groupBy;
        this.having        = having;
        this.orderBy       = orderBy;
    }

    public Cursor query(long length, long offset) {
        if (length > 0) {

            // query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
            // queryWithFactory(null, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, null);

            // queryWithFactory(CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal)
            // String sql = SQLiteQueryBuilder.buildQueryString(distinct, table, columns, selection, groupBy, having, orderBy, limit);

            // String sql = SQLiteQueryBuilder.buildQueryString(distinct, table, projection, selection, groupBy, having, orderBy, null);
            return db.query(distinct, table, projection, selection, selectionArgs, groupBy, having, orderBy + " LIMIT " + length + " OFFSET " + offset, null);

        } else {
            // String sql = SQLiteQueryBuilder.buildQueryString(distinct, table, projection, selection, groupBy, having, orderBy, null);
            return db.query(distinct, table, projection, selection, selectionArgs, groupBy, having, orderBy, null);
        }
    }
}
