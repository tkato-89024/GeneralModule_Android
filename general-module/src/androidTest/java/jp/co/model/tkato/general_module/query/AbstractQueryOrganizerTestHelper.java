package jp.co.model.tkato.general_module.query;

import android.database.Cursor;

@SuppressWarnings("WeakerAccess")
public class AbstractQueryOrganizerTestHelper extends AbstractQueryOrganizer {

    @Override
    public Cursor query(long length, long offset) {
        return null;
    }

    public void setDistinct(boolean distinct) {

        this.distinct = distinct;
    }

    public void setProjection(String[] projection) {

        this.projection = projection;
    }

    public void setSelection(String selection) {

        this.selection = selection;
    }

    public void setSelectionArgs(String[] selectionArgs) {

        this.selectionArgs = selectionArgs;
    }

    public void setOrderBy(String orderBy) {

        this.orderBy = orderBy;
    }
}
