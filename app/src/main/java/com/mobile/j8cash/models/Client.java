package com.mobile.j8cash.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

public class Client extends SugarRecord {
    public String name;
    public String username;
    public String email;
    public String phone;
    public String pin;
    public int country;
    public String currency;
    public String token;
    public float wallet;

    public static List<Client> getClients()
    {
        List<Client> list = new ArrayList<>();

        Client c = new Client();
        c.name = "Alphonse Jasper";
        c.phone = "+2567 387 75433";
        list.add(c);

        Client c1 = new Client();
        c1.name = "Jean Bosco";
        c1.phone = "+2437 453 56566";
        list.add(c1);

        Client c2 = new Client();
        c2.name = "Hope Mary";
        c2.phone = "+2567 456 23422";
        list.add(c2);

        Client c3 = new Client();
        c3.name = "Tuza Cedric";
        c3.phone = "+2437 453 5656";
        list.add(c3);

        Client c4 = new Client();
        c4.name = "Alphonse Jasper";
        c4.phone = "+2567 387 75433";
        list.add(c4);

        Client c5 = new Client();
        c5.name = "Jean Bosco";
        c5.phone = "+2437 453 56566";
        list.add(c5);

        Client c6 = new Client();
        c6.name = "Hope Mary";
        c6.phone = "+2567 456 23422";
        list.add(c6);

        Client c7 = new Client();
        c7.name = "Tuza Cedric";
        c7.phone = "+2437 453 5656";
        list.add(c7);


        Client c8 = new Client();
        c8.name = "Alphonse Jasper";
        c8.phone = "+2437 387 75433";
        list.add(c8);

        Client c11 = new Client();
        c11.name = "Jean Bosco";
        c11.phone = "+2567 453 56566";
        list.add(c11);

        Client c21 = new Client();
        c21.name = "Hope Mary";
        c21.phone = "+2567 456 23422";
        list.add(c21);

        Client c31 = new Client();
        c31.name = "Tuza Cedric";
        c31.phone = "+2437 453 5656";
        list.add(c31);

        Client c41 = new Client();
        c41.name = "Alphonse Jasper";
        c41.phone = "+2567 387 75433";
        list.add(c41);

        Client c51 = new Client();
        c51.name = "Jean Bosco";
        c51.phone = "+2437 453 56566";
        list.add(c51);

        Client c61 = new Client();
        c61.name = "Hope Mary";
        c61.phone = "+2567 456 23422";
        list.add(c61);

        Client c71 = new Client();
        c71.name = "Tuza Cedric";
        c71.phone = "+2437 453 5656";
        list.add(c71);

        return list;
    }
}
