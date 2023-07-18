package com.tv.uscreen.yojmatv.adapters.commonRails;

import android.content.Context;
import android.os.SystemClock;
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
import com.tv.uscreen.yojmatv.databinding.PotraitItemBinding;
import com.tv.uscreen.yojmatv.databinding.PotraitItemLargeBinding;
import com.tv.uscreen.yojmatv.databinding.PotraitItemSmallBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class CommonPotraitRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private final RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private final CommonRailtItemClickListner listner;
    private final int pos;
    BaseCategory baseCategory;
    public CommonPotraitRailAdapter(RailCommonData railCommonData, int position, CommonRailtItemClickListner listner,BaseCategory baseCat) {
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
                PotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                PotraitItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item_small, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new SmallHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                PotraitItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item_large, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new LargeHolder(binding);
            }
            else {
                PotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);
            }
        }else {
            PotraitItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.potrait_item, parent, false);
            binding.setColorsData(ColorsHelper.INSTANCE);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof   NormalHolder) {
            setNomalValues(((  NormalHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof   SmallHolder) {
            setSmallValues(((  SmallHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof   LargeHolder) {
            setLargeValues(((LargeHolder) holder).circularItemBinding,i);
        }
        else {
            setNomalValues(((  NormalHolder) holder).circularItemBinding,i);
        }
    }

    private void setLargeValues(PotraitItemLargeBinding itemBinding, int i) {
        EnveuVideoItemBean enveuVideoItemBean=videos.get(i);
        // Logger.w("posterValue-->>",videos.get(i).getPosterURL());
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i,itemBinding.rippleView.getContext());

        });

        try {
            AppCommonMethod.handleTags(enveuVideoItemBean.getIsVIP(),enveuVideoItemBean.getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,enveuVideoItemBean.getAssetType());

        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
        try {
            if (enveuVideoItemBean.getPosterURL() != null && !enveuVideoItemBean.getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadPortraitImage(itemBinding.itemImage, AppCommonMethod.getListPRImage(enveuVideoItemBean.getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }
    }

    private void setSmallValues(PotraitItemSmallBinding itemBinding, int i) {
        EnveuVideoItemBean enveuVideoItemBean=videos.get(i);
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i, itemBinding.rippleView.getContext());

        });

        try {
            AppCommonMethod.handleTags(enveuVideoItemBean.getIsVIP(),enveuVideoItemBean.getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,enveuVideoItemBean.getAssetType());

        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
        try {
            if (enveuVideoItemBean.getPosterURL() != null && !enveuVideoItemBean.getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadPortraitImage(itemBinding.itemImage, AppCommonMethod.getListPRImage(enveuVideoItemBean.getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }
    }

    private void setNomalValues(PotraitItemBinding itemBinding, int i) {
        EnveuVideoItemBean enveuVideoItemBean=videos.get(i);
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i, itemBinding.rippleView.getContext());

        });

        try {
            AppCommonMethod.handleTags(enveuVideoItemBean.getIsVIP(),enveuVideoItemBean.getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,enveuVideoItemBean.getAssetType());

        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
        try {
            if (enveuVideoItemBean.getPosterURL() != null && !enveuVideoItemBean.getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadPortraitImage(itemBinding.itemImage, AppCommonMethod.getListPRImage(enveuVideoItemBean.getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }

    }

    public void itemClick(int position, Context context) {
        Logger.d("clickedfrom list " + position);

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(railCommonData, position);
        AppCommonMethod.trackFcmEvent("Content Screen","", context,0);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        final PotraitItemBinding circularItemBinding;

        NormalHolder(PotraitItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final PotraitItemSmallBinding circularItemBinding;

        SmallHolder(PotraitItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final PotraitItemLargeBinding circularItemBinding;

        LargeHolder(PotraitItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
