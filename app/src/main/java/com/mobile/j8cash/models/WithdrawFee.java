package com.mobile.j8cash.models;
import java.util.List;

public class WithdrawFee {
//    {
//        "status":"success"
//        "message":"Transfer fee fetched"
//        "data":[
//        0:{
//        "currency":"UGX"
//        "fee_type":"value"
//        "fee":500
//    }
//        1:{
//        "currency":"UGX"
//        "fee_type":"percentage"
//        "fee":0
//    }
//]
//    }

    public String status;
    public String message;
    public List< Datum> data;

    public class Datum{
        public String currency;
        public String fee_type;
        public float fee;
    }
}
