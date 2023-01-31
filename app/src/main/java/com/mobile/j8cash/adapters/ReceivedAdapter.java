package com.mobile.j8cash.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.j8cash.R;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.models.Transfer;
import com.mobile.j8cash.utils.Utils;

import java.util.List;

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.ViewHolder> {
    private List<Transfer> transfers;
    private Context context;
    private final LayoutInflater inflater;

    public ReceivedAdapter(Context context, List<Transfer> list) {
        this.transfers = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public ReceivedAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view;
        view = inflater.inflate(R.layout.sent_list_item, viewGroup, false);

        return new ReceivedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceivedAdapter.ViewHolder holder, int i) {
        Transfer transfer = transfers.get(i);
        holder.dateView.setText(transfer.sender_date);
        holder.amountView.setText(Utils.getPrice(Integer.parseInt(transfer.amount)));
        holder.personView.setText("From "+ transfer.sender.name);
    }

    @Override
    public int getItemCount() {
        return transfers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateView, amountView, personView;

        public ViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.date);
            amountView = view.findViewById(R.id.amount);
            personView = view.findViewById(R.id.person);
        }

    }
}
