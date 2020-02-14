/*
 GeneralModule_Android Cipherer

 Copyright (c) 2019 tkato

 This software is released under the MIT License.
 http://opensource.org/licenses/mit-license.php
 */
package jp.co.model.tkato.general_module.cursor;

import android.database.Cursor;

public interface ICursorer {

    Cursor getCursor();
    int getCountByCursor();
    Cursorer query();
    Cursorer query(final long length, final long offset);
}
