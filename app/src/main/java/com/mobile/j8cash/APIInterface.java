package com.mobile.j8cash;

import com.google.gson.JsonObject;
import com.mobile.j8cash.models.Airtime;
import com.mobile.j8cash.models.AirtimeResponse;
import com.mobile.j8cash.models.Client;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.models.InviteCodeResponse;
import com.mobile.j8cash.models.PINResponse;
import com.mobile.j8cash.models.RecentTransaction;
import com.mobile.j8cash.models.SendResponse;
import com.mobile.j8cash.models.Transfer;
import com.mobile.j8cash.models.UpdateProfile;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.models.VerificationCode;
import com.mobile.j8cash.models.Withdraw;
import com.mobile.j8cash.models.WithdrawFee;
import com.mobile.j8cash.models.WithdrawResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("login")
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<User> register(@Field("name") String name, @Field("username") String username, @Field("email") String Email, @Field("phone") String phone, @Field("country") String country, @Field("password") String password);

    @POST("verify_phone")
    Call<VerificationCode> sendCode(@Query("phone")String phone);

    @POST("confirm_code")
    Call<VerificationCode> confirmCode(@Query("phone")String phone, @Query("code") String code);

    @POST("create_pin")
    Call<PINResponse> create_pin(@Query("user_id") int user_id, @Query("pin") String pin);

    @POST("deposit")
    Call<Deposit> deposit(@Query("payment_method") String paymentMethod, @Query("phone") String phone, @Query("amount") float amount, @Query("tx_ref") String tx_ref);

    @GET("get_deposits")
    Call<List<Deposit>> getDeposits();

    @GET("users")
    Call<List<Client>> getUsers();

    @POST("send")
    Call<SendResponse> send(@Query("receiver_id") int receiver_id, @Query("amount") int amount, @Query("text") String reason);

    @POST("buy_airtime")
    Call<AirtimeResponse> buy_airtime(@Query("phone") String phone, @Query("amount") float amount, @Query("currency") String currencyCode);

    @GET("get_airtime")
    Call<List<Airtime>> getAirtimeList();

    @GET("get_transfers")
    Call<List<Transfer>> getTransfers();

    @GET("get_received")
    Call<List<Transfer>> getReceivedTransfers();

    @POST("update_profile")
    Call<UpdateProfile> update_profile(@Query("email") String email, @Query("name") String name, @Query("username") String username);

    @Multipart
    @POST("update_profile")
    Call<UpdateProfile> update_profile_pic(@Query("email") String email, @Query("name") String name, @Query("username") String username, @Part() MultipartBody.Part file);

    @POST("reward_points")
    Call<InviteCodeResponse> postInviteCode(@Query("invite_code") String invite_code);

    @GET("recent_transactions")
    Call<List<RecentTransaction>> getRecentTransactions();

    @GET("get_withdraws")
    Call<List<Withdraw>> getWithdraws();

    @POST("transfers")
    Call<WithdrawResponse> withdraw(@Body JsonObject data);

    @POST("withdraw")
    Call<Withdraw> postWithdraw(@Query("amount") int amount, @Query("tx_ref") String tx_ref, @Query("phone") String phone);

    @GET("transfers/fee")
    Call<WithdrawFee> getFee(@Query("amount") String amount,  @Query("currency") String currency,  @Query("type") String type);

}
