package com.mobile.j8cash.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mobile.j8cash.R;
import com.mobile.j8cash.models.Client;
import com.mobile.j8cash.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Client> clients;
    CustomFilter filter;
    private List<Client> clientsFiltered;

    public UsersAdapter(Context context, List<Client> users) {
        this.context = context;
        this.clientsFiltered = users;
        this.clients = users;
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public Object getItem(int i) {
        return clients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Client user = clients.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.client_name);
        TextView phone = (TextView) convertView.findViewById(R.id.client_phone);
        ImageView imageView = convertView.findViewById(R.id.client_image);
        Drawable drawable = TextDrawable.builder().buildRound(String.valueOf(user.name.charAt(0)), context.getResources().getColor(R.color.colorAccent));
        imageView.setImageDrawable(drawable);
        // Populate the data into the template view using the data object
        name.setText(user.name);
        phone.setText("+"+user.phone);
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {

        if(filter==null)
        {
            filter=new CustomFilter(clientsFiltered,this);
        }

        return filter;
    }

    public class CustomFilter extends Filter {

        List<Client> filterList;
        UsersAdapter adapter;

        public CustomFilter(List<Client> filterList, UsersAdapter adapter) {
            this.filterList = filterList;
            this.adapter = adapter;
        }

        //FILTERING
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //RESULTS
            FilterResults results=new FilterResults();

            //VALIDATION
            if(constraint != null && constraint.length()>0)
            {

                //CHANGE TO UPPER FOR CONSISTENCY
                constraint=constraint.toString().toUpperCase();

                ArrayList<Client> filteredMovies=new ArrayList<>();

                //LOOP THRU FILTER LIST
                for(int i=0;i<filterList.size();i++)
                {
                    //FILTER
                    if(filterList.get(i).name.toUpperCase().contains(constraint))
                    {
                        filteredMovies.add(filterList.get(i));
                    }

                }

                results.count=filteredMovies.size();
                results.values=filteredMovies;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;
            }

            return results;
        }

        //PUBLISH RESULTS

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            adapter.clients= (ArrayList<Client>) results.values;
            adapter.notifyDataSetChanged();

        }
    }
}
