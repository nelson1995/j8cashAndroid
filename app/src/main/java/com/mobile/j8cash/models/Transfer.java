package com.mobile.j8cash.models;

import com.orm.SugarRecord;

public class Transfer extends SugarRecord {

    public int sender_id;
    public int receiver_id;
    public String amount;
    public String text;
    public String sender_date;
    public String receiver_date;

    public Client receiver;
    public Client sender;
}
