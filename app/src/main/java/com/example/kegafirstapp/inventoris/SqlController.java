package com.example.kegafirstapp.inventoris;

/**
 * Created by Kega on 8/29/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SqlController {

    private DataHelper dbhelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public SqlController(Context c) {
        ourcontext = c;
    }

    public SqlController open() throws SQLException {
        dbhelper = new DataHelper(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbhelper.close();
    }

    //UNTUK INSERT DATA
    /*public void insertData(String name, String lname) {

        ContentValues cv = new ContentValues();
        cv.put(MyDbHelper.MEMBER_FIRSTNAME, name);
        cv.put(MyDbHelper.MEMBER_LASTNAME, lname);
        database.insert(MyDbHelper.TABLE_MEMBER, null, cv);

    }*/

    public Cursor readEntry() {

        String[] allColumns = new String[] { DataHelper.INVENTORI_QRCODE, DataHelper.INVENTORI_BARANG};

        Cursor c = database.query(DataHelper.TABLE_INVENTORI, allColumns, null, null, null,
                null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }



}

