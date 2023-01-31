package com.mobile.j8cash.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aquery.AQuery;
import com.mobile.j8cash.AccountManager;
import com.mobile.j8cash.ContactUsActivity;
import com.mobile.j8cash.DepositActivity;
import com.mobile.j8cash.ProfileActivity;
import com.mobile.j8cash.R;
import com.mobile.j8cash.ShareActivity;
import com.mobile.j8cash.UpdateProfileActivity;
import com.mobile.j8cash.ViewQRActivity;
import com.mobile.j8cash.WithdrawActivity;
import com.mobile.j8cash.models.User;
import com.mobile.j8cash.utils.Utils;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private AQuery aq;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        aq = new AQuery(view);

        aq.id(R.id.link_withdraw).click(k->{
            startActivity(new Intent(getActivity(), WithdrawActivity.class));
        });
        aq.id(R.id.link_deposit).click(k->{
            startActivity(new Intent(getActivity(), DepositActivity.class));
        });

        aq.id(R.id.link_share).click(k->{
            startActivity(new Intent(getActivity(), ShareActivity.class));
        });

        aq.id(R.id.link_qr_code).click(k->{
            startActivity(new Intent(getActivity(), ViewQRActivity.class));
        });

        aq.id(R.id.link_profile).click(k->{
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });

        aq.id(R.id.link_contact_us).click(k->{
            startActivity(new Intent(getActivity(), ContactUsActivity.class));
        });


        aq.id(R.id.btn_logout).click(l->{
            AccountManager.logout(getContext());
        });

        aq.id(R.id.user_wallet).text(Utils.getPrice((int) Prefs.getFloat(AccountManager.WALLET, 0)));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        aq.id(R.id.fullnames).text(Prefs.getString(AccountManager.NAME, ""));

        String profile_pic = Prefs.getString(AccountManager.PROFILE_URL, "");
        if(profile_pic.equals("") || profile_pic == null){
            return;
        }
        aq.id(R.id.profile_image).image(profile_pic);
    }
}
