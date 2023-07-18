package com.tv.uscreen.adapters.commonRails;


import android.app.Activity;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tv.uscreen.R;
import com.tv.uscreen.activities.listing.callback.ItemClickListener;
import com.tv.uscreen.activities.series.ui.SeriesDetailActivity;
import com.tv.uscreen.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.databinding.SquareListingItemBinding;
import com.tv.uscreen.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.utils.constants.AppConstants;
import com.tv.uscreen.utils.helpers.ImageHelper;
import com.tv.uscreen.utils.helpers.intentlaunchers.ActivityLauncher;

import java.util.List;


public class SquareListingAdapter extends RecyclerView.Adapter<SquareListingAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    ItemClickListener listener;
    private long mLastClickTime = 0;

    public SquareListingAdapter(List<EnveuVideoItemBean> itemsList, String contentType, ItemClickListener callBack) {
        this.itemsList = itemsList;
        this.contentType = contentType;
        listener = callBack;
    }

    public void notifydata(List<EnveuVideoItemBean> i) {

        this.itemsList.addAll(i);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SquareListingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.square_listing_item, parent, false);
        binding.setColorsData(ColorsHelper.INSTANCE);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        if (itemsList.size() > 0) {
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.squareItemBinding.setPlaylistItem(contentsItem);

                try {
                    AppCommonMethod.handleTags(itemsList.get(i).getIsVIP(),itemsList.get(i).getIsNewS(),
                            holder.squareItemBinding.flExclusive,holder.squareItemBinding.flNew,holder.squareItemBinding.flEpisode,holder.squareItemBinding.flNewMovie,itemsList.get(i).getAssetType());

                }catch (Exception ignored){

                }
                holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                holder.squareItemBinding.tvDescription.setText(itemsList.get(i).getDescription());
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {
                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        listener.onRowItemClicked(itemsList.get(i), i);
                    });

                    if (itemsList.get(i).getPosterURL() != null && !itemsList.get(i).getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext()).loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getListSQRImage(itemsList.get(i).getPosterURL(), holder.squareItemBinding.itemImage.getContext()));
                    }
                    //Glide.with(mContext).load(itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);
                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    Glide.with(holder.squareItemBinding.itemImage.getContext()).load(AppCommonMethod.getImageUrl(AppConstants.SERIES, "SQUARE") + itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);
                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        ActivityLauncher.getInstance().seriesDetailScreen((Activity) holder.squareItemBinding.itemImage.getContext(), SeriesDetailActivity.class, itemsList.get(i).getId());

                    });

                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    Glide.with(holder.squareItemBinding.itemImage.getContext()).load(AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "SQUARE") + itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
                    Glide.with(holder.squareItemBinding.itemImage.getContext()).load(AppCommonMethod.getImageUrl(AppConstants.GENRE, "SQUARE") + itemsList.get(i).getPosterURL()).into(holder.squareItemBinding.itemImage);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final SquareListingItemBinding squareItemBinding;

        SingleItemRowHolder(SquareListingItemBinding squareItemBind) {
            super(squareItemBind.getRoot());
            squareItemBinding = squareItemBind;

        }

    }


}

