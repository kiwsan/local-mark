package com.phranakhon.localmark;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListViewData extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private FileActivity control;

    private ArrayList<GetData> listData = new ArrayList<GetData>();

    public AdapterListViewData(FileActivity control,
                               ArrayList<GetData> listData) {
        this.control = control;
        this.context = control.getBaseContext();
        this.mInflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    public int getCount() {

        return listData.size();
    }

    public Object getItem(int position) {

        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderListAdapter holderListAdapter;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_listview, null);
            holderListAdapter = new HolderListAdapter();

            holderListAdapter.txtName = (TextView) convertView
                    .findViewById(R.id.txtName);
            holderListAdapter.txtUserID = (TextView) convertView
                    .findViewById(R.id.txtUserID);
            holderListAdapter.txtDate = (TextView) convertView
                    .findViewById(R.id.txtDate);
            holderListAdapter.txtWidth = (TextView) convertView
                    .findViewById(R.id.txtWidthArea);
            holderListAdapter.btnOpen = (Button) convertView
                    .findViewById(R.id.btn_open);

            convertView.setTag(holderListAdapter);
        } else {

            holderListAdapter = (HolderListAdapter) convertView.getTag();
        }

        final int id = listData.get(position).getId();
        final String txt_name = listData.get(position).get_txt_name();
        final String txt_user_id = listData.get(position).get_txt_user_id();
        final String txt_date = listData.get(position).get_txt_date();
        final String txt_address = listData.get(position).get_txt_address();
        final String txt_width = listData.get(position).get_txt_width_area();
        final String txt_lat_lng = listData.get(position).get_txt_lat_lng();
        final String txt_str_address = listData.get(position)
                .get_txt_str_address();

        holderListAdapter.txtName.setText("ชื่อ : " + txt_name);
        holderListAdapter.txtUserID.setText("เลขที่บัตรประจำตัวประชาชน : " + txt_user_id);
        holderListAdapter.txtDate.setText("จำนวนไร่ : " + txt_width);
        holderListAdapter.txtWidth.setText("สถานที่ : " + txt_str_address);

        holderListAdapter.btnOpen
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        control.openPage(id, txt_name, txt_user_id, txt_date,
                                txt_width, txt_lat_lng, txt_str_address,
                                txt_address);
                    }
                });

        return convertView;
    }
}
