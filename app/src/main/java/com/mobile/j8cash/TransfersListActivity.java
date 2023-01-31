package com.mobile.j8cash;

import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mobile.j8cash.adapters.DepositAdapter;
import com.mobile.j8cash.adapters.TransfersTabAdapter;
import com.mobile.j8cash.fragments.ReceivedFragment;
import com.mobile.j8cash.fragments.SentFragment;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.models.Transfer;
import com.mobile.j8cash.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransfersListActivity extends BaseActivity {
    private AQuery aq;
    private TransfersTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Transfer> sentList;
    private List<Transfer> receivedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfers_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Money Transfers");
        aq = new AQuery(this);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TransfersTabAdapter(getSupportFragmentManager());

        getDeposits();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showFragments(){
        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.container).show();
        adapter.addFragment(new SentFragment(sentList), "SENT");
        adapter.addFragment(new ReceivedFragment(receivedList), "RECEIVED");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getDeposits(){
        aq.id(R.id.progress_layout).show();
        aq.id(R.id.container).hide();

        Call<List<Transfer>> call = RetrofitClient.getInstance().create(APIInterface.class).getTransfers();
        call.enqueue(new Callback<List<Transfer>>() {
            @Override
            public void onResponse(Call<List<Transfer>> call, Response<List<Transfer>> response) {
                if(response.isSuccessful()){
                    Utils.log("Transfers s -> "+response.body().size());
                    sentList = response.body();
                    getReceived();
                }else{
                    aq.toast("An error occurred.");
                    try {
                        Utils.log("Transfers e -> "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Transfer>> call, Throwable t) {
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("Failed to fetch transfers");
                }
                Utils.log("Transfers f -> " + t.getLocalizedMessage());
            }
        });
    }


    private void getReceived(){

        Call<List<Transfer>> call = RetrofitClient.getInstance().create(APIInterface.class).getReceivedTransfers();
        call.enqueue(new Callback<List<Transfer>>() {
            @Override
            public void onResponse(Call<List<Transfer>> call, Response<List<Transfer>> response) {
                if(response.isSuccessful()){
                    Utils.log("Transfers s -> "+response.body().size());
                    receivedList = response.body();
                    showFragments();
                }else{
                    aq.toast("An error occurred");
                    try {
                        Utils.log("Transfers e -> "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Transfer>> call, Throwable t) {
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("Failed to fetch transfers");
                }
                Utils.log("Transfers f -> " + t.getLocalizedMessage());
            }
        });
    }



}
