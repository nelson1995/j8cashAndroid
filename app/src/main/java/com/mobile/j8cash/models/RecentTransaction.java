package com.mobile.j8cash.models;

import com.orm.SugarRecord;

import java.util.List;

public class RecentTransaction {
    public int wallet;
    public List<Airtime> airtime;
    public List<Deposit> deposits;
    public List<Transfer> transfers;


}
