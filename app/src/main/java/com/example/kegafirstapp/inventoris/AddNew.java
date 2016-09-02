package com.example.kegafirstapp.inventoris;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Kega on 9/2/2016.
 */
public class AddNew extends ActionBarActivity {

    EditText textQr,textdesc;
    Button btnSubmit;

    DataHelper dbHelper;

    private static final String TAG_INVENTORI = "inventori";
    private static final String TAG_QR = "No_qr_code";
    private static final String TAG_BARANG = "Jenis_barang";
    private static final String TAG_Status = "Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_or_update_item);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add New Data");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        textQr = (EditText) findViewById(R.id.txtNoQr);
        textdesc = (EditText) findViewById(R.id.txtDesc);
        btnSubmit = (Button) findViewById(R.id.bSubmit);
        dbHelper = new DataHelper(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_data();

            }
        });
    }

    private void submit_data() {
        String get_qr = textQr.getText().toString();
        String get_desc = textdesc.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String insert = "insert into "+TAG_INVENTORI+"("+TAG_QR+","+TAG_BARANG+","+TAG_Status+
                ") values('"+get_qr+"','"+get_desc+"','undefined')";
        db.execSQL(insert);
        Toast.makeText(getApplicationContext(), "Data has been store", Toast.LENGTH_LONG).show();
        AddNew.this.finish();
        Intent i = new Intent(getApplicationContext(),ReportScan.class);
        startActivity(i);
    }
}
