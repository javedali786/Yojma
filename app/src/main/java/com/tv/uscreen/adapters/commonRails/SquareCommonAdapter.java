package com.tv.uscreen.adapters.commonRails;


import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.R;
import com.tv.uscreen.activities.detail.ui.DetailActivity;
import com.tv.uscreen.activities.detail.ui.EpisodeActivity;
import com.tv.uscreen.activities.series.ui.SeriesDetailActivity;
import com.tv.uscreen.beanModel.ContinueRailModel.CommonContinueRail;
import com.tv.uscreen.beanModel.responseModels.landingTabResponses.railData.ContentsItem;
import com.tv.uscreen.databinding.SquareItemBinding;
import com.tv.uscreen.utils.Logger;
import com.tv.uscreen.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.utils.constants.AppConstants;
import com.tv.uscreen.utils.helpers.ImageHelper;
import com.tv.uscreen.utils.helpers.intentlaunchers.ActivityLauncher;
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;


public class SquareCommonAdapter extends RecyclerView.Adapter<SquareCommonAdapter.SingleItemRowHolder> {

    private final List<ContentsItem> itemsList;
    private final String contentType;
    private long mLastClickTime = 0;
    private final ArrayList<CommonContinueRail> continuelist;
    private final KsPreferenceKeys preference;
    private final String isLogin;
    private boolean isContinueList;
    private final int itemWidth;
    private final int itemHeight;

    public SquareCommonAdapter(Activity context, List<ContentsItem> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist) {
        this.itemsList = itemsList;
        this.contentType = contentType;
        this.continuelist = continuelist;
        if (this.continuelist != null) {
            isContinueList = this.continuelist.size() > 0;
        }
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 3;
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            //landscape
            if (context.getResources().getConfiguration().orientation == 2)
                num = 6;
            else
                num = 5;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 20) / num;
        itemHeight = itemWidth;

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SquareItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.square_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        holder.squareItemBinding.itemImage.getLayoutParams().width = itemWidth;
        holder.squareItemBinding.itemImage.getLayoutParams().height = itemHeight;

        if (itemsList.size() > 0) {
            ContentsItem contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.squareItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.squareItemBinding.ivContinuePlay.setVisibility(View.GONE);

                if (contentsItem.isPremium()) {
                    holder.squareItemBinding.flExclusive.setVisibility(View.VISIBLE);
                } else {
                    holder.squareItemBinding.flExclusive.setVisibility(View.GONE);
                }
                if (contentsItem.isNew()) {
                    holder.squareItemBinding.flNew.setVisibility(View.VISIBLE);
                } else {
                    holder.squareItemBinding.flNew.setVisibility(View.GONE);
                }


                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {
                    holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getTitle());

                    ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext())
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());

                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());
                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        try {
                            if ((itemsList.get(i).getAssetType()) != null) {
                                if (itemsList.get(i).getAssetType().equalsIgnoreCase("EPISODE")) {
                                    ActivityLauncher.getInstance().episodeScreen((Activity) holder.squareItemBinding.itemImage.getContext(), EpisodeActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                                } else {
                                    ActivityLauncher.getInstance().detailScreen((Activity) holder.squareItemBinding.itemImage.getContext(), DetailActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                                }
                            }
                        } catch (Exception e) {
                            ActivityLauncher.getInstance().detailScreen((Activity) holder.squareItemBinding.itemImage.getContext(), DetailActivity.class, itemsList.get(i).getId(), "0", contentsItem.isPremium());
                        }

                    });
                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {

                    holder.squareItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        ActivityLauncher.getInstance().seriesDetailScreen((Activity) holder.squareItemBinding.itemImage.getContext(), SeriesDetailActivity.class, itemsList.get(i).getId());

                    });
                    holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getName());

                    ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext())
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "SQUARE") + itemsList.get(i).getPicture());
                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());


                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    holder.squareItemBinding.tvTitle.setText(itemsList.get(i).getName());
                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());

                    ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext())
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "SQUARE") + itemsList.get(i).getPicture());

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {

                    ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext())
                            .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "SQUARE") + itemsList.get(i).getPicture());
                    Logger.e("imageUrl", "imageUrl" + AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + itemsList.get(i).getPortraitImage());


                }
            }
        } else if (continuelist.size() > 0) {
            if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                holder.squareItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.squareItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.squareItemBinding.flNew.setVisibility(View.GONE);

                if (continuelist.get(i).getUserAssetDetail().isPremium()) {
                    holder.squareItemBinding.flExclusive.setVisibility(View.VISIBLE);
                } else {
                    holder.squareItemBinding.flExclusive.setVisibility(View.GONE);
                }
                ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext())
                        .loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "SQUARE") + continuelist.get(i).getUserAssetDetail().getPortraitImage());
                holder.squareItemBinding.itemImage.setOnClickListener(v -> {
                    if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                        if (continuelist.size() > 0 && (continuelist.get(i).getUserAssetDetail().getAssetType()) != null) {
                            AppCommonMethod.launchDetailScreen(holder.squareItemBinding.itemImage.getContext(),0l,continuelist.get(i).getUserAssetDetail().getAssetType(), continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
                        }
                    }
                });

            } else {
                holder.squareItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.squareItemBinding.ivContinuePlay.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public int getItemCount() {
        if (isContinueList)
            return (null != continuelist ? continuelist.size() : 0);
        else
            return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final SquareItemBinding squareItemBinding;

        SingleItemRowHolder(SquareItemBinding squareItemBind) {
            super(squareItemBind.getRoot());
            squareItemBinding = squareItemBind;

        }

    }

}

