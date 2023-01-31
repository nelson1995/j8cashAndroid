package com.mobile.j8cash.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquery.AQuery;
import com.mobile.j8cash.APIInterface;
import com.mobile.j8cash.R;
import com.mobile.j8cash.RetrofitClient;
import com.mobile.j8cash.adapters.ReceivedAdapter;
import com.mobile.j8cash.adapters.SentAdapter;
import com.mobile.j8cash.models.Transfer;
import com.mobile.j8cash.utils.Utils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReceivedFragment extends Fragment {

    private AQuery aq;
    private RecyclerView transfersRecyclerView;
    private ReceivedAdapter adapter;
    private List<Transfer> receivedList;
    public ReceivedFragment(List<Transfer> list) {
        // Required empty public constructor
        this.receivedList = list;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_received, container, false);
        aq = new AQuery(view);
        transfersRecyclerView = view.findViewById(R.id.received_list);
        transfersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadTransfers();

    }


    private void loadTransfers(){

        if(receivedList.size() == 0){
            showEmptyLayout("You have not received money yet!");
            return;
        }
        aq.id(R.id.rtl_empty_container).hide();

        adapter = new ReceivedAdapter(getActivity(), receivedList );
        transfersRecyclerView.setAdapter(adapter);

    }


    private void showEmptyLayout(String msg){
        aq.id(R.id.received_list).hide();
        aq.id(R.id.rtl_empty_container).show();
        aq.id(R.id.txt_empty_message).text(msg);
    }

}
