package com.mobile.j8cash.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.aquery.AQuery;
import com.mobile.j8cash.APIInterface;
import com.mobile.j8cash.AccountManager;
import com.mobile.j8cash.AirtimeActivity;
import com.mobile.j8cash.DepositActivity;
import com.mobile.j8cash.R;
import com.mobile.j8cash.RetrofitClient;
import com.mobile.j8cash.SendToUsersActivity;
import com.mobile.j8cash.models.Airtime;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.models.HomeTransaction;
import com.mobile.j8cash.models.RecentTransaction;
import com.mobile.j8cash.models.Transfer;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{

    private LinearLayout containerT;
    private LinearLayout depositBtn, sendBtn, airtimeBtn;
    private AQuery aq;
    private List<HomeTransaction> homeTransactionList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        aq = new AQuery(root);
        containerT = root.findViewById(R.id.ln_transactions);

        int wallet = (int) Prefs.getFloat(AccountManager.WALLET,0);

        aq.id(R.id.wallet_text).text(Utils.getPrice(wallet));
        depositBtn = root.findViewById(R.id.btn_deposit);
        sendBtn = root.findViewById(R.id.btn_send);
        airtimeBtn = root.findViewById(R.id.btn_airtime);
        depositBtn.setOnClickListener(k->{
            startActivity(new Intent(getActivity(), DepositActivity.class));
        });

        sendBtn.setOnClickListener(k->{
            startActivity(new Intent(getActivity(), SendToUsersActivity.class));
        });

        airtimeBtn.setOnClickListener(l->{
            startActivity(new Intent(getActivity(), AirtimeActivity.class));
        });
        getTransactions();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void populateHome(List<HomeTransaction> homeTransactionList){
        if (this.homeTransactionList.size() == 0){
            aq.id(R.id.empty_layout).show();
            aq.id(R.id.progress_layout).hide();
            return;
        }

        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.empty_layout).hide();

        LayoutInflater inflater = getLayoutInflater();
        for(HomeTransaction a : this.homeTransactionList){
            View view = inflater.inflate(R.layout.transactions_list_item, null);
            AQuery aa = new AQuery(view);
            aa.id(R.id.t_title).text(a.message);
            aa.id(R.id.t_date).text(a.date);
//            aa.id(R.id.t_amount).text(a.amount);
//            aa.id(R.id.t_person).text(a.person);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 16);

            containerT.addView(view, params);
        }
    }

    private void getTransactions(){

        aq.id(R.id.progress_layout).show();
        aq.id(R.id.empty_layout).hide();

        Call<List<RecentTransaction>> call = RetrofitClient.getInstance().create(APIInterface.class).getRecentTransactions();
        call.enqueue(new Callback<List<RecentTransaction>>() {
            @Override
            public void onResponse(Call<List<RecentTransaction>> call, Response<List<RecentTransaction>> response) {
                if(response.isSuccessful()){
                    homeTransactionList = new ArrayList<>();
                    for (Deposit deposit : response.body().get(0).deposits){
                        HomeTransaction homeTransaction = new HomeTransaction();
                        homeTransaction.uuid = UUID.randomUUID().toString();
                        homeTransaction.amount = deposit.amount;
                        homeTransaction.type = "deposit";
                        homeTransaction.date = deposit.date;
                        homeTransaction.message = "Deposited " + Utils.getPrice(deposit.amount);
                        homeTransactionList.add(homeTransaction);
                    }
                    for (Transfer transfer : response.body().get(0).transfers){
                        HomeTransaction homeTransaction = new HomeTransaction();
                        homeTransaction.uuid = UUID.randomUUID().toString();
                        homeTransaction.amount = Integer.parseInt(transfer.amount);
                        homeTransaction.type = "transfer";
                        homeTransaction.date = transfer.sender_date;
                        homeTransaction.message = "Sent "+ Utils.getPrice(Integer.parseInt(transfer.amount));
                        homeTransactionList.add(homeTransaction);
                    }
                    for (Airtime airtime : response.body().get(0).airtime){
                        HomeTransaction homeTransaction = new HomeTransaction();
                        homeTransaction.uuid = UUID.randomUUID().toString();
                        homeTransaction.amount = Integer.parseInt(airtime.amount);
                        homeTransaction.type = "airtime";
                        homeTransaction.date = airtime.date;
                        homeTransaction.message = "Bought airtime of "+ Utils.getPrice(Integer.parseInt(airtime.amount));
                        homeTransactionList.add(homeTransaction);
                    }
                    populateHome(homeTransactionList);

                }else{
                    try {
                        Utils.logE("onError : "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    aq.toast("An error occurred, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<RecentTransaction>> call, Throwable t) {
                if (t instanceof IOException) {
                    aq.toast("No internet connection");

                } else {
                    aq.toast("System failure, try again later");
                }
            }
        });
    }

}