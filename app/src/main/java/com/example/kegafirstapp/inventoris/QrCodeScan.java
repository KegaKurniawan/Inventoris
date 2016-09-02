package com.example.kegafirstapp.inventoris;

/**
 * Created by Kega on 8/30/2016.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QrCodeScan extends ActionBarActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private static final String TAG_INVENTORI = "inventori";
    private static final String TAG_QR = "No_qr_code";
    private static final String TAG_BARANG = "Jenis_barang";
    private static final String TAG_Status = "Status";
    private static final String TAG_Gambar = "Gambar";

    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Scan QR Code");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        dbHelper = new DataHelper(this);
    }

    public void onClick(View v){
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        //Do anything with result here :D
        Log.w("handleResult", result.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage(result.getText());

        System.out.println(result);

        String kode = result.getText();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String cek = "SELECT COUNT (*) FROM "+TAG_INVENTORI+" WHERE "+TAG_QR+"= '"+kode+"'";

        int nomorCek = (int) DatabaseUtils.longForQuery(db,cek,null);

        if(nomorCek==0){
            String insert2 = "insert or ignore into inventori("+TAG_QR+","+TAG_BARANG+","+TAG_Status+
                    ") values('"+kode+"','---','undefined')";
            db.execSQL(insert2);
            Toast.makeText(getApplicationContext(), "new data added", Toast.LENGTH_LONG).show();
        }else{
            String UpdateStatus = "update "+TAG_INVENTORI+" set "+TAG_Status+"= 'Checked' where "+TAG_QR+
                    "= '"+kode+"'";
            db.execSQL(UpdateStatus);
            Toast.makeText(getApplicationContext(), "Check", Toast.LENGTH_LONG).show();
        }

        builder.setNeutralButton("Press to continue scan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mScannerView.resumeCameraPreview(QrCodeScan.this);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
