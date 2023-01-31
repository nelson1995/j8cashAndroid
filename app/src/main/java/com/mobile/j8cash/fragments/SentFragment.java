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
import com.mobile.j8cash.adapters.DepositAdapter;
import com.mobile.j8cash.adapters.SentAdapter;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.models.Transfer;
import com.mobile.j8cash.utils.Utils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SentFragment extends Fragment {
    private AQuery aq;
    private RecyclerView transfersRecyclerView;
    private SentAdapter adapter;
    private List<Transfer> sentList;
    public SentFragment(List<Transfer> list) {
        // Required empty public constructor
        this.sentList = list;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sent, container, false);
        aq = new AQuery(view);
        transfersRecyclerView = view.findViewById(R.id.sent_list);
        transfersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadTransfers();
    }


    private void loadTransfers(){

        if(sentList.size() == 0){
            showEmptyLayout("You have not sent any money yet!");
            return;
        }

        aq.id(R.id.rtl_empty_container).hide();


        adapter = new SentAdapter(getActivity(), sentList );
        transfersRecyclerView.setAdapter(adapter);

    }

    private void showEmptyLayout(String msg){
        aq.id(R.id.sent_list).hide();
        aq.id(R.id.rtl_empty_container).show();
        aq.id(R.id.txt_empty_message).text(msg);
    }



}
