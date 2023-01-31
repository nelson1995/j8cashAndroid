package com.mobile.j8cash.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mobile.j8cash.R;
import com.mobile.j8cash.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter   extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {
    private List<Client> clients;
    private Context context;
    private int count = 0;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Client item);
    }
    public ClientAdapter(Context context, List<Client> list, OnItemClickListener listener) {
        this.clients = list;
        this.context = context;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public ClientAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view;
            view = inflater.inflate(R.layout.user_list_item, viewGroup, false);

        return new ClientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientAdapter.ViewHolder holder, int i) {
        Client client = clients.get(i);
        holder.bind(client, listener);
        holder.nameView.setText(client.name);
        holder.phoneView.setText("+"+client.phone);
        Drawable drawable = TextDrawable.builder().buildRound(String.valueOf(client.name.charAt(0)), context.getResources().getColor(R.color.colorAccent));
        holder.imageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameView, phoneView;

        public ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.client_name);
            phoneView = view.findViewById(R.id.client_phone);
            imageView = view.findViewById(R.id.client_image);

        }

        public void bind(Client client, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(client));

        }
    }


}
