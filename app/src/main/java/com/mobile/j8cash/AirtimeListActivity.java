package com.mobile.j8cash;

import android.os.Bundle;

import com.aquery.AQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.j8cash.adapters.AirtimeAdapter;
import com.mobile.j8cash.adapters.DepositAdapter;
import com.mobile.j8cash.models.Airtime;
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

public class AirtimeListActivity extends BaseActivity {
    private AQuery aq;
    private AirtimeAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Airtime Transactions");
        recyclerView = findViewById(R.id.airtime_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        aq = new AQuery(this);
        getAirtimeT();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getAirtimeT(){
        aq.id(R.id.progress_layout).show();
        aq.id(R.id.airtime_recycler_view).hide();
        aq.id(R.id.rtl_empty_container).hide();

        Call<List<Airtime>> call = RetrofitClient.getInstance().create(APIInterface.class).getAirtimeList();
        call.enqueue(new Callback<List<Airtime>>() {
            @Override
            public void onResponse(Call<List<Airtime>> call, Response<List<Airtime>> response) {
                if(response.isSuccessful()){
                    loadAirtime(response.body());
                }else{
                    aq.toast("An error occurred, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<Airtime>> call, Throwable t) {
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("System failure, try again later");
                }
            }
        });
    }

    private void loadAirtime(List<Airtime> airtimeList){

        if(airtimeList.size() == 0){
            showEmptyLayout("No airtime transactions");
            return;
        }

        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.airtime_recycler_view).show();
        aq.id(R.id.rtl_empty_container).hide();

        adapter = new AirtimeAdapter(this, airtimeList );
        recyclerView.setAdapter(adapter);

    }

    private void showEmptyLayout(String msg){
        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.airtime_recycler_view).hide();
        aq.id(R.id.rtl_empty_container).show();
        aq.id(R.id.icon_empty).image(R.drawable.ic_airtime);
        aq.id(R.id.txt_empty_message).text(msg);
    }

}
