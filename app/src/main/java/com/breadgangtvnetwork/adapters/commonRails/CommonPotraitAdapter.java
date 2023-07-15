package com.breadgangtvnetwork.adapters.commonRails;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.breadgangtvnetwork.R;
import com.breadgangtvnetwork.activities.listing.callback.ItemClickListener;
import com.breadgangtvnetwork.activities.series.ui.SeriesDetailActivity;
import com.breadgangtvnetwork.beanModel.ContinueRailModel.CommonContinueRail;
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData;
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.breadgangtvnetwork.databinding.PotraitItemBinding;
import com.breadgangtvnetwork.utils.Logger;
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod;
import com.breadgangtvnetwork.utils.constants.AppConstants;
import com.breadgangtvnetwork.utils.helpers.ImageHelper;
import com.breadgangtvnetwork.utils.helpers.intentlaunchers.ActivityLauncher;
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;

import java.util.ArrayList;
import java.util.List;


public class CommonPotraitAdapter extends RecyclerView.Adapter<CommonPotraitAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final ItemClickListener listener;
    private long mLastClickTime = 0;
    private final ArrayList<CommonContinueRail> continuelist;
    private boolean isContinueList;
    private final String isLogin;
    private final KsPreferenceKeys preference;
    BaseCategory baseCategory;
    private RailCommonData railCommonData;

    public CommonPotraitAdapter(Activity context, List<EnveuVideoItemBean> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist, int view, ItemClickListener listener, BaseCategory baseCat, RailCommonData railCommonData) {
        this.itemsList = itemsList;
        this.listener = listener;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.baseCategory=baseCat;
        this.railCommonData = railCommonData;
        if (this.continuelist != null) {
            isContinueList = this.continuelist.size() > 0;
        }

        int num = 3;
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            //landscape
            if (context.getResources().getConfiguration().orientation == 2)
                num = 6;
            else
                num = 5;
        }
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    }

    public void notifydata(List<EnveuVideoItemBean> i) {
        this.itemsList.addAll(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PotraitItemBinding binding = PotraitItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, @SuppressLint("RecyclerView") int position) {
        if (itemsList.size() > 0) {

            try {
                AppCommonMethod.handleTags(itemsList.get(position).getIsVIP(),itemsList.get(position).getIsNewS(),
                        holder.potraitItemBinding.flExclusive,holder.potraitItemBinding.flNew,holder.potraitItemBinding.flEpisode,holder.potraitItemBinding.flNewMovie,itemsList.get(position).getAssetType());

            }catch (Exception e){
                Logger.w(e);
            }

            try {
                AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                holder.potraitItemBinding.tvTitle.setText(itemsList.get(position).getTitle());
                holder.potraitItemBinding.tvDescription.setText(itemsList.get(position).getDescription());
            }catch (Exception ignored){

            }

            holder.potraitItemBinding.llContinueProgress.setVisibility(View.GONE);
            holder.potraitItemBinding.ivContinuePlay.setVisibility(View.GONE);
            EnveuVideoItemBean contentsItem = itemsList.get(position);
            if (contentsItem != null) {
                holder.potraitItemBinding.setPlaylistItem(contentsItem);
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    holder.potraitItemBinding.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onRowItemClicked(contentsItem, position);
                        }
                    });
                    if (contentsItem.getPosterURL() != null && !contentsItem.getPosterURL().equalsIgnoreCase("")) {

                        ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext()).loadImageToListPortrait(holder.potraitItemBinding.itemImage, AppCommonMethod.getListPRImage(contentsItem.getPosterURL(), holder.potraitItemBinding.itemImage.getContext()));

                    }

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {

                    holder.potraitItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        ActivityLauncher.getInstance().seriesDetailScreen((Activity) holder.potraitItemBinding.itemImage.getContext(), SeriesDetailActivity.class, contentsItem.getId());
                    });
                    //holder.potraitItemBinding.tvTitle.setText(contentsItem.getTitle());
                    try {
                        AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                        holder.potraitItemBinding.tvTitle.setText(itemsList.get(position).getTitle());
                        holder.potraitItemBinding.tvDescription.setText(itemsList.get(position).getDescription());
                    }catch (Exception ignored){

                    }
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageToListPortrait(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "POTRAIT") + contentsItem.getPosterURL());


                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    try {
                        AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                        holder.potraitItemBinding.tvTitle.setText(itemsList.get(position).getTitle());
                        holder.potraitItemBinding.tvDescription.setText(itemsList.get(position).getDescription());
                    }catch (Exception ignored){

                    }
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageToListPortrait(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "POTRAIT") + contentsItem.getPosterURL());

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageToListPortrait(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "POTRAIT") + contentsItem.getPosterURL());
                }
            }
        } else if (continuelist.size() > 0) {
            if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                holder.potraitItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.flNew.setVisibility(View.GONE);
                try {
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageToListPortrait(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "POTRAIT") + continuelist.get(position).getUserAssetDetail().getPortraitImage());
                } catch (Exception e) {

                }
                holder.potraitItemBinding.itemImage.setOnClickListener(v -> {
                    if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                        if (continuelist.size() > 0 && (continuelist.get(position).getUserAssetDetail().getAssetType()) != null) {
                            AppCommonMethod.redirectionLogic(holder.potraitItemBinding.itemImage.getContext(),railCommonData,position);
                          //  AppCommonMethod.launchDetailScreen(holder.potraitItemBinding.itemImage.getContext(), 0l, continuelist.get(position).getUserAssetDetail().getAssetType(), continuelist.get(position).getUserAssetDetail().getId(), String.valueOf(continuelist.get(position).getUserAssetStatus().getPosition()), continuelist.get(position).getUserAssetDetail().isPremium());
                            AppCommonMethod.trackFcmEvent("Content Screen","", holder.potraitItemBinding.itemImage.getContext(),0);

                        }
                    }
                });

            } else {
                holder.potraitItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.potraitItemBinding.ivContinuePlay.setVisibility(View.GONE);
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
        final PotraitItemBinding potraitItemBinding;

        SingleItemRowHolder(PotraitItemBinding potraitItemBind) {
            super(potraitItemBind.getRoot());
            potraitItemBinding = potraitItemBind;
        }
    }


}

