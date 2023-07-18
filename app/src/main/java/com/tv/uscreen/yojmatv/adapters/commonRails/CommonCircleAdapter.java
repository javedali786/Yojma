package com.tv.uscreen.yojmatv.adapters.commonRails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.listing.callback.ItemClickListener;
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity;
import com.tv.uscreen.yojmatv.beanModel.ContinueRailModel.CommonContinueRail;
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
;
import com.tv.uscreen.yojmatv.databinding.GridCircleItemBinding;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper;
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

public class CommonCircleAdapter extends RecyclerView.Adapter<CommonCircleAdapter.SingleItemRowHolder> {

    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private final ItemClickListener listener;
    private long mLastClickTime = 0;
    private final ArrayList<CommonContinueRail> continuelist;
    private boolean isContinueList;
    private final String isLogin;
    private final KsPreferenceKeys preference;
    private RailCommonData railCommonData;

    public CommonCircleAdapter(Activity context, List<EnveuVideoItemBean> itemsList, String contentType, ArrayList<CommonContinueRail> continuelist, ItemClickListener callback, RailCommonData playlistRailData) {
        this.itemsList = itemsList;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.railCommonData = playlistRailData;
        listener = callback;
        if (this.continuelist != null) {
            isContinueList = this.continuelist.size() > 0;
        }
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 4;
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            if (context.getResources().getConfiguration().orientation == 2)
                num = 6;
            else
                num = 5;
        }
    }

    public void notifydata(List<EnveuVideoItemBean> i) {

        this.itemsList.addAll(i);
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        GridCircleItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.grid_circle_item, parent, false);
        binding.setColorsData(ColorsHelper.INSTANCE);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, @SuppressLint("RecyclerView") int i) {
        if (itemsList.size() > 0) {
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.circularItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.circularItemBinding.ivContinuePlay.setVisibility(View.GONE);
                holder.circularItemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    if (contentsItem.getPosterURL() != null && !contentsItem.getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(holder.circularItemBinding.itemImage.getContext())
                                .loadCIRImage2(holder.circularItemBinding.itemImage, contentsItem.getPosterURL(),null);
                    }

                    holder.circularItemBinding.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onRowItemClicked(itemsList.get(i), i);
                        }
                    });
                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    holder.circularItemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        ActivityLauncher.getInstance().seriesDetailScreen((Activity) holder.circularItemBinding.itemImage.getContext(), SeriesDetailActivity.class, itemsList.get(i).getId());

                    });
                    ImageHelper.getInstance(holder.circularItemBinding.itemImage.getContext())
                            .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "CIRCLE") + itemsList.get(i).getPosterURL());

                } else if (contentType.equalsIgnoreCase(AppConstants.CAST_AND_CREW)) {

                    ImageHelper.getInstance(holder.circularItemBinding.itemImage.getContext())
                            .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.CAST_AND_CREW, "CIRCLE") + itemsList.get(i).getPosterURL());
                } else if (contentType.equalsIgnoreCase(AppConstants.GENRE)) {

                    ImageHelper.getInstance(holder.circularItemBinding.itemImage.getContext())
                            .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.GENRE, "CIRCLE") + itemsList.get(i).getPosterURL());

                }


            }
        } else if (continuelist.size() > 0) {

            if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                holder.circularItemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.circularItemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.circularItemBinding.flNew.setVisibility(View.GONE);

                if (continuelist.get(i).getUserAssetDetail().isPremium()) {
                    holder.circularItemBinding.flExclusive.setVisibility(View.VISIBLE);
                } else {
                    holder.circularItemBinding.flExclusive.setVisibility(View.GONE);
                }
                ImageHelper.getInstance(holder.circularItemBinding.itemImage.getContext())
                        .loadImageTo(holder.circularItemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "CIRCLE") + continuelist.get(i).getUserAssetDetail().getPortraitImage());

                holder.circularItemBinding.itemImage.setOnClickListener(v -> {
                    if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                        if (continuelist.size() > 0 && (continuelist.get(i).getUserAssetDetail().getAssetType()) != null) {
                            AppCommonMethod.redirectionLogic(holder.circularItemBinding.itemImage.getContext(),railCommonData,i);
                          //  AppCommonMethod.launchDetailScreen(holder.circularItemBinding.itemImage.getContext(),0l, continuelist.get(i).getUserAssetDetail().getAssetType(), continuelist.get(i).getUserAssetDetail().getId(), String.valueOf(continuelist.get(i).getUserAssetStatus().getPosition()), continuelist.get(i).getUserAssetDetail().isPremium());
                        }
                    }
                });

            } else {
                holder.circularItemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.circularItemBinding.ivContinuePlay.setVisibility(View.GONE);
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

        final GridCircleItemBinding circularItemBinding;

        SingleItemRowHolder(GridCircleItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            circularItemBinding = circularItemBind;
        }

    }


}
