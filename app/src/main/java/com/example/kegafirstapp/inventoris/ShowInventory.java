package com.example.kegafirstapp.inventoris;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

/**
 * Created by Kega on 8/29/2016.
 */
public class ShowInventory extends ActionBarActivity {

    TableLayout tableInvent;
    SqlController sqlcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_list);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Inventory List");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        tableInvent = (TableLayout) findViewById(R.id.tableLayout1);
        sqlcon = new SqlController(this);
        BuildTable();
    }

    private void BuildTable() {

        sqlcon.open();
        Cursor c = sqlcon.readEntry();

        int rows = c.getCount();
        int cols = c.getColumnCount();

        c.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
                tv.setBackgroundResource(R.drawable.cell_shape);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(0, 5, 0, 5);

                tv.setText(c.getString(j));

                row.addView(tv);

            }

            c.moveToNext();

            tableInvent.addView(row);

        }
        sqlcon.close();


    }
}
