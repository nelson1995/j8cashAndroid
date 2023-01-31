package com.mobile.j8cash;

import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.j8cash.adapters.DepositAdapter;
import com.mobile.j8cash.models.Deposit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepositsListActivity extends BaseActivity {
    private RecyclerView depositsRecyclerView;
    private DepositAdapter adapter;
    private AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposits_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Deposits");
        aq = new AQuery(this);
        depositsRecyclerView = findViewById(R.id.deposits_recycler_view);
        depositsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        getDeposits();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }


    private void getDeposits(){
        aq.id(R.id.progress_layout).show();
        aq.id(R.id.deposits_recycler_view).hide();
        aq.id(R.id.rtl_empty_container).hide();

        Call<List<Deposit>> call = RetrofitClient.getInstance().create(APIInterface.class).getDeposits();
        call.enqueue(new Callback<List<Deposit>>() {
            @Override
            public void onResponse(Call<List<Deposit>> call, Response<List<Deposit>> response) {
                if(response.isSuccessful()){
                    loadDeposits(response.body());
                }else{
                    aq.toast("An error occurred, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<Deposit>> call, Throwable t) {
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("System failure, try again later");
                }
            }
        });
    }

    private void loadDeposits(List<Deposit> depositList){

        if(depositList.size() == 0){
            showEmptyLayout("No deposits yet");
            return;
        }

        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.deposits_recycler_view).show();
        aq.id(R.id.rtl_empty_container).hide();

        adapter = new DepositAdapter(this, depositList );
        depositsRecyclerView.setAdapter(adapter);

    }


    private void showEmptyLayout(String msg){
        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.deposits_recycler_view).hide();
        aq.id(R.id.rtl_empty_container).show();
        aq.id(R.id.icon_empty).image(R.drawable.ic_deposit);
        aq.id(R.id.txt_empty_message).text(msg);
    }

}
