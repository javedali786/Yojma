package com.tv.uscreen.adapters.commonRails;


import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;

import com.tv.uscreen.R;
import com.tv.uscreen.activities.detail.ui.DetailActivity;
import com.tv.uscreen.activities.detail.ui.EpisodeActivity;
import com.tv.uscreen.activities.listing.callback.ItemClickListener;
import com.tv.uscreen.activities.series.ui.SeriesDetailActivity;
import com.tv.uscreen.beanModel.responseModels.series.season.ItemsItem;
import com.tv.uscreen.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.databinding.LandscapeListingItemBinding;
import com.tv.uscreen.utils.Logger;
import com.tv.uscreen.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.utils.constants.AppConstants;
import com.tv.uscreen.utils.helpers.ImageHelper;
import com.tv.uscreen.utils.helpers.StringUtils;
import com.tv.uscreen.utils.helpers.intentlaunchers.ActivityLauncher;

import java.util.List;

public class LandscapeListingAdapter extends RecyclerView.Adapter<LandscapeListingAdapter.SingleItemRowHolder> {

    final List<ItemsItem> itemsItems;
    final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    boolean isTablet;
    private long mLastClickTime = 0;
    private final int itemWidth;
    private final int itemHeight;
    private final ItemClickListener listener;
    BaseCategory baseCategory;
    public LandscapeListingAdapter(Activity context, List<EnveuVideoItemBean> itemsList, List<ItemsItem> itemsItems, String contentType, ItemClickListener callBack, BaseCategory baseCat, boolean tabletSize) {
        this.itemsList = itemsList;
        this.itemsItems = itemsItems;
        this.contentType = contentType;
        this.listener = callBack;
        this.baseCategory=baseCat;
        this.isTablet = tabletSize;
        int num = 2;
        if (isTablet) {
            //landscape
            if (context.getResources().getConfiguration().orientation == 2)
                num = 4;
            else
                num = 3;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 80) / num;
        itemHeight = (itemWidth * 9) / 16;
    }

    public void notifydata(List<EnveuVideoItemBean> i) {

        this.itemsList.addAll(i);
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LandscapeListingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.landscape_listing_item, parent, false);
        //ThemeHandler.getInstance().applyheadingLandscapeBig(parent.getContext(),binding);
        binding.setColorsData(ColorsHelper.INSTANCE);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        Logger.d( "onBindViewHolder" + "onBindViewHolder");
        if (isTablet) {
            holder.landscapeItemBinding.itemImage.getLayoutParams().width = itemWidth;
            holder.landscapeItemBinding.itemImage.getLayoutParams().height = itemHeight;

        }
        if (itemsList.size() > 0) {
            Logger.d( "onBindViewHolder" + itemsList.get(i).getPosterURL());

            try {
                AppCommonMethod.handleTags(itemsList.get(i).getIsVIP(),itemsList.get(i).getIsNewS(),
                        holder.landscapeItemBinding.flExclusive,holder.landscapeItemBinding.flNew,holder.landscapeItemBinding.flEpisode,holder.landscapeItemBinding.flNewMovie,itemsList.get(i).getAssetType());

            }catch (Exception ignored){

            }
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.landscapeItemBinding.setPlaylistItem(contentsItem);
                try {
                    AppCommonMethod.handleTitleDesc(holder.landscapeItemBinding.titleLayout,holder.landscapeItemBinding.tvTitle,holder.landscapeItemBinding.tvDescription,baseCategory);
                    holder.landscapeItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                    holder.landscapeItemBinding.tvDescription.setText(itemsList.get(i).getDescription());
                }catch (Exception ignored){

                }
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {
                    if (itemsList.get(i).getPosterURL() != null && !itemsList.get(i).getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadListImage(holder.landscapeItemBinding.itemImage, AppCommonMethod.getListLDSImage(itemsList.get(i).getPosterURL(), holder.landscapeItemBinding.itemImage.getContext()));
                    }
                    holder.landscapeItemBinding.itemImage.setOnClickListener(view -> {
                        listener.onRowItemClicked(itemsList.get(i), i);
                    });

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    Glide.with(holder.landscapeItemBinding.itemImage.getContext()).load(AppCommonMethod.getImageUrl(AppConstants.SERIES, "LANDSCAPE") + itemsList.get(i).getPosterURL()).into(holder.landscapeItemBinding.itemImage);
                    holder.landscapeItemBinding.itemImage.setOnClickListener(view -> {

                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        ActivityLauncher.getInstance().seriesDetailScreen((Activity) holder.landscapeItemBinding.itemImage.getContext(), SeriesDetailActivity.class, itemsList.get(i).getId());

                    });

                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    Glide.with(holder.landscapeItemBinding.itemImage.getContext()).load(AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "LANDSCAPE") + itemsList.get(i).getPosterURL()).into(holder.landscapeItemBinding.itemImage);

                }
            } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
                Glide.with(holder.landscapeItemBinding.itemImage.getContext()).load(AppCommonMethod.getImageUrl(AppConstants.GENRE, "LANDSCAPE") + itemsList.get(i).getPosterURL()).into(holder.landscapeItemBinding.itemImage);
            }
        } else {
            ItemsItem sItem = itemsItems.get(i);
            Glide.with(holder.landscapeItemBinding.itemImage.getContext()).load(AppCommonMethod.urlPoints + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(800)" + AppConstants.VIDEO_IMAGE_BASE_KEY + itemsItems.get(i).getLandscapeImage()).into(holder.landscapeItemBinding.itemImage);
            holder.landscapeItemBinding.itemImage.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (!StringUtils.isNullOrEmptyOrZero(sItem.getVideoType())) {
                    if (sItem.getVideoType().equalsIgnoreCase("EPISODE")) {
                        ActivityLauncher.getInstance().episodeScreen((Activity) holder.landscapeItemBinding.itemImage.getContext(), EpisodeActivity.class, sItem.getId(), "", sItem.isPremium());
                    } else {
                        ActivityLauncher.getInstance().detailScreen((Activity) holder.landscapeItemBinding.itemImage.getContext(), DetailActivity.class, sItem.getId(), "", sItem.isPremium());
                    }
                }


            });
        }
    }
    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (itemsList.size() > 0) {
            itemCount = itemsList.size();
        } else if (itemsItems.size() > 0) {
            itemCount = itemsItems.size();
        }
        return itemCount;
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        public final LandscapeListingItemBinding landscapeItemBinding;

        public SingleItemRowHolder(LandscapeListingItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeItemBinding = flightItemLayoutBinding;
        }

    }
}

