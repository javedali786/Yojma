package com.tv.adapters.commonRails;

import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.tv.R;
import com.tv.activities.listing.callback.ItemClickListener;
import com.tv.beanModel.ContinueRailModel.CommonContinueRail;
import com.tv.beanModel.responseModels.series.season.ItemsItem;
import com.tv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.databinding.PosterLandscapeItemBinding;
import com.tv.utils.Logger;
import com.tv.utils.colorsJson.converter.ColorsHelper;
import com.tv.utils.commonMethods.AppCommonMethod;
import com.tv.utils.constants.AppConstants;
import com.tv.utils.helpers.ImageHelper;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

public class  CommonPosterLandscapeAdapter extends RecyclerView.Adapter<CommonPosterLandscapeAdapter.SingleItemRowHolder> {

    private final List<ItemsItem> seasonItems;
    private final String contentType;
    private final List<EnveuVideoItemBean> itemsList;
    private long mLastClickTime = 0;
    private final int itemWidth;
    private final int itemHeight;
    private final ArrayList<CommonContinueRail> continuelist;
    private final String isLogin;
    private final KsPreferenceKeys preference;
    private final ItemClickListener listner;

    BaseCategory baseCategory;
    public CommonPosterLandscapeAdapter(Activity context, List<EnveuVideoItemBean> itemsList, List<ItemsItem> itemsItems, String contentType, ArrayList<CommonContinueRail> continuelist, BaseCategory baseCat, ItemClickListener listner) {
        this.itemsList = itemsList;
        this.seasonItems = itemsItems;
        this.contentType = contentType;
        this.continuelist = continuelist;
        this.baseCategory=baseCat;
        this.listner = listner;
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        int num = 2;
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            if (context.getResources().getConfiguration().orientation == 2)
                num = 4;
            else
                num = 3;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        (context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need two fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 80) / num;
        itemHeight = (int) ((itemWidth * 2) / 10);
    }

    public void notifydata(List<EnveuVideoItemBean> i) {
        this.itemsList.addAll(i);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PosterLandscapeItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.poster_landscape_item, parent, false);
        binding.setColorsData(ColorsHelper.INSTANCE);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        holder.itemBinding.itemImage.getLayoutParams().width = itemWidth;
        holder.itemBinding.itemImage.getLayoutParams().height = itemHeight;
        holder.itemBinding.llContinueProgress.setVisibility(View.GONE);
        holder.itemBinding.ivContinuePlay.setVisibility(View.GONE);

        if (itemsList.size() > 0) {

            try {
                AppCommonMethod.handleTags(itemsList.get(i).getIsVIP(),itemsList.get(i).getIsNewS(),
                        holder.itemBinding.flExclusive,holder.itemBinding.flNew,holder.itemBinding.flEpisode,holder.itemBinding.flNewMovie,itemsList.get(i).getAssetType());

            }catch (Exception e){
                Logger.w(e);
            }

            try {
                AppCommonMethod.handleTitleDesc(holder.itemBinding.titleLayout,holder.itemBinding.tvTitle,holder.itemBinding.tvDescription,baseCategory);
                holder.itemBinding.tvTitle.setText(itemsList.get(i).getTitle());
                holder.itemBinding.tvDescription.setText(itemsList.get(i).getDescription());
            }catch (Exception ignored){

            }

            Logger.d( "CommonPosterLandscapeAdapter");
            EnveuVideoItemBean contentsItem = itemsList.get(i);
            if (contentsItem != null) {
                holder.itemBinding.setPlaylistItem(contentsItem);
                holder.itemBinding.tvTitle.setText(itemsList.get(i).getTitle());

                try {
                    if (contentsItem.getVideoPosition() > 0) {
                        AppCommonMethod.railBadgeVisibility(holder.itemBinding.llContinueProgress, true);
                        int totalDuration = contentsItem.getVideoDetails().getDuration() / 1000;
                        int currentPosition = (int) contentsItem.getVideoPosition();
                        int percentagePlayed = (currentPosition*100/totalDuration);
                        holder.itemBinding.setProgress((int) percentagePlayed);
                    } else {
                        AppCommonMethod.railBadgeVisibility(holder.itemBinding.llContinueProgress, false);
                    }
                }catch (Exception e){
                    Logger.w(e);
                }

                if (contentType.equalsIgnoreCase(AppConstants.VOD)) {

                    if (contentsItem.getPosterURL() != null && !contentsItem.getPosterURL().equalsIgnoreCase("")) {
                        ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                                .loadListImage(holder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(contentsItem.getPosterURL(), holder.itemBinding.itemImage.getContext()));
                    }

                    holder.itemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                            return;
                        }
                        itemClick(i);
                    });

                } else if (contentType.equalsIgnoreCase(AppConstants.SERIES)) {
                    holder.itemBinding.itemImage.setOnClickListener(view -> {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                            return;
                        }
                        itemClick(i);
                    });

                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                            .loadListImage(holder.itemBinding.itemImage, AppCommonMethod.getImageUrl(AppConstants.SERIES, "POSTER_LANDSCAPE") + itemsList.get(i).getPosterURL());

                }
            }
        } else if (seasonItems.size() > 0) {
            ItemsItem sItem = seasonItems.get(i);
            ImageHelper.getInstance(holder.itemBinding.itemImage.getContext())
                    .loadListImage(holder.itemBinding.itemImage, AppCommonMethod.urlPoints + AppConstants.FILTER + AppConstants.QUALITY + "(100):format(webP):maxbytes(800)" + AppConstants.VIDEO_IMAGE_BASE_KEY + seasonItems.get(i).getLandscapeImage());
            holder.itemBinding.itemImage.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                itemClick(i);
               /* if (!StringUtils.isNullOrEmptyOrZero(sItem.getVideoType())) {
                    if (sItem.getVideoType().equalsIgnoreCase("EPISODE")) {
                        ActivityLauncher.getInstance().episodeScreen((Activity) holder.itemBinding.itemImage.getContext(), EpisodeActivity.class, sItem.getId(), "", sItem.isPremium());
                    } else {
                        ActivityLauncher.getInstance().detailScreen((Activity) holder.itemBinding.itemImage.getContext(), DetailActivity.class, sItem.getId(), "", sItem.isPremium());
                    }
                }
*/
            });


        }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (itemsList.size() > 0) {
            itemCount = itemsList.size();
        } else if (seasonItems.size() > 0) {
            itemCount = seasonItems.size();
        } else if (continuelist.size() > 0) {
            itemCount = continuelist.size();
        }
        return itemCount;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        PosterLandscapeItemBinding itemBinding;

        SingleItemRowHolder(PosterLandscapeItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public void itemClick(int position) {
        Logger.d("clickedfrom list " + position);

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.onRowItemClicked(itemsList.get(position), position);
    }


}

