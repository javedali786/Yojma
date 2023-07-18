package com.tv.uscreen.activities.profile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.R;
import com.tv.uscreen.activities.profile.model.AccountItem;

import java.util.ArrayList;

public class AdapterAccountItem extends RecyclerView.Adapter<AdapterAccountItem.ItemViewHolder> {
    ArrayList<AccountItem> items = new ArrayList<>();
    OnItemChangeClickListener mListener;
    public AdapterAccountItem(ArrayList<AccountItem> items, OnItemChangeClickListener  mListener) {
        this.items = items;
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public AdapterAccountItem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_item, null);
        return new AdapterAccountItem.ItemViewHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterAccountItem.ItemViewHolder holder, int position) {
        holder.imageIcon.setImageResource(items.get(position).getImage());
        holder.iconName.setText(items.get(position).getName());
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageIcon;
        TextView iconName;
        public ItemViewHolder(@NonNull View itemView)  {
            super(itemView);
            iconName = itemView.findViewById(R.id.more_list_title);
            imageIcon =itemView.findViewById(R.id.more_list_icon);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mListener!=null)mListener.onItemClick(view,getAdapterPosition());
        }
    }
    public interface OnItemChangeClickListener {
        void onItemClick(View view, int position);
    }

}
