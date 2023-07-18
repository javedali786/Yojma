package com.tv.activities.profile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tv.R;
import com.tv.activities.profile.model.OrderHistoryModal;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.vItemHolder> {
    ArrayList<OrderHistoryModal> oderItemArray = new ArrayList<>();
    public OrderHistoryAdapter(ArrayList<OrderHistoryModal> oderItemArray) {
        this.oderItemArray =oderItemArray;

    }

    @NonNull
    @Override
    public OrderHistoryAdapter.vItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, null);
        return new OrderHistoryAdapter.vItemHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.vItemHolder holder, int position) {
      holder.item.setText(oderItemArray.get(position).getAnnually());
      holder.expire.setText(oderItemArray.get(position).getActive());
      holder.validaity.setText(oderItemArray.get(position).getValidity());
      holder.validaityDate.setText(oderItemArray.get(position).getValidityDate());
      holder.paymentMode.setText(oderItemArray.get(position).getPaymentMode());
      holder.apple.setText(oderItemArray.get(position).getApple());
      holder.orderID.setText(oderItemArray.get(position).getOrderId());
      holder.euroId.setText(oderItemArray.get(position).getEuroPay());
      holder.data.setText(oderItemArray.get(position).getDate());
      holder.expireDate.setText(oderItemArray.get(position).getExpireDate());
      holder.paymentStatus.setText(oderItemArray.get(position).getPayStatus());
      holder.payStatusComplecte.setText(oderItemArray.get(position).getCompleteStatus());
    }

    @Override
    public int getItemCount() {
        return oderItemArray.size();
    }
    public static class vItemHolder extends RecyclerView.ViewHolder {
        TextView item,expire,validaity,validaityDate,paymentMode,apple,orderID,orderXEMHN,euroId,data,expireDate,paymentStatus,payStatusComplecte;
        TextView transactionType,PurchaseId;
        public vItemHolder(@NonNull View itemView) {
            super(itemView);
          //  item = itemView.findViewById(R.id.order_annually);
            expire = itemView.findViewById(R.id.activeExpired);
            validaity = itemView.findViewById(R.id.order_validity_id);
           // validaityDate = itemView.findViewById(R.id.order_validity_date);
            paymentMode = itemView.findViewById(R.id.order_payment_mode_id);
           // apple = itemView.findViewById(R.id.order_apple_id);
            orderID = itemView.findViewById(R.id.order_id);
           // orderXEMHN = itemView.findViewById(R.id.order_xEMhn);
           // euroId = itemView.findViewById(R.id.order_Euro_id);
            data = itemView.findViewById(R.id.order_date_id);
            expireDate = itemView.findViewById(R.id.order_dateEnd_id);
            paymentStatus = itemView.findViewById(R.id.order_payment_status_id);
           // payStatusComplecte = itemView.findViewById(R.id.order_payment_status_complecte);
            transactionType = itemView.findViewById(R.id.order_transaction_type_id);
            PurchaseId = itemView.findViewById(R.id.order_Purchase_id);
        }
    }
}
