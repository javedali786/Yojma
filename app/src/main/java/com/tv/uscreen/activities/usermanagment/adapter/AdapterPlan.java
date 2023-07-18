package com.tv.uscreen.activities.usermanagment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.R;
import com.tv.uscreen.activities.purchase.purchase_model.PurchaseModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterPlan extends RecyclerView.Adapter<AdapterPlan.ViewHolder>{
     List<PurchaseModel> items;
    OnPurchaseItemClick mListener;
    Context context;
    public AdapterPlan(List<PurchaseModel> items1, OnPurchaseItemClick mListener, Context context) {
        this.items = items1;
        Collections.sort(items, new Comparator<PurchaseModel>(){
            public int compare(PurchaseModel o1, PurchaseModel o2){
                return Integer.compare(o1.getSubscriptionOrder(), o2.getSubscriptionOrder());
            }
        });

        if (items != null && items.size() > 0) {
            items.get(0).setSelected(true);
        }
        this.mListener = mListener;
        this.context=context;
    }
    @NonNull
    @Override
    public AdapterPlan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_subscription, null);
        return new AdapterPlan.ViewHolder(inflate);
    }

    public interface OnPurchaseItemClick {
        void onPurchaseCardClick(boolean click, PurchaseModel planName);
        void setDescription(String description,PurchaseModel planName);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.planName.setText(items.get(position).getTitle());
        holder.amountName.setText(items.get(position).getPrice());
        Drawable drawable;
        if (items.get(position).isSelected()) {
            mListener.setDescription(items.get(position).getDescription(),items.get(position));
            drawable=selected(context,holder);
        } else {
            drawable=unselected(context,holder);
        }
        ViewCompat.setBackground(holder.mainLay, drawable);


        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetList(items);
                items.get(position).setSelected(true);
                mListener.onPurchaseCardClick(true, items.get(position));
            }
        });
    }

    private Drawable unselected(Context context, ViewHolder holder) {
        holder.planName.setTextColor(context.getResources().getColor(R.color.series_detail_now_playing_title_color));
        holder.amountName.setTextColor(context.getResources().getColor(R.color.series_detail_now_playing_title_color));
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.border_plan, null);
    }

    private Drawable selected(Context context, ViewHolder holder) {
        holder.planName.setTextColor(context.getResources().getColor(R.color.buy_now_pay_now_btn_text_color));
        holder.amountName.setTextColor(context.getResources().getColor(R.color.buy_now_pay_now_btn_text_color));
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.roundedcornerforbtn, null);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView planName,amountName;
        LinearLayout mainLay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planName = (TextView) itemView.findViewById(R.id.plan_name_text);
            amountName = (TextView) itemView.findViewById(R.id.plan_amount_text);
            mainLay=(LinearLayout)itemView.findViewById(R.id.main_lay);
        }
    }

    private void resetList(List<PurchaseModel> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(false);
        }
    }
}
