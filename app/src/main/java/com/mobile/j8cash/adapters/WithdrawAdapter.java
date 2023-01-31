package com.mobile.j8cash.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.j8cash.R;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.models.Withdraw;
import com.mobile.j8cash.utils.Utils;

import java.util.List;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ViewHolder> {
    private List<Withdraw> deposits;
    private Context context;
    private final LayoutInflater inflater;

    public WithdrawAdapter(Context context, List<Withdraw> list) {
        this.deposits = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public WithdrawAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view;
        view = inflater.inflate(R.layout.withdraws_list_item, viewGroup, false);
        return new WithdrawAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WithdrawAdapter.ViewHolder holder, int i) {
        Withdraw deposit = deposits.get(i);
        holder.dateView.setText(deposit.date);
        holder.amountView.setText(Utils.getPrice(Integer.parseInt(deposit.amount)));
        holder.methodTypeView.setText(deposit.status);
        holder.message.setText(deposit.message);
    }

    @Override
    public int getItemCount() {
        return deposits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateView, amountView, methodTypeView, message;

        public ViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.d_date);
            amountView = view.findViewById(R.id.d_amount);
            methodTypeView = view.findViewById(R.id.d_payment_method);
            message = view.findViewById(R.id.message);


        }
    }
}
