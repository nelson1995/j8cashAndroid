package com.mobile.j8cash.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mobile.j8cash.R;
import com.mobile.j8cash.models.Airtime;
import com.mobile.j8cash.models.Client;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.utils.Utils;

import java.util.List;

public class AirtimeAdapter  extends RecyclerView.Adapter<AirtimeAdapter.ViewHolder> {
    private List<Airtime> airtimes;
    private Context context;
    private final LayoutInflater inflater;

    public AirtimeAdapter(Context context, List<Airtime> list) {
        this.airtimes = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public AirtimeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view;
        view = inflater.inflate(R.layout.airtime_list_item, viewGroup, false);

        return new AirtimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AirtimeAdapter.ViewHolder holder, int i) {
        Airtime airtime = airtimes.get(i);
        holder.dateView.setText(airtime.date);
        holder.amountView.setText(Utils.getPrice(Integer.parseInt(airtime.amount)));
        holder.statusView.setText(airtime.status);
        holder.phoneView.setText("To "+ airtime.phone);
    }

    @Override
    public int getItemCount() {
        return airtimes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateView, amountView, statusView, phoneView;

        public ViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.date);
            amountView = view.findViewById(R.id.amount);
            phoneView = view.findViewById(R.id.phone);
            statusView = view.findViewById(R.id.status);

        }
    }
}
