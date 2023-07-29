package com.tv.uscreen.yojmatv.adapters.commonRails;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
import com.tv.uscreen.yojmatv.databinding.LandscapeItemBinding;
import com.tv.uscreen.yojmatv.databinding.LandscapeItemLargeBinding;
import com.tv.uscreen.yojmatv.databinding.LandscapeItemSmallBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class CommonLandscapeRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private final RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private final CommonRailtItemClickListner listner;
    private final int pos;
    BaseCategory baseCategory;
    public CommonLandscapeRailAdapter(RailCommonData railCommonData, int position, CommonRailtItemClickListner listner, BaseCategory baseCat) {
        this.railCommonData = railCommonData;
        this.videos = new ArrayList<>();
        this.videos = railCommonData.getEnveuVideoItemBeans();
        this.listner = listner;
        this.pos=position;
        this.baseCategory=baseCat;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {

                LandscapeItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);


            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                LandscapeItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item_small, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new SmallHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                LandscapeItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item_large, parent, false);
                return new LargeHolder(binding);
            }else {
                LandscapeItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);
            }
        }else {
            LandscapeItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.landscape_item, parent, false);
            binding.setColorsData(ColorsHelper.INSTANCE);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Log.w("callinGonBind","in");
        if (holder instanceof NormalHolder) {
            setNomalValues((( NormalHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof  SmallHolder) {
            setSmallValues((( SmallHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof  LargeHolder) {
            setLargeValues((( LargeHolder) holder).circularItemBinding,i);
        }
        else {
            setNomalValues((( NormalHolder) holder).circularItemBinding,i);
        }

    }

    private void setLargeValues(LandscapeItemLargeBinding itemBinding, int i) {
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
        try {
            if (videos.get(i).getVideoPosition() > 0) {
                AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, true);
                int totalDuration = videos.get(i).getVideoDetails().getDuration() / 1000;
                int currentPosition = (int) videos.get(i).getVideoPosition();
                int percentagePlayed = (currentPosition*100/totalDuration);
                itemBinding.setProgress((int) percentagePlayed);
            } else {
                AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, false);
            }
        }catch (Exception e){
            Logger.w(e);
        }

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListImage(itemBinding.itemImage, AppCommonMethod.getListLDSImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }
    }

    private void setSmallValues(LandscapeItemSmallBinding itemBinding, int i) {
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


        try {
            if (videos.get(i).getVideoPosition() > 0) {
                AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, true);
                int totalDuration = videos.get(i).getVideoDetails().getDuration() / 1000;
                int currentPosition = (int) videos.get(i).getVideoPosition();
                int percentagePlayed = (currentPosition*100/totalDuration);
                itemBinding.setProgress((int) percentagePlayed);
            } else {
                AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, false);
            }
        }catch (Exception e){
            Logger.w(e);
        }

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListImage(itemBinding.itemImage, AppCommonMethod.getListLDSImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception e){
            Logger.w(e);
        }
    }

    private void setNomalValues(LandscapeItemBinding itemBinding, int i) {
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
        try {
        if (videos.get(i).getVideoPosition() > 0) {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, true);
            int totalDuration = videos.get(i).getVideoDetails().getDuration() / 1000;
            int currentPosition = (int) videos.get(i).getVideoPosition();
            int percentagePlayed = (currentPosition*100/totalDuration);
            itemBinding.setProgress((int) percentagePlayed);
        } else {
            AppCommonMethod.railBadgeVisibility(itemBinding.llContinueProgress, false);
        }
        }catch (Exception e){
            Logger.w(e);
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

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        final LandscapeItemBinding circularItemBinding;

        NormalHolder(LandscapeItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final LandscapeItemSmallBinding circularItemBinding;

        SmallHolder(LandscapeItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final LandscapeItemLargeBinding circularItemBinding;

        LargeHolder(LandscapeItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
