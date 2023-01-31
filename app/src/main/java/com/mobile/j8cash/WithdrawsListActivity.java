package com.mobile.j8cash;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aquery.AQuery;
import com.mobile.j8cash.adapters.WithdrawAdapter;
import com.mobile.j8cash.models.Withdraw;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawsListActivity extends BaseActivity {
    private RecyclerView depositsRecyclerView;
    private WithdrawAdapter adapter;
    private AQuery aq;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraws_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Withdraws");
        aq = new AQuery(this);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDeposits(false);
            }
        });
        depositsRecyclerView = findViewById(R.id.deposits_recycler_view);
        depositsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        getDeposits(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }


    private void getDeposits(boolean hidelist){
        if(hidelist){
            aq.id(R.id.progress_layout).show();
            aq.id(R.id.deposits_recycler_view).hide();
            aq.id(R.id.rtl_empty_container).hide();
        }


        Call<List<Withdraw>> call = RetrofitClient.getInstance().create(APIInterface.class).getWithdraws();
        call.enqueue(new Callback<List<Withdraw>>() {
            @Override
            public void onResponse(Call<List<Withdraw>> call, Response<List<Withdraw>> response) {
                if(response.isSuccessful()){
                    loadDeposits(response.body());
                }else{
                    aq.toast("An error occurred, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<Withdraw>> call, Throwable t) {
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("System failure, try again later");
                }
            }
        });
    }

    private void loadDeposits(List<Withdraw> depositList){

        if(depositList.size() == 0){
            showEmptyLayout("No recent withdraws");
            return;
        }

        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.deposits_recycler_view).show();
        aq.id(R.id.rtl_empty_container).hide();

        adapter = new WithdrawAdapter(this, depositList );
        depositsRecyclerView.setAdapter(adapter);
        refreshLayout.setRefreshing(false);

    }

    private void showEmptyLayout(String msg){
        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.deposits_recycler_view).hide();
        aq.id(R.id.rtl_empty_container).show();
        aq.id(R.id.icon_empty).image(R.drawable.ic_withdraw_alt);
        aq.id(R.id.txt_empty_message).text(msg);
    }

}
