package com.breadgangtvnetwork.adapters.commonRails;

import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.breadgangtvnetwork.R;
import com.breadgangtvnetwork.activities.listing.callback.ItemClickListener;
import com.breadgangtvnetwork.beanModel.ContinueRailModel.CommonContinueRail;
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData;
import com.breadgangtvnetwork.beanModel.responseModels.series.season.ItemsItem;
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.breadgangtvnetwork.databinding.PosterPotraitItemBinding;
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper;
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod;
import com.breadgangtvnetwork.utils.constants.AppConstants;
import com.breadgangtvnetwork.utils.helpers.ImageHelper;
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;

import java.util.ArrayList;
import java.util.List;

public class CommonPosterPotraitAdapter extends RecyclerView.Adapter<CommonPosterPotraitAdapter.SingleItemRowHolder> {

    private static List<EnveuVideoItemBean> itemsList;
    private final List<ItemsItem> seasonItems;
    private final String contentType;
    private long mLastClickTime = 0;
    private final int itemWidth;
    private final int itemHeight;
    private final ArrayList<CommonContinueRail> continuelist;
    private final String isLogin;
    private final KsPreferenceKeys preference;
    private final ItemClickListener listener;
    BaseCategory baseCategory;
    public CommonPosterPotraitAdapter(Activity context, List<EnveuVideoItemBean> itemsList, List<ItemsItem> itemsItems, String contentType, ArrayList<CommonContinueRail> continuelist, ItemClickListener callBack, BaseCategory baseCat, RailCommonData playlistRailData) {
        CommonPosterPotraitAdapter.itemsList = itemsList;
        this.seasonItems = itemsItems;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.listener = callBack;
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        this.baseCategory=baseCat;
        int num = 3;
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            if (context.getResources().getConfiguration().orientation == 2)
                num = 5;
            else
                num = 4;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels) / num;
        itemHeight = (itemWidth * 3) / 2;
    }

    public void notifydata(List<EnveuVideoItemBean> i) {
        itemsList.addAll(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommonPosterPotraitAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PosterPotraitItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.poster_potrait_item, parent, false);
        binding.setColorsData(ColorsHelper.INSTANCE);
        return new CommonPosterPotraitAdapter.SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonPosterPotraitAdapter.SingleItemRowHolder holder, int i) {
        holder.itemBinding.llContinueProgress.setVisibility(View.GONE);
        holder.itemBinding.ivContinuePlay.setVisibility(View.GONE);

        if (itemsList.size() > 0) {
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {

                try {
                    AppCommonMethod.handleTitleDesc(holder.itemBinding.titleLayout,holder.itemBinding.tvTitle,holder.itemBinding.tvDescription,baseCategory);
                    holder.itemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                    holder.itemBinding.tvDescription.setText(itemsList.get(i).getDescription());
                }catch (Exception ignored){

                }
                holder.itemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    if (itemsList.get(i).getPosterURL() != null && !itemsList.get(i).getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageToListPortrait(holder.itemBinding.itemImage, AppCommonMethod.getListPRImage(itemsList.get(i).getPosterURL(), holder.itemBinding.itemImage.getContext()));
                    }
                    holder.itemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        listener.onRowItemClicked(itemsList.get(i), i);
                    });

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    holder.itemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        listener.onRowItemClicked(itemsList.get(i), i);
                    });
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                            .loadImageToListPortrait(holder.itemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "POTRIAT") + itemsList.get(i).getPosterURL());

                }


            }
        } else if (seasonItems.size() > 0) {

            ItemsItem sItem = seasonItems.get(i);
            if (sItem.isPremium()) {
                holder.itemBinding.flExclusive.setVisibility(View.VISIBLE);
            } else {
                holder.itemBinding.flExclusive.setVisibility(View.GONE);
            }

            if (sItem.isNew()) {
                holder.itemBinding.flNew.setVisibility(View.VISIBLE);
            } else {
                holder.itemBinding.flNew.setVisibility(View.GONE);
            }

            ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                    .loadImageToListPortrait(holder.itemBinding.itemImage, AppCommonMethod.urlPoints + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(800)" + AppConstants.VIDEO_IMAGE_BASE_KEY + seasonItems.get(i).getLandscapeImage());


            holder.itemBinding.itemImage.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                listener.onRowItemClicked(itemsList.get(i), i);
            });
        } else if (continuelist.size() > 0) {
            if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                holder.itemBinding.llContinueProgress.setVisibility(View.VISIBLE);
                holder.itemBinding.ivContinuePlay.setVisibility(View.VISIBLE);
                holder.itemBinding.flNew.setVisibility(View.GONE);
                ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                        .loadImageToListPortrait(holder.itemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.VOD, "POTRIAT") + continuelist.get(i).getUserAssetDetail().getPosterPortraitImage());


                holder.itemBinding.itemImage.setOnClickListener(view -> {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    listener.onRowItemClicked(itemsList.get(i), i);
                });

            } else {
                holder.itemBinding.llContinueProgress.setVisibility(View.GONE);
                holder.itemBinding.ivContinuePlay.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (itemsList.size() > 0) {
            itemCount = itemsList.size();
        } else if (continuelist != null && continuelist.size() > 0) {
            itemCount = continuelist.size();
        }
        return itemCount;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        PosterPotraitItemBinding itemBinding;

        SingleItemRowHolder(PosterPotraitItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

}


