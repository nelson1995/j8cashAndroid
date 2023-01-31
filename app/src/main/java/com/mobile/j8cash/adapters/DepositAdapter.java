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
import com.mobile.j8cash.models.Client;
import com.mobile.j8cash.models.Deposit;
import com.mobile.j8cash.utils.Utils;

import java.util.List;

public class DepositAdapter  extends RecyclerView.Adapter<DepositAdapter.ViewHolder> {
    private List<Deposit> deposits;
    private Context context;
    private final LayoutInflater inflater;

    public DepositAdapter(Context context, List<Deposit> list) {
        this.deposits = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DepositAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view;
        view = inflater.inflate(R.layout.deposits_list_item, viewGroup, false);

        return new DepositAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepositAdapter.ViewHolder holder, int i) {
        Deposit deposit = deposits.get(i);
        holder.dateView.setText(deposit.date);
        holder.amountView.setText(Utils.getPrice(deposit.amount));
        holder.methodTypeView.setText(deposit.payment_method);
    }

    @Override
    public int getItemCount() {
        return deposits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateView, amountView, methodTypeView;

        public ViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.d_date);
            amountView = view.findViewById(R.id.d_amount);
            methodTypeView = view.findViewById(R.id.d_payment_method);

        }
    }
}
