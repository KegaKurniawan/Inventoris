package com.example.kegafirstapp.inventoris;

/**
 * Created by Kega on 8/29/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

    public static final String TABLE_INVENTORI = "inventori";
    public static final String INVENTORI_QRCODE= "No_qr_code";
    public static final String INVENTORI_BARANG = "Jenis_barang";
    public static final String INVENTORI_STATUS = "Status";
    //public static final String INVENTORI_GAMBAR = "Gambar";

    private static final String DATABASE_NAME = "inventori.db";
    private static final int DATABASE_VERSION=1;
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table inventori(No_qr_code text PRIMARY KEY, Jenis_barang text null, Status text null);";
        Log.d("Data", "onCreate: " + create_table);
        db.execSQL(create_table);
        //String inset_sql = "INSERT INTO biodata (no, nama, tgl, jk, alamat) VALUES ('1001', 'Fathur', '1994-02-03', 'Laki-laki','Jakarta');";
        //db.execSQL(inset_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS inventori");
        onCreate(db);

    }
}
