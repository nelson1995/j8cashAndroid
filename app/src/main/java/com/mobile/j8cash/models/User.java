package com.mobile.j8cash.models;

import com.orm.SugarRecord;

public class User extends SugarRecord {
    public String name;
    public String username;
    public String email;
    public String phone;
    public String pin;
    public String country;
    public String currency;
    public String phone_code;
    public String country_code;
    public String token;
    public float wallet;
    public String invite_code;
    public int points;
    public String profile_pic_url;
}
