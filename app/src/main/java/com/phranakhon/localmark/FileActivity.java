package com.phranakhon.localmark;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listHistory;

    private ArrayList<GetData> listData = new ArrayList<GetData>();

    public int postion;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listHistory = (ListView) findViewById(R.id.list_area);

        dbHelper = new SQLiteHelper(this);
        database = dbHelper.getReadableDatabase();

        showList();
    }

    private void showList() {
        // TODO Auto-generated method stub
        getDataList();
        listHistory.setAdapter(new AdapterListViewData(this, listData));
    }

    private void getDataList() {

        Cursor mCursor = database.query(true, "area_db_table", new String[]{
                        "id", "txt_name", "txt_user_id", "txt_date", "txt_address",
                        "txt_width_area", "txt_lat_lng", "txt_str_address"}, null,
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            listData.clear();
            if (mCursor.getCount() > 0) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex("id"));
                    String txt_name = mCursor.getString(mCursor
                            .getColumnIndex("txt_name"));
                    String txt_user_id = mCursor.getString(mCursor
                            .getColumnIndex("txt_user_id"));
                    String txt_date = mCursor.getString(mCursor
                            .getColumnIndex("txt_date"));
                    String txt_address = mCursor.getString(mCursor
                            .getColumnIndex("txt_address"));
                    String txt_width_area = mCursor.getString(mCursor
                            .getColumnIndex("txt_width_area"));
                    String txt_lat_lng = mCursor.getString(mCursor
                            .getColumnIndex("txt_lat_lng"));
                    String txt_str_address = mCursor.getString(mCursor
                            .getColumnIndex("txt_str_address"));

                    listData.add(new GetData(id, txt_name, txt_user_id,
                            txt_date, txt_address, txt_width_area, txt_lat_lng,
                            txt_str_address));
                } while (mCursor.moveToNext());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_manage:
                openManagement();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Next Page From Select File To Insert From Database
    private void openManagement() {
        // TODO Auto-generated method stub
        startActivity(new Intent(FileActivity.this,
                AreaActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.management_file, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openPage(final int id, final String txt_name,
                         final String txt_user_id, final String txt_date,
                         final String txt_width, final String txt_lat_lng,
                         final String txt_str_address, final String txt_address) {
        // TODO Auto-generated method stub

        final AlertDialog.Builder singlechoicedialog = new AlertDialog.Builder(
                this);
        final CharSequence[] Report_items = {"ข้อมูลเอกสาร", "แก้ไขข้อมูล",
                "ลบข้อมูล"};
        singlechoicedialog.setTitle("โปรดเลือก");
        singlechoicedialog.setSingleChoiceItems(Report_items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        postion = item;

                        String value = Report_items[item].toString();
                        System.out.println("Selected position::" + value);

                        if (postion == 0) {

                            Intent intent = new Intent(FileActivity.this,
                                    DetailActivity.class);
                            // intent.putExtra("ID", id);
                            intent.putExtra("NAME", txt_name);
                            intent.putExtra("USERID", txt_user_id);
                            intent.putExtra("DATE", txt_date);
                            intent.putExtra("WIDTH", txt_width);
                            intent.putExtra("LATLNG", txt_lat_lng);
                            intent.putExtra("AREAADDRESS", txt_str_address);
                            intent.putExtra("ADDRESS", txt_address);
                            startActivity(intent);
                        } else if (postion == 1) {

                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(
                                    R.layout.prompts_edit_dialog, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInputName = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInputName);
                            final EditText userInputID = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInputID);
                            final EditText userInputAddress = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInputAddress);
                            final EditText userInputArea = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInputArea);
                            // Get Text Edit
                            userInputName.setText(txt_name);
                            userInputID.setText(txt_user_id);
                            userInputAddress.setText(txt_address);
                            userInputArea.setText(txt_width);

                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int s) {
                                                    // get user input and set it
                                                    // to result
                                                    // edit text Update Type
                                                    ContentValues values = new ContentValues();
                                                    values.put("id", id);
                                                    values.put("txt_name",
                                                            userInputName
                                                                    .getText()
                                                                    .toString());
                                                    values.put("txt_user_id",
                                                            userInputID
                                                                    .getText()
                                                                    .toString());
                                                    values.put("txt_address",
                                                            userInputAddress
                                                                    .getText()
                                                                    .toString());
                                                    values.put(
                                                            "txt_width_area",
                                                            userInputArea
                                                                    .getText()
                                                                    .toString());

                                                    database.update(
                                                            "area_db_table",
                                                            values, "id = ?",
                                                            new String[]{""
                                                                    + id});
                                                    Toast.makeText(
                                                            getApplicationContext(),
                                                            "Successfully",
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                    showList();
                                                }
                                            })
                                    .setNegativeButton(
                                            "Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int s) {

                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder
                                    .create();

                            // show it
                            alertDialog.show();
                        } else {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    FileActivity.this);
                            alertDialog.setTitle("ยืนยัน...");
                            alertDialog.setMessage("ต้องการลบข้อมูลหรือไม่?");
                            alertDialog.setIcon(R.drawable.delete);
                            alertDialog.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                            // Write your code here to invoke
                                            database.delete("area_db_table",
                                                    "id = " + id, null);
                                            showList();

                                            // YES event
                                        }
                                    });

                            alertDialog.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // Write your code here to invoke NO
                                            // event
                                            dialog.cancel();
                                        }
                                    });

                            // Showing Alert Message
                            alertDialog.show();
                        }
                        dialog.cancel();
                    }
                });

        AlertDialog alert_dialog = singlechoicedialog.create();
        alert_dialog.show();
        alert_dialog.getListView().setItemChecked(postion, true);
    }
}
