package com.mobile.j8cash;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aquery.AQuery;
import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mobile.j8cash.adapters.ClientAdapter;
import com.mobile.j8cash.adapters.UsersAdapter;
import com.mobile.j8cash.models.Client;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendToUsersActivity extends BaseActivity implements OnSearchViewListener {
    private AQuery aq;
    private ClientAdapter adapter;
    private UsersAdapter usersAdapter;
    private ListView clients_recycler_view;
    private MaterialSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_users);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Select receiver");


        mSearchView = (MaterialSearchView) findViewById(R.id.sv);
        mSearchView.setOnSearchViewListener(this);
        aq = new AQuery(this);

        aq.id(R.id.btn_scan_qr).click(k->{
            new IntentIntegrator(this).setBeepEnabled(false).setPrompt("Scan QR").initiateScan();
//            IntentIntegrator integrator = new IntentIntegrator(this);
////            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//            integrator.setPrompt("Scan QR");
////            integrator.setCameraId(0);  // Use a specific camera of the device
//            integrator.setBeepEnabled(false);
////            integrator.setBarcodeImageEnabled(true);
//            integrator.initiateScan();
        });

        clients_recycler_view  = findViewById(R.id.clients_recycler_view);
//        clients_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        getUsers();


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                //aq.toast("Failed to scan");
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                findUserCode(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void findUserCode(String code){
        List<Client> clients = Client.find(Client.class, "name =? ", code);
        if(clients.size() > 0){
            Client client = clients.get(0);
            nextActivity(client);
        }else{
            aq.toast("User does not exist");
        }
    }


    public void getUsers(){
        aq.id(R.id.progress_layout).show();
        aq.id(R.id.clients_recycler_view).hide();

//        loadUsers();

        Call<List<Client>> call = RetrofitClient.getInstance().create(APIInterface.class).getUsers();
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if(response.isSuccessful()){
                    for (Client c: response.body()){
                        c.save();
                    }
                    loadUsers(response.body());
                }else{
                    aq.toast("An error occurred");
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                if (t instanceof IOException){
                    aq.toast("No internet connection");
                }else{
                    aq.toast("Failed to fetch users. Try again later");
                }
            }
        });
    }

    private void loadUsers(List<Client> clients){

//        List<Client> clients = Client.listAll(Client.class);
        if (clients.size() == 0){
            aq.toast("No users available!");
            return;
        }
        aq.id(R.id.progress_layout).hide();
        aq.id(R.id.clients_recycler_view).show();

        usersAdapter = new UsersAdapter(this, clients);
        clients_recycler_view.setOnItemClickListener((adapterView, view, i, l) -> {
            Client client = clients.get(i);
            nextActivity(client);
        });
        clients_recycler_view.setAdapter(usersAdapter);



//        adapter = new ClientAdapter(SendToUsersActivity.this, clients, client->{
//            Intent i = new Intent(SendToUsersActivity.this, SendActivity.class);
//            i.putExtra("ClientName", client.name);
//            i.putExtra("ClientId", client.getId().intValue());
//            startActivity(i);
//        });
//        clients_recycler_view.setAdapter(adapter);

    }

    private void nextActivity(Client client){
        Intent intent = new Intent(SendToUsersActivity.this, SendActivity.class);
        intent.putExtra("ClientName", client.name);
        intent.putExtra("ClientId", client.getId().intValue());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.receiver_list_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return true;
    }


    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        usersAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public void onQueryTextChange(String s) {
        usersAdapter.getFilter().filter(s);

    }
}
