package com.example.kegafirstapp.inventoris;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kegafirstapp.inventoris.JSONparser;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity{

    //URL to get JSON
    private static String url = "http://easygaming.esy.es/ekspor_impor/daftar_inventori.php";

    //TAG JSON
    private static final String TAG_INVENTORI = "inventori";
    private static final String TAG_QR = "No_qr_code";
    private static final String TAG_BARANG = "Jenis_barang";
    private static final String TAG_Status = "Status";
    private static final String TAG_Gambar = "Gambar";

    JSONArray inventori = null;

    JSONparser jParser = new JSONparser();

    DataHelper dbHelper;
    Button btnRetrieve,btnInvent,btnDelete,btnScan, btnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Main Menu");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dbHelper = new DataHelper(this);
        btnRetrieve = (Button)findViewById(R.id.bRetrieve);
        btnInvent = (Button)findViewById(R.id.bShowInvent);
        btnDelete = (Button)findViewById(R.id.bDeleteInvent);
        btnScan = (Button) findViewById(R.id.bScan);
        btnReport = (Button) findViewById(R.id.bCheck);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_JsonInventori();
            }
        });

        btnInvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ShowInventory.class);
                startActivity(i);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_deleteInventory();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),QrCodeScan.class);
                startActivity(i);
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ReportScan.class);
                startActivity(i);
            }
        });
    }

    private void get_JsonInventori(){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jParser.makeHttpRequest(url,"GET",params);
        Log.d("All inventori : ",json.toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String create_table = "create table IF NOT EXISTS inventori(No_qr_code text PRIMARY KEY, Jenis_barang text null, Status text null);";
        Log.d("Data", "onCreate: " + create_table);
        db.execSQL(create_table);

        if(json != null){
            try{
                inventori = json.getJSONArray(TAG_INVENTORI);

                for(int i = 0; i < inventori.length();i++){
                    json = inventori.getJSONObject(i);

                    String qr_code = json.getString(TAG_QR);
                    String barang = json.getString(TAG_BARANG);
                    String status = json.getString(TAG_Status);
                    String gambar = json.getString(TAG_Gambar);

                    System.out.println("ini QR code-nya : "+qr_code+" ini" +
                            " ini barangnya : "+barang+" ini statusnya : "+status+"" +
                            " ini gambarnya : "+gambar);

                    String insert = "insert or ignore into inventori("+TAG_QR+","+TAG_BARANG+","+TAG_Status+
                            ") values('"+qr_code+"','"+barang+"','"+status+"')";

                    db.execSQL(insert);
                }

                Toast.makeText(getApplicationContext(), "Data Already Stored", Toast.LENGTH_LONG).show();

            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }

    }

    private void get_deleteInventory(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("Delete from "+TAG_INVENTORI);
        //db.execSQL("DROP TABLE inventori");
    }
}
