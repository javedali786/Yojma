package com.tv.uscreen.yojmatv.adapters.commonRails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.listing.callback.ItemClickListener;
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity;
import com.tv.uscreen.yojmatv.beanModel.ContinueRailModel.CommonContinueRail;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
;
import com.tv.uscreen.yojmatv.databinding.PotraitTwoItemBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper;
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

public class CommonPotraitTwoAdapter extends RecyclerView.Adapter<CommonPotraitTwoAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final ItemClickListener listener;
    private long mLastClickTime = 0;
    private final ArrayList<CommonContinueRail> continuelist;
    private boolean isContinueList;
    private final String isLogin;
    private final KsPreferenceKeys preference;
    BaseCategory baseCategory;

    public CommonPotraitTwoAdapter(Activity context, List<EnveuVideoItemBean> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist, int view, ItemClickListener listener, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.listener = listener;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.baseCategory=baseCat;
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
        PotraitTwoItemBinding binding = PotraitTwoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.setColorsData(ColorsHelper.INSTANCE);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {
        if (itemsList.size() > 0) {

            try {
                AppCommonMethod.handleTags(itemsList.get(i).getIsVIP(),itemsList.get(i).getIsNewS(),
                        holder.potraitItemBinding.flExclusive,holder.potraitItemBinding.flNew,holder.potraitItemBinding.flEpisode,holder.potraitItemBinding.flNewMovie,itemsList.get(i).getAssetType());

            }catch (Exception e){
                Logger.w(e);
            }

            try {
                AppCommonMethod.handleTitleDesc(holder.potraitItemBinding.titleLayout,holder.potraitItemBinding.tvTitle,holder.potraitItemBinding.tvDescription,baseCategory);
                holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                holder.potraitItemBinding.tvDescription.setText(itemsList.get(i).getDescription());
            }catch (Exception e){
                Logger.w(e);
            }

            holder.potraitItemBinding.llContinueProgress.setVisibility(View.GONE);
            holder.potraitItemBinding.ivContinuePlay.setVisibility(View.GONE);
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.potraitItemBinding.setPlaylistItem(contentsItem);
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    holder.potraitItemBinding.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onRowItemClicked(itemsList.get(i), i);
                        }
                    });
                    if (itemsList.get(i).getPosterURL() != null && !itemsList.get(i).getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext()).loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getListPRImage(itemsList.get(i).getPosterURL(), holder.potraitItemBinding.itemImage.getContext()));
                    }

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {

                    holder.potraitItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        ActivityLauncher.getInstance().seriesDetailScreen((Activity) holder.potraitItemBinding.itemImage.getContext(), SeriesDetailActivity.class, itemsList.get(i).getId());
                    });
                    holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "POTRAIT") + itemsList.get(i).getPosterURL());


                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {
                    holder.potraitItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "POTRAIT") + itemsList.get(i).getPosterURL());

                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "POTRAIT") + itemsList.get(i).getPosterURL());
                }
            }
        } else if (continuelist.size() > 0) {
            if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                holder.potraitItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.potraitItemBinding.flNew.setVisibility(View.GONE);
                try {
                    ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext())
                            .loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "POTRAIT") + continuelist.get(i).getUserAssetDetail().getPortraitImage());
                } catch (Exception e) {
                    Logger.w(e);
                }
                holder.potraitItemBinding.itemImage.setOnClickListener(v -> {
                    if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                        if (continuelist.size() > 0 && (continuelist.get(i).getUserAssetDetail().getAssetType()) != null) {
                            AppCommonMethod.launchDetailScreen(holder.potraitItemBinding.itemImage.getContext(),0l,continuelist.get(i).getUserAssetDetail().getAssetType(), continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
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
        final PotraitTwoItemBinding potraitItemBinding;

        SingleItemRowHolder(PotraitTwoItemBinding potraitItemBind) {
            super(potraitItemBind.getRoot());
            potraitItemBinding = potraitItemBind;
        }
    }


}


