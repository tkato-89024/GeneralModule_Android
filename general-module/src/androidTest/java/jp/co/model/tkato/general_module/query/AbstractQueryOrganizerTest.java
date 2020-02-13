package jp.co.model.tkato.general_module.query;

import android.database.Cursor;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractQueryOrganizerTest {

    private AbstractQueryOrganizer aqo = new AbstractQueryOrganizer() {
        @Override
        public Cursor query(long length, long offset) {
            return null;
        }
    };

    @Test
    public void isDistinct() {
        //boolean distinct = aqo.isDistinct();
        //aqo.query(0,0);
    }

    @Test
    public void getProjection() {
        //String[] projection = aqo.getProjection();
    }

    @Test
    public void getSelection() {
        //final String selection = aqo.getSelection();
    }

    @Test
    public void getSelectionArgs() {
        //aqo.getSelectionArgs();
    }

    @Test
    public void getOrderBy() {
        //aqo.getOrderBy();
    }
}