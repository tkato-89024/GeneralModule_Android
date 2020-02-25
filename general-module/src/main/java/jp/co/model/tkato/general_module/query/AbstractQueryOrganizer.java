package jp.co.model.tkato.general_module.query;

import android.database.Cursor;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractQueryOrganizer implements IQueryOrganizer {

    protected boolean  distinct;   // 選んだカラムの結果の重複を削る。カラムが複数ある場合、複数の結果を合わせて重複してるものを削る
    protected String[] projection; // null 指定で、全て取得
    protected String   selection;
    protected String[] selectionArgs;
    protected String   orderBy;

    public boolean isDistinct() {
        return distinct;
    }

    public String[] getProjection() {
        return projection;
    }

    public String getSelection() {
        return selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public Cursor query() {
        return query(0, 0);
    }
}
