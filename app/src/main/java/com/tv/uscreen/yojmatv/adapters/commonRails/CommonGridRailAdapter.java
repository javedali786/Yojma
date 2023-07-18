package com.tv.uscreen.yojmatv.adapters.commonRails;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.enveu.client.enums.RailCardSize;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.CommonRailtItemClickListner;
;
;
;
import com.tv.uscreen.yojmatv.databinding.GridLandscapeItemBinding;
import com.tv.uscreen.yojmatv.databinding.GridLandscapeItemLargeBinding;
import com.tv.uscreen.yojmatv.databinding.GridLandscapeItemSmallBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class CommonGridRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private final RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private final CommonRailtItemClickListner listner;
    private final int pos;
    BaseCategory baseCategory;

    public CommonGridRailAdapter(RailCommonData railCommonData, int position, CommonRailtItemClickListner listner, BaseCategory baseCat) {
        this.railCommonData = railCommonData;
        this.videos = new ArrayList<>();
        this.videos = railCommonData.getEnveuVideoItemBeans();
        this.listner = listner;
        this.pos=position;
        this.baseCategory=baseCat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("grid", "onCreateViewHolder: " + viewType);
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                GridLandscapeItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.grid_landscape_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                //ThemeHandler.getInstance().applyheadingGridLandscapeBig1(parent.getContext(),binding);
                return new CommonGridRailAdapter.NormalHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                GridLandscapeItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.grid_landscape_item_small, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                //ThemeHandler.getInstance().applyheadingGridCommonPosterRailAdapter(parent.getContext(),binding);
                return new CommonGridRailAdapter.SmallHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                GridLandscapeItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.grid_landscape_item_large, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new CommonGridRailAdapter.LargeHolder(binding);
            }else {
                GridLandscapeItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.grid_landscape_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                //ThemeHandler.getInstance().applyheadingGridLandscapeBig1(parent.getContext(),binding);
                return new CommonGridRailAdapter.NormalHolder(binding);
            }
        }else {
            GridLandscapeItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.grid_landscape_item, parent, false);
            binding.setColorsData(ColorsHelper.INSTANCE);
            //ThemeHandler.getInstance().applyheadingGridLandscapeBig1(parent.getContext(),binding);
            return new CommonGridRailAdapter.NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Log.w("callinGonBind","in");
        if (holder instanceof CommonGridRailAdapter.NormalHolder) {
            setNomalValues(((CommonGridRailAdapter.NormalHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof CommonGridRailAdapter.SmallHolder) {
            setSmallValues(((CommonGridRailAdapter.SmallHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof CommonGridRailAdapter.LargeHolder) {
            setLargeValues(((CommonGridRailAdapter.LargeHolder) holder).circularItemBinding,i);
        }
        else {
            setNomalValues(((CommonGridRailAdapter.NormalHolder) holder).circularItemBinding,i);
        }
    }

    private void setNomalValues(GridLandscapeItemBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });
       /* try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception ignored){

        }*/

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
        if (videos.get(i).getVideoPosition() > 0) {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, true);
            double totalDuration = videos.get(i).getDuration();
            double currentPosition = videos.get(i).getVideoPosition() * 1000.0;
            double percentagePlayed = ((currentPosition / totalDuration) * 100L);
            itemBinding.setProgress((int) percentagePlayed);
        } else {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, false);
        }

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListImage(itemBinding.itemImage, AppCommonMethod.getListLDSImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception e){
            Logger.w(e);
        }
    }


    public void itemClick(int position) {
        Logger.d("clickedfrom list " + position);

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(railCommonData, position);
    }


    private void setSmallValues(GridLandscapeItemSmallBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }


        if (videos.get(i).getVideoPosition() > 0) {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, true);
            double totalDuration = videos.get(i).getDuration();
            double currentPosition = videos.get(i).getVideoPosition() * 1000.0;
            double percentagePlayed = ((currentPosition / totalDuration) * 100L);
            itemBinding.setProgress((int) percentagePlayed);
        } else {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, false);
        }

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListImage(itemBinding.itemImage, AppCommonMethod.getListLDSImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception e){
            Logger.w(e);
        }
    }

    private void setLargeValues(GridLandscapeItemLargeBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception e){
            Logger.w(e);
        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
        if (videos.get(i).getVideoPosition() > 0) {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, true);
            double totalDuration = videos.get(i).getDuration();
            double currentPosition = videos.get(i).getVideoPosition() * 1000.0;
            double percentagePlayed = ((currentPosition / totalDuration) * 100L);
            itemBinding.setProgress((int) percentagePlayed);
        } else {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, false);
        }

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListImage(itemBinding.itemImage, AppCommonMethod.getListLDSImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }
    }


    @Override
    public int getItemCount() {
        if(videos.size()>baseCategory.getListingLayoutContentSize()){
            return baseCategory.getListingLayoutContentSize();
        }else{
            return videos.size();
        }


    }


    public class NormalHolder extends RecyclerView.ViewHolder {

        final GridLandscapeItemBinding circularItemBinding;

        NormalHolder(GridLandscapeItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }
    }

        public class SmallHolder extends RecyclerView.ViewHolder {

            final GridLandscapeItemSmallBinding circularItemBinding;

            SmallHolder(GridLandscapeItemSmallBinding circularItemBind) {
                super(circularItemBind.getRoot());
                this.circularItemBinding = circularItemBind;

            }

        }

        public class LargeHolder extends RecyclerView.ViewHolder {

            final GridLandscapeItemLargeBinding circularItemBinding;

            LargeHolder(GridLandscapeItemLargeBinding circularItemBind) {
                super(circularItemBind.getRoot());
                this.circularItemBinding = circularItemBind;

            }

        }

}
