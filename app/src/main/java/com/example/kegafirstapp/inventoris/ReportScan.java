package com.example.kegafirstapp.inventoris;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kega on 8/31/2016.
 */
public class ReportScan extends ActionBarActivity {

    TableLayout tableScan;
    DataHelper dbhelper;
    TextView TotalItem,TotalScan;
    ListView qrlist;
    Button bAdd;

    private static final String TAG_INVENTORI = "inventori";
    private static final String TAG_QR = "No_qr_code";
    private static final String TAG_BARANG = "Jenis_barang";
    private static final String TAG_Status = "Status";

    public final static String ID_EXTRA = "com.example.kegafirstapp.inventoris.ReportScan._ID";

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_report);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Scan Report");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        tableScan = (TableLayout) findViewById(R.id.tableUndone);
        TotalItem = (TextView) findViewById(R.id.total_item);
        TotalScan = (TextView) findViewById(R.id.total_scan);
        qrlist = (ListView) findViewById(R.id.listview1);
        bAdd = (Button) findViewById(R.id.bAddUnd);

        dbhelper = new DataHelper(this);
        db = dbhelper.getReadableDatabase();
        db.beginTransaction();
        buildScanTable();
        fill_list();
        db.endTransaction();
        db.close();

        qrlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = ((TextView)view.findViewById(R.id.noqr_text)).getText().toString();
                System.out.println("ini isi text "+text);
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(), EditList.class);
                in.putExtra(ID_EXTRA, String.valueOf(text));
                startActivityForResult(in, 100);

            }
        });

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportScan.this.finish();
                Intent i = new Intent(getApplicationContext(),AddNew.class);
                startActivity(i);
            }
        });
    }

    private void buildScanTable() {
        String scanReport = "select "+TAG_QR+","+TAG_BARANG+" from "+TAG_INVENTORI+" where "+TAG_Status+"='Not_check'";
        Cursor mCur = db.rawQuery(scanReport,null);
        if (mCur.getCount()>0){
            while (mCur.moveToNext()){
                String noQr = mCur.getString(mCur.getColumnIndex(TAG_QR));
                String desc = mCur.getString(mCur.getColumnIndex(TAG_BARANG));
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String[] colText={noQr,desc};
                for(String text:colText) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setText(text);
                    row.addView(tv);
                }
                tableScan.addView(row);
            }
        }

        String cek1 = "SELECT COUNT (*) FROM "+TAG_INVENTORI+" WHERE "+TAG_Status+"= 'Not_check' or "+TAG_Status+"= 'Checked'";
        String cek2 = "SELECT COUNT (*) FROM "+TAG_INVENTORI+" WHERE "+TAG_Status+"= 'undefined' or "+TAG_Status+"= 'Checked'";

        String cek1Result = DatabaseUtils.stringForQuery(db,cek1,null);
        String cek2Result = DatabaseUtils.stringForQuery(db,cek2,null);

        TotalItem.setText(cek1Result);
        TotalScan.setText(cek2Result);
    }

    private void fill_list(){
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        String scanReport2 = "select "+TAG_QR+","+TAG_BARANG+" from "+TAG_INVENTORI+" where "+TAG_Status+"='undefined'";
        Cursor mCur = db.rawQuery(scanReport2,null);

        if (mCur.getCount()>0){
            while (mCur.moveToNext()){
                String noQr = mCur.getString(mCur.getColumnIndex(TAG_QR));
                String desc = mCur.getString(mCur.getColumnIndex(TAG_BARANG));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_QR, noQr);
                map.put(TAG_BARANG, desc);
                data.add(map);
            }
        }
        ListAdapter NoCoreAdapter = new SimpleAdapter(ReportScan.this, data,
                R.layout.custom_list, new String[]{TAG_QR}, new int[]{R.id.noqr_text} );
        qrlist.setAdapter(NoCoreAdapter);
    }
}
