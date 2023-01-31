package com.mobile.j8cash.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlutterwaveResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Transaction transaction;

    public class Transaction {
        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("txRef")
        @Expose
        public String txRef;
        @SerializedName("orderRef")
        @Expose
        public String orderRef;
        @SerializedName("flwRef")
        @Expose
        public String flwRef;
        @SerializedName("redirectUrl")
        @Expose
        public String redirectUrl;
        @SerializedName("device_fingerprint")
        @Expose
        public String deviceFingerprint;
        @SerializedName("settlement_token")
        @Expose
        public Object settlementToken;
        @SerializedName("cycle")
        @Expose
        public String cycle;
        @SerializedName("amount")
        @Expose
        public int amount;
        @SerializedName("charged_amount")
        @Expose
        public int chargedAmount;
        @SerializedName("appfee")
        @Expose
        public double appfee;
        @SerializedName("merchantfee")
        @Expose
        public int merchantfee;
        @SerializedName("merchantbearsfee")
        @Expose
        public int merchantbearsfee;
        @SerializedName("chargeResponseCode")
        @Expose
        public String chargeResponseCode;
        @SerializedName("raveRef")
        @Expose
        public String raveRef;
        @SerializedName("chargeResponseMessage")
        @Expose
        public String chargeResponseMessage;
        @SerializedName("authModelUsed")
        @Expose
        public String authModelUsed;
        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("IP")
        @Expose
        public String iP;
        @SerializedName("narration")
        @Expose
        public String narration;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("modalauditid")
        @Expose
        public String modalauditid;
        @SerializedName("vbvrespmessage")
        @Expose
        public String vbvrespmessage;
        @SerializedName("authurl")
        @Expose
        public String authurl;
        @SerializedName("vbvrespcode")
        @Expose
        public String vbvrespcode;
        @SerializedName("acctvalrespmsg")
        @Expose
        public Object acctvalrespmsg;
        @SerializedName("acctvalrespcode")
        @Expose
        public String acctvalrespcode;
        @SerializedName("paymentType")
        @Expose
        public String paymentType;
        @SerializedName("paymentPlan")
        @Expose
        public Object paymentPlan;
        @SerializedName("paymentPage")
        @Expose
        public Object paymentPage;
        @SerializedName("paymentId")
        @Expose
        public String paymentId;
        @SerializedName("fraud_status")
        @Expose
        public String fraudStatus;
        @SerializedName("charge_type")
        @Expose
        public String chargeType;
        @SerializedName("is_live")
        @Expose
        public int isLive;
        @SerializedName("createdAt")
        @Expose
        public String createdAt;
        @SerializedName("updatedAt")
        @Expose
        public String updatedAt;
        @SerializedName("deletedAt")
        @Expose
        public Object deletedAt;
        @SerializedName("customerId")
        @Expose
        public int customer_id;
        @SerializedName("AccountId")
        @Expose
        public int accountId;
        @SerializedName("customer.id")
        @Expose
        public int customerId;
        @SerializedName("customer.phone")
        @Expose
        public Object customerPhone;
        @SerializedName("customer.fullName")
        @Expose
        public String customerFullName;
        @SerializedName("customer.customertoken")
        @Expose
        public Object customerCustomertoken;
        @SerializedName("customer.email")
        @Expose
        public String customerEmail;
        @SerializedName("customer.createdAt")
        @Expose
        public String customerCreatedAt;
        @SerializedName("customer.updatedAt")
        @Expose
        public String customerUpdatedAt;
        @SerializedName("customer.deletedAt")
        @Expose
        public Object customerDeletedAt;
        @SerializedName("customer.AccountId")
        @Expose
        public int customerAccountId;
        @SerializedName("meta")
        @Expose
        public List<Object> meta = null;
        @SerializedName("flwMeta")
        @Expose
        public FlwMeta flwMeta;
    }
    public class FlwMeta {}
}
