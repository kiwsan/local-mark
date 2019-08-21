package com.phranakhon.localmark;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    TextView txt_name, txt_user_id, txt_date, txt_width, txt_lat_lng,
            txt_area_address, txt_address;
    String _txt_name, _txt_user_id, _txt_date, _txt_width, _txt_lat_lng,
            _txt_str_address, _txt_address;

    String _I_LNG = "";
    String _I_LAT = "";
    String _I_L = "";
    String path;
    String TTPA;

    ArrayList<HashMap<String, String>> MultiColumnList;

    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        _txt_name = (String) bundle.get("NAME");
        _txt_user_id = (String) bundle.get("USERID");
        _txt_date = (String) bundle.get("DATE");
        _txt_width = (String) bundle.get("WIDTH");
        _txt_lat_lng = (String) bundle.get("LATLNG");
        _txt_str_address = (String) bundle.get("AREAADDRESS");
        _txt_address = (String) bundle.get("ADDRESS");

        txt_name = (TextView) findViewById(R.id.str_name);
        txt_user_id = (TextView) findViewById(R.id.str_user_id);
        txt_date = (TextView) findViewById(R.id.str_date);
        txt_width = (TextView) findViewById(R.id.str_sum_area);
        txt_area_address = (TextView) findViewById(R.id.str_address);
        txt_address = (TextView) findViewById(R.id.str_add);

        txt_name.setText("����-���ʡ��: " + _txt_name);
        txt_user_id.setText("�Ţ����Шӵ�ǻ�ЪҪ�: " + _txt_user_id);
        txt_date.setText("�ѹ/����: " + _txt_date);
        txt_width.setText(_txt_width);
        txt_area_address.setText("ʶҹ���: " + _txt_str_address);
        txt_address.setText("�������: " + _txt_address);

        dbHelper = new SQLiteHelper(this);
        MultiColumnList = dbHelper.SelectAllData(_txt_lat_lng);

        // listView1
        ListView lisView1 = (ListView) findViewById(R.id.listViewMulticolumn);

        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(this, MultiColumnList,
                R.layout.activity_multi_column, new String[]{"txt_lat_lng",
                "txt_lng", "txt_lat"}, new int[]{R.id.Col_ID,
                R.id.Col_LNG, R.id.Col_LAT});
        lisView1.setAdapter(sAdap);

    }

    private void subMethodInsertDataFromTxt() {
        // TODO Auto-generated method stub

        String txtData = "���-���ʡ��: " + _txt_name.toString() + "\n"
                + "�Ţ����Шӵ�ǻ�ЪҪ�: " + _txt_user_id.toString() + "\n"
                + "�ѹ���: " + _txt_date.replace(",", "").toString() + "\n"
                + "�������: " + _txt_address.toString() + "\n" + "�ӹǹ: "
                + _txt_width.toString() + " ���" + "," + "\n" + "ʶҹ���: "
                + _txt_str_address.toString();

        database = dbHelper.getWritableDatabase();
        Cursor mCursor = database
                .rawQuery(
                        "SELECT txt_lng, txt_lat FROM sub_area_db_table WHERE txt_lat_lng = 'PN0'",
                        null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {

                if (mCursor.getCount() > 0) {
                    do {

                        String _lng = mCursor.getString(mCursor
                                .getColumnIndex("txt_lng"));
                        String _lat = mCursor.getString(mCursor
                                .getColumnIndex("txt_lat"));

                        _I_LNG += _lng;
                        _I_LAT += _lat;
                        _I_L += "" + _lng + " " + _lat + "\n";

                    } while (mCursor.moveToNext());
                }
            }
        }

        try {

            String sdcard = Environment.getExternalStorageDirectory()
                    .toString();
            TTPA = "/Local Database/" + _txt_name.toString() + ".csv";
            path = sdcard + "/Local Database/" + _txt_name.toString() + ".csv";

            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(txtData + "\n" + "" + "�ͧ�Ԩٴ" + "          "
                    + "�еԨٴ" + "\n" + _I_L);
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_print:
                print();
                return true;
            case R.id.action_detail:
                openDetail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Next Page From Select File To Insert From Database
    private void openDetail() {
        // TODO Auto-generated method stub

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        // Setting Dialog Title
        alertDialog.setTitle("Save File...");
        // Setting Dialog Message
        alertDialog.setMessage("��ͧ��� Export ��� �������?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_action_sd_storage);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User pressed YES button. Write Logic Here
                        subMethodInsertDataFromTxt();
                        Toast.makeText(
                                getBaseContext(),
                                "Export �������: '" + _txt_name.toString()
                                        + ".csv'", Toast.LENGTH_SHORT).show();
                    }
                });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User pressed No button. Write Logic Here
                        Toast.makeText(getApplicationContext(), "No",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Setting Netural "Cancel" Button
        alertDialog.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User pressed Cancel button. Write Logic Here
                        Toast.makeText(getApplicationContext(), "Cancel",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    private void print() {
        // TODO Auto-generated method stub
        if (isNetworkAvailable() == false) {
            Toast.makeText(this,
                    "Network connection not available, Please try later",
                    Toast.LENGTH_SHORT).show();
        } else {

            subMethodInsertDataFromTxt();
            File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + TTPA);
            Intent printIntent = new Intent(this,
                    PrintActivity.class);
            printIntent.setDataAndType(Uri.fromFile(file), "text/csv");
            printIntent.putExtra("title", "Android Print..");
            startActivity(printIntent);

            Toast.makeText(getBaseContext(),
                    "�������: '" + _txt_name.toString() + "'",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network", "***Available***");
            return true;
        }
        Log.e("Network", "***Not Available***");
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.management_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
