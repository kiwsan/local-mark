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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AreaActivity extends AppCompatActivity {

    private String m_chosenDir = "";
    private boolean m_newFolderEnabled = true;
    final Context context = this;
    String datecreate, txt_latlng;
    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;
    private EditText username, userid, address, area;
    private StringBuilder strAddress, str;
    public static int TBID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datecreate = java.text.DateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime());

        dbHelper = new SQLiteHelper(this);
        database = dbHelper.getWritableDatabase();

        Cursor mCursor = database.rawQuery(
                "SELECT txt_lat_lng FROM area_db_table ORDER BY id DESC", null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {

                String txt_new_genLATLNG = mCursor.getString(mCursor
                        .getColumnIndex("txt_lat_lng"));

                String C_L = txt_new_genLATLNG.substring(2,
                        txt_new_genLATLNG.length());
                TBID = Integer.parseInt(C_L) + 1;

            }
        }
    }

    private void subMethodGetFileFromDataUser(final String chosenDir) {
        // TODO Auto-generated method stub
        /*** Read Text File SD Card ***/
        try {

            String path = chosenDir;
            File file = new File(path);

            ArrayList<String> srtArr = new ArrayList<String>();

            // List File
            File[] files = file.listFiles();
            for (File sfil : files) {
                if (sfil.isFile()) {
                    String fileName = sfil.toString().substring(
                            sfil.toString().lastIndexOf('/') + 1,
                            sfil.toString().length());
                    srtArr.add(fileName);
                }
            }

            file = null;
            final ListView lisView = (ListView) findViewById(R.id.listViewSelect);
            String[] DataText = {};
            DataText = srtArr.toArray(new String[srtArr.size()]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, DataText);
            lisView.setAdapter(adapter);
            lisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    // TODO Auto-generated method stub

                    String selectedNameFromList = (lisView
                            .getItemAtPosition(arg2).toString());

                    useW3cOrgDocumentBuilder(chosenDir, selectedNameFromList);

                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li
                            .inflate(
                                    R.layout.activity_insert_basic_data_from_user,
                                    null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setView(promptsView);

                    // Get Text Edit
                    username = (EditText) promptsView
                            .findViewById(R.id.editTextUserName);
                    userid = (EditText) promptsView
                            .findViewById(R.id.editTextID);
                    address = (EditText) promptsView
                            .findViewById(R.id.editTextAddress);

                    area = (EditText) promptsView
                            .findViewById(R.id.editTextWidthArea);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int s) {
                                            // get user input and set it
                                            // to result
                                            if (username.length() > 0
                                                    && userid.length() > 0
                                                    && datecreate.length() > 0
                                                    && address.length() > 0
                                                    && area.length() > 0) {

                                                save();
                                            } else {
                                                TTT();
                                            }
                                        }

                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int s) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            });
            /*** END >> Read Text File SD Card ***/

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(AreaActivity.this,
                    "Failed! = " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void TTT() {
        // TODO Auto-generated method stub
        Toast.makeText(this, "กรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
    }

    private void useW3cOrgDocumentBuilder(String chosenDir,
                                          String selectedNameFromList) {
        // TODO Auto-generated method stub

        String path = chosenDir;
        String kmlfile = selectedNameFromList;

        try {

            String kmlFile = path + "/" + kmlfile + "/";
            InputStream is = new FileInputStream(kmlFile);

            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document document = docBuilder.parse(is);

            NodeList listNameTag = null;
            NodeList listCoordinateTag = null;

            if (document == null) {

                txt_latlng = ("document coordinates point kml null..");
                return;
            }

            str = new StringBuilder();
            strAddress = new StringBuilder();
            // dealing with 'name' tagged elements
            listNameTag = document.getElementsByTagName("name");
            // showing 'name' tags
            for (int i = 0; i < listNameTag.getLength(); i++) {
                Node node = listNameTag.item(i);

                String text = node.getTextContent();
                strAddress.append(text);
                // get all attributes of the current �name� element (i-th hole)

            }

            // dealing with 'coordinates' tagged elements
            listCoordinateTag = document.getElementsByTagName("coordinates");
            // showing 'coordinates' tags
            for (int i = 0; i < listCoordinateTag.getLength(); i++) {
                String coordText = listCoordinateTag.item(i).getFirstChild()
                        .getNodeValue();
                str.append(coordText + "\n");
            }

            txt_latlng = (str.toString().trim());

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (ParserConfigurationException e) {

            e.printStackTrace();
        } catch (SAXException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void save() {
        // TODO Auto-generated method stub

        ContentValues values = new ContentValues();
        values.put("txt_name", username.getText().toString());
        values.put("txt_user_id", userid.getText().toString());
        values.put("txt_date", datecreate.toString());
        values.put("txt_address", address.getText().toString());
        values.put("txt_width_area", area.getText().toString());
        values.put("txt_lat_lng", "PN" + TBID);
        values.put("txt_str_address", strAddress.toString());

        database.insert("area_db_table", null, values);

        // Get Sub String Function
        // LatLng
        String _main_sub = "";
        String replaceOne = str.toString().trim().replaceAll(",", "");
        String replaceTwo = replaceOne.replaceAll(" ", "");
        for (int i = 0; i < replaceTwo.length(); i += 29) {

            _main_sub = replaceTwo.substring(i, i + 29);

            ContentValues sub_values = new ContentValues();
            sub_values.put("txt_lat_lng", "PN" + TBID);
            sub_values.put("txt_lng", _main_sub.substring(0, 10).toString());
            sub_values.put("txt_lat",
                    _main_sub.substring(10, _main_sub.length() - 10) + " "
                            + _main_sub.substring(19, _main_sub.length()));

            database.insert("sub_area_db_table", null, sub_values);
        }

        Toast.makeText(this, "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();
        // showList();
        Intent intent = new Intent(AreaActivity.this,
                FileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_sdcard:
                openSelecttion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSelecttion() {
        // TODO Auto-generated method stub
        DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(
                AreaActivity.this,
                new DirectoryChooserDialog.ChosenDirectoryListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        m_chosenDir = chosenDir;

                        subMethodGetFileFromDataUser(chosenDir);
                    }
                });

        directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
        directoryChooserDialog.chooseDirectory(m_chosenDir);
        m_newFolderEnabled = !m_newFolderEnabled;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_position_lat_lng_kmlfile, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
