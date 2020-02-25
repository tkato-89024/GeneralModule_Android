package jp.co.model.tkato.general_module.cursor;

import android.database.Cursor;

public interface ICursorer {

    Cursor getCursor();
    int getCountByCursor();
    Cursorer query();
    Cursorer query(final long length, final long offset);
}
