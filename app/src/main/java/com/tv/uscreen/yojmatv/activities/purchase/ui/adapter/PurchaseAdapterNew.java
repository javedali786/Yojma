package com.tv.uscreen.yojmatv.activities.purchase.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseModel;
import com.tv.uscreen.yojmatv.activities.usermanagment.model.PlanSubscriptionModel;

import java.util.List;

public class PurchaseAdapterNew extends RecyclerView.Adapter<PurchaseAdapterNew.PurchaseViewHolder> {

    List<PlanSubscriptionModel> items;
    PurchaseAdapterNew.OnPurchaseItemClick mListener;

    public PurchaseAdapterNew(List<PlanSubscriptionModel> items, PurchaseAdapterNew.OnPurchaseItemClick mListener) {
        this.items = items;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_subscription, parent, false);

        return new PurchaseViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        try {
            holder.planName.setText(items.get(position).getPlanName());
            holder.amountName.setText(items.get(position).getPlan());

        } catch (Exception e) {
            Log.w("PurchaseAdapterNew", e.toString());
        }
    }

    private void resetList(List<PurchaseModel> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface OnPurchaseItemClick {
        void onPurchaseCardClick(boolean click, PurchaseModel planName);
    }

    public static class PurchaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PurchaseAdapterNew.OnPurchaseItemClick mListener;
        public TextView planName,amountName;

        public void setOnItemClickListener(PurchaseAdapterNew.OnPurchaseItemClick mListener) {
            this.mListener = mListener;
        }

        public PurchaseViewHolder(View view) {
            super(view);
            planName = (TextView) itemView.findViewById(R.id.plan_name_text);
            amountName = (TextView) itemView.findViewById(R.id.plan_amount_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           // if (mListener!=null)mListener.onItemClick(view,getBindingAdapterPosition());
        }
    }
}
