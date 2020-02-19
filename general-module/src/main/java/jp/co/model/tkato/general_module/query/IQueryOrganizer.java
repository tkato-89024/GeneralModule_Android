package jp.co.model.tkato.general_module.query;
import android.database.Cursor;

public interface IQueryOrganizer {

    Cursor query();
    Cursor query(final long length, final long offset);
}
