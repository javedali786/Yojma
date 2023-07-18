package com.tv.uscreen.activities.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import com.tv.uscreen.R;
import com.tv.uscreen.activities.purchase.purchase_model.PurchaseModel;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
    List<PurchaseModel> items;
    OnPurchaseItemClick mListener;
    Context context;
    public PurchaseAdapter(List<PurchaseModel> items, OnPurchaseItemClick mListener, Context context) {
        this.items = items;
        this.mListener = mListener;
        this.context=context;
    }

    public interface OnPurchaseItemClick {
        void onPurchaseCardClick(boolean click, PurchaseModel planName);
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_purchase, parent, false);
        return new PurchaseViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.description.setText(items.get(position).getDescription());
        holder.currency_price.setText(items.get(position).getPrice());

        Drawable drawable;
        if (items.get(position).isSelected()) {
            drawable=selected(context,holder);
        } else {
            drawable=unselected(context,holder);
        }
        ViewCompat.setBackground(holder.cardView, drawable);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetList(items);
                items.get(position).setSelected(true);
                mListener.onPurchaseCardClick(true, items.get(position));
            }
        });
    }

    private void resetList(List<PurchaseModel> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(false);
        }
    }
    private Drawable unselected(Context context, PurchaseAdapter.PurchaseViewHolder holder) {
        holder.title.setTextColor(context.getResources().getColor(R.color.series_detail_now_playing_title_color));
        holder.description.setTextColor(context.getResources().getColor(R.color.series_detail_now_playing_title_color));
        holder.currency_price.setTextColor(context.getResources().getColor(R.color.series_detail_now_playing_title_color));
        ViewCompat.setBackground(holder.currency_price, ResourcesCompat.getDrawable(context.getResources(), R.drawable.rounded_corner_textview_blue, null));
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.border_plan, null);
    }

    private Drawable selected(Context context, PurchaseAdapter.PurchaseViewHolder holder) {
        holder.title.setTextColor(context.getResources().getColor(R.color.buy_now_pay_now_btn_text_color));
        holder.description.setTextColor(context.getResources().getColor(R.color.buy_now_pay_now_btn_text_color));
        holder.currency_price.setTextColor(context.getResources().getColor(R.color.buy_now_pay_now_btn_text_color));
        ViewCompat.setBackground(holder.currency_price, ResourcesCompat.getDrawable(context.getResources(), R.drawable.rounded_corner_textview_grey, null));
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.roundedcornerforbtn, null);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView   description;
        public final TextView plan_type;
        public final TextView currency_price;
        public final RelativeLayout cardView;
        public ShimmerFrameLayout shimmerFrameLayout;
        public RelativeLayout relativeLayout;


        public PurchaseViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            currency_price = view.findViewById(R.id.currency_price);
            plan_type = view.findViewById(R.id.plan_type);
            cardView = view.findViewById(R.id.cv_tvod);
        }
    }

}

