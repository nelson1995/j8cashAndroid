package com.mobile.j8cash.models;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Until;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Deposit extends SugarRecord {

    @SerializedName("phone")
    public String phone;
    @SerializedName("amount")
    public int amount;
    @SerializedName("payment_method")
    public String payment_method;
    @SerializedName("tx_ref")
    public String tx_ref;
    @SerializedName("date")
    public String date;

    @SerializedName("wallet_balance")
    public float wallet_balance;
}
