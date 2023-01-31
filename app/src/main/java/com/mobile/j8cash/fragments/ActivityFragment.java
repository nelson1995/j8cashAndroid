package com.mobile.j8cash.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquery.AQuery;
import com.mobile.j8cash.AirtimeListActivity;
import com.mobile.j8cash.DepositsListActivity;
import com.mobile.j8cash.R;
import com.mobile.j8cash.TransfersListActivity;
import com.mobile.j8cash.WithdrawActivity;
import com.mobile.j8cash.WithdrawsListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {
    AQuery aq;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_activity, container, false);
       aq = new AQuery(view);

       aq.id(R.id.btn_deposits_list).click(k->{
           startActivity(new Intent(getActivity(), DepositsListActivity.class));
       });

        aq.id(R.id.btn_transfers_list).click(k->{
            startActivity(new Intent(getActivity(), TransfersListActivity.class));
        });

        aq.id(R.id.btn_airtime_list).click(k->{
            startActivity(new Intent(getActivity(), AirtimeListActivity.class));
        });

        aq.id(R.id.btn_withdraw).click(k->{
            startActivity(new Intent(getActivity(), WithdrawsListActivity.class));
        });
       return view;
    }

}
