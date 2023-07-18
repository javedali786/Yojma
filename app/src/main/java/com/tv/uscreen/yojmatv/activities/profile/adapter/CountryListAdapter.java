package com.tv.uscreen.yojmatv.activities.profile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.profile.model.CountryListModel;

import java.util.ArrayList;
import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.vItemHolder> implements Filterable {
    private ArrayList<CountryListModel> countryList = new ArrayList<>();
    private ArrayList<CountryListModel> exampleListFull = new ArrayList<>();
    public ItemSelected itemSelected;

    public CountryListAdapter(ArrayList<CountryListModel> countryList, ItemSelected itemSelected) {
        this.countryList = countryList;
        this.itemSelected = itemSelected;
        exampleListFull = new ArrayList<>(countryList);
    }

    @NonNull
    @Override
    public CountryListAdapter.vItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_list, null);
        return new CountryListAdapter.vItemHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryListAdapter.vItemHolder holder, int position) {
        holder.tvCountry.setText(countryList.get(position).getName());

        holder.ll_Country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelected.getSelectedCountry(countryList.get(position).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return countryList.size();

    }

    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CountryListModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CountryListModel item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            countryList.clear();
           countryList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface ItemSelected {
        void getSelectedCountry(String countryName);
    }

    public static class vItemHolder extends RecyclerView.ViewHolder {
        TextView tvCountry;
        LinearLayout ll_Country;

        public vItemHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            ll_Country = itemView.findViewById(R.id.ll_Country);


        }
    }
}
