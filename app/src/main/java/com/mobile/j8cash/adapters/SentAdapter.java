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

public class SentAdapter extends RecyclerView.Adapter<SentAdapter.ViewHolder> {
    private List<Transfer> transfers;
    private Context context;
    private final LayoutInflater inflater;
    private SentAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Deposit item);
    }
    public SentAdapter(Context context, List<Transfer> list) {
        this.transfers = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public SentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view;
        view = inflater.inflate(R.layout.sent_list_item, viewGroup, false);

        return new SentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SentAdapter.ViewHolder holder, int i) {
        Transfer transfer = transfers.get(i);
        holder.dateView.setText(transfer.sender_date);
        holder.amountView.setText(Utils.getPrice(Integer.parseInt(transfer.amount)));
        if (transfer !=null) {
            holder.personView.setText("To " + transfer.receiver.name);
        }
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
