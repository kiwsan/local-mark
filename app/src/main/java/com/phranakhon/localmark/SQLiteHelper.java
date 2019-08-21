package com.phranakhon.localmark;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Area_db";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE_MAIN = ""
            + "CREATE TABLE area_db_table (" + "id INTEGER PRIMARY KEY, "
            + "txt_name TEXT NOT NULL, " + "txt_user_id TEXT NOT NULL, "
            + "txt_date TEXT NOT NULL, " + "txt_address TEXT NOT NULL, "
            + "txt_width_area TEXT NOT NULL, " + "txt_lat_lng TEXT NOT NULL, "
            + "txt_str_address TEXT NOT NULL);";

    private static final String DB_CREATE_SUB = ""
            + "CREATE TABLE sub_area_db_table (" + "id INTEGER PRIMARY KEY, "
            + "txt_lat_lng TEXT NOT NULL, " + "txt_lng TEXT NOT NULL, "
            + "txt_lat TEXT NOT NULL);";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_MAIN);
        db.execSQL(DB_CREATE_SUB);
    }

    public ArrayList<HashMap<String, String>> SelectAllData(String TDID) {
        // TODO Auto-generated method stub

        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT txt_lat_lng, txt_lng, txt_lat FROM sub_area_db_table WHERE txt_lat_lng = '"
                    + TDID + "' ORDER BY id DESC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put("txt_lat_lng", cursor.getString(0));
                        map.put("txt_lng", cursor.getString(1));
                        map.put("txt_lat", cursor.getString(2));
                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgread database version from version" + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS member");
        onCreate(db);
    }
}
