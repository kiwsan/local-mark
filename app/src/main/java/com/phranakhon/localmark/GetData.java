package com.phranakhon.localmark;

public class GetData {

    private int id;
    private String txt_name;
    private String txt_user_id;
    private String txt_date;
    private String txt_address;
    private String txt_width_area;
    private String txt_lat_lng;
    private String txt_str_address;

    public GetData(int id, String txt_name, String txt_user_id,
                   String txt_date, String txt_address, String txt_width_area,
                   String txt_lat_lng, String txt_str_address) {
        // TODO Auto-generated constructor stub

        this.id = id;
        this.txt_name = txt_name;
        this.txt_user_id = txt_user_id;
        this.txt_date = txt_date;
        this.txt_address = txt_address;
        this.txt_width_area = txt_width_area;
        this.txt_lat_lng = txt_lat_lng;
        this.txt_str_address = txt_str_address;
    }

    public int getId() {
        // TODO Auto-generated method stub
        return this.id;
    }

    public String get_txt_name() {
        // TODO Auto-generated method stub
        return this.txt_name;
    }

    public String get_txt_user_id() {
        // TODO Auto-generated method stub
        return this.txt_user_id;
    }

    public String get_txt_date() {
        // TODO Auto-generated method stub
        return this.txt_date;
    }

    public String get_txt_address() {
        // TODO Auto-generated method stub
        return this.txt_address;
    }

    public String get_txt_width_area() {
        // TODO Auto-generated method stub
        return this.txt_width_area;
    }

    public String get_txt_lat_lng() {
        // TODO Auto-generated method stub
        return this.txt_lat_lng;
    }

    public String get_txt_str_address() {
        // TODO Auto-generated method stub
        return this.txt_str_address;
    }
}

