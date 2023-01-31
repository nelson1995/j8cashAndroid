package com.mobile.j8cash.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class HomeTransaction extends SugarRecord {
    @Unique
    public String uuid;
    public String type;
    public int amount;
    public String date;
    public String phone;
    public String message;
}
