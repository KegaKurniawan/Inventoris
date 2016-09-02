package com.example.kegafirstapp.inventoris;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Kega on 9/1/2016.
 */
public class EditList extends ActionBarActivity {

    String passedVar;
    DataHelper dbhelper;
    EditText txtNoQr, txtDesc;
    Button btnSubmit;

    private static final String TAG_INVENTORI = "inventori";
    private static final String TAG_QR = "No_qr_code";
    private static final String TAG_BARANG = "Jenis_barang";
    private static final String TAG_Status = "Status";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_or_update_item);
        setContentView(R.layout.edit_or_update_item);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Data");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        passedVar = getIntent().getStringExtra(ReportScan.ID_EXTRA);

        System.out.println("ini udah dipass variablenya ; "+passedVar);

        txtNoQr = (EditText) findViewById(R.id.txtNoQr);
        txtDesc = (EditText) findViewById(R.id.txtDesc);
        btnSubmit = (Button) findViewById(R.id.bSubmit);

        dbhelper = new DataHelper(this);

        fill_text();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_list();
            }
        });
    }

    private void fill_text(){
        String scanReport2 = "select "+TAG_QR+","+TAG_BARANG+" from "+TAG_INVENTORI+" where "+TAG_QR+"='"+passedVar+"'";
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor mCur = db.rawQuery(scanReport2,null);

        if (mCur.getCount()>0){
            while (mCur.moveToNext()){
                String desc = mCur.getString(mCur.getColumnIndex(TAG_BARANG));
                txtNoQr.setText(passedVar);
                txtDesc.setText(desc);
            }
        }
    }

    private void update_list(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String new_desc = txtDesc.getText().toString();
        String updateList =  "update "+TAG_INVENTORI+" set "+TAG_BARANG+"= '"+new_desc+"' where "+TAG_QR+"= '"+passedVar+"'";
        db.execSQL(updateList);
        Toast.makeText(getApplicationContext(), "Data update succes", Toast.LENGTH_LONG).show();
    }
}
