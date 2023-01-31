package com.mobile.j8cash.models;

public class WithdrawResponse {

    public String status; // error, success
    public String message;
    public Data data;

    public class Data{
        public int id;
        public String account_number;
        public String bank_code;
        public String full_name;
        public String created_at;
        public String currency;
        public String debit_currency;
        public int amount;
        public int fee;
        public String status;
        public String reference;
        public String narration;
        public String complete_message;
        public String bank_name;
        public int requires_approval;
        public int is_approved;
    }

//    {
//        "event.type": "Transfer",
//            "transfer": {
//        "id": 2643463,
//                "account_number": "256773343543",
//                "bank_code": "MPS",
//                "fullname": "Ben Rapha",
//                "date_created": "2020-08-01T20:46:04.000Z",
//                "currency": "UGX",
//                "debit_currency": "NGN",
//                "amount": 5500,
//                "fee": 500,
//                "status": "FAILED",
//                "reference": "Test-J8Cash-Withdraws1",
//                "meta": null,
//                "narration": "Akhlm Pstmn Trnsfr xx007",
//                "approver": null,
//                "complete_message": "DISBURSE FAILED: Insufficient funds in customer wallet",
//                "requires_approval": 0,
//                "is_approved": 1,
//                "bank_name": "FA-BANK"
//    }
//    }
}
