/*
 GeneralModule_Android Cipherer

 Copyright (c) 2019 tkato

 This software is released under the MIT License.
 http://opensource.org/licenses/mit-license.php
 */
package jp.co.model.tkato.general_module.cursor;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jp.co.model.tkato.general_module.query.IQueryOrganizer;

public class Cursorer implements ICursorer {

    @Nullable
    @SuppressWarnings("all")
    public IQueryOrganizer organizer;

    @Nullable
    protected Cursor cursor;

    @Nullable
    public IQueryOrganizer getOrganizer() {
        return organizer;
    }

    @Nullable
    public Cursor getCursor() {
        return cursor;
    }

    @SuppressWarnings("all")
    public Cursorer() {

    }

    @SuppressWarnings("all")
    public Cursorer(@Nullable IQueryOrganizer organizer) {
        this.organizer = organizer;
    }

    // 一時的に query を作成し取得
    @SuppressWarnings("all")
    public int getCountNewCursor(@NonNull final IQueryOrganizer organizer) {

        if (null == organizer) {
            //Logger.w("query fail: null organizer");
            return -1;
        }

        try (Cursor c = organizer.query()) {
            return cursor.getCount();
        } catch (Exception e) {
            //Logger.w("query fail: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return -1;
    }

    // query 実行後に取得可能
    public int getCountByCursor() {

        if (null == cursor) {
            //Logger.w("getCount fail: null cursor");
            return -1;
        }
        return cursor.getCount();
    }

    public Cursorer query() {
        return query(0, 0);
    }

    public Cursorer query(final long length, final long offset) {

        //Logger.v("query: length = " + length + ", offset = " + offset);

        if (null == organizer) {
            //Logger.w("query fail: null organizer");
            return this;
        }

        try {
            this.cursor = organizer.query(length, offset);

        } catch (Exception e) {
            //Logger.w("query fail: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return this;
    }
}