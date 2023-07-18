package com.tv.uscreen.activities.watchList.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.R;
import com.tv.uscreen.beanModel.watchHistory.ItemsItem;
import com.tv.uscreen.databinding.CommonSearchAdapterBinding;
import com.tv.uscreen.utils.Logger;
import com.tv.uscreen.utils.constants.AppConstants;
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class WatchHistoryAdapter extends RecyclerView.Adapter<WatchHistoryAdapter.SingleItemRowHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static List<ItemsItem> list;
    private final Context context;
    private final KsPreferenceKeys preference;
    private final WatchHistoryAdaperListener listener;


    public WatchHistoryAdapter(Context context, List<ItemsItem> list, WatchHistoryAdaperListener listener) {
        this.context = context;
        WatchHistoryAdapter.list = list;
        this.listener = listener;

        preference = KsPreferenceKeys.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        boolean isLoadingAdded = false;
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
    @NonNull
    @Override
    public WatchHistoryAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CommonSearchAdapterBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.common_search_adapter, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewHolder.itemBinding.tvEpisode.setTextColor(ContextCompat.getColor(context, R.color.unselected_indicator_color));
        } else {
            viewHolder.itemBinding.tvEpisode.setTextColor(context.getResources().getColor(R.color.unselected_indicator_color));
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (list.get(position).getGenres() != null && list.get(position).getGenres().size() > 0) {
            for (int i = 0; i < list.get(position).getGenres().size(); i++) {
                if (i == list.get(position).getGenres().size() - 1) {
                    stringBuilder = stringBuilder.append(list.get(position).getGenres().get(i));

                } else
                    stringBuilder = stringBuilder.append(list.get(position).getGenres().get(i)).append(", ");
            }
        }
        viewHolder.itemBinding.tvEpisode.setText(stringBuilder.toString());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> onItemClicked(position));
    }


    private void onItemClicked(int position) {
        try {
            if (list.get(position).getStatus() != null && !list.get(position).getStatus().equalsIgnoreCase(AppConstants.UNPUBLISHED))
                listener.onWatchHistoryItemClicked(list.get(position));
        } catch (Exception e) {
            Logger.e("WatchListAdapter", "WatchListAdapter" + list.get(position));

        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface WatchHistoryAdaperListener {
        void onWatchHistoryItemClicked(ItemsItem itemValue);
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final CommonSearchAdapterBinding itemBinding;

        public SingleItemRowHolder(CommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

}


