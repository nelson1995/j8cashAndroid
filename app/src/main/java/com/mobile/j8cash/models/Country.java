package com.mobile.j8cash.models;

import java.util.ArrayList;
import java.util.List;

public class Country {

    public int id;
    public String name;
    public String phoneCode;
    public String currency;

    public static List<Country> getCountries(){
        List<Country> list = new ArrayList<>();

        Country ug = new Country();
        ug.id = 1;
        ug.name = "Uganda";
        ug.phoneCode = "256";
        ug.currency = "UGX";
        list.add(ug);

        Country drc = new Country();
        drc.id = 2;
        drc.name = "Democratic Republic of Congo, DRC";
        drc.phoneCode = "243";
        drc.currency = "USD";
        list.add(drc);
        return list;
    }

    public static String getPhoneCode(int country_id){
        if (country_id == 1){
            return "256";
        }
        if (country_id == 2){
            return "243";
        }
        return "";
    }

    public static String getCurrency(int country_id){
        if (country_id == 1){
            return "UGX";
        }
        if (country_id == 2){
            return "CDF";
        }
        return "";
    }
}
