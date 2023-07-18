package com.tv.adapters.commonRails;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.enveu.client.enums.RailCardSize;
import com.tv.R;
import com.tv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.callbacks.commonCallbacks.CommonRailtItemClickListner;
import com.tv.databinding.CircleItemBinding;
import com.tv.databinding.CircleItemLargeBinding;
import com.tv.databinding.CircleItemSmallBinding;
import com.tv.databinding.GrdCircleItemBinding;
import com.tv.utils.Logger;
import com.tv.utils.colorsJson.converter.ColorsHelper;
import com.tv.utils.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class CommonGridCircleRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private final RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private final CommonRailtItemClickListner listner;
    private final int pos;

    BaseCategory baseCategory;
    public CommonGridCircleRailAdapter(RailCommonData railCommonData, int position, CommonRailtItemClickListner listner, BaseCategory baseCat) {
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
                GrdCircleItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.grd_circle_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);
            }else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                CircleItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circle_item_small, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new SmallHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                CircleItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circle_item_large, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new LargeHolder(binding);
            }

            else {
                GrdCircleItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circle_item, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);
            }
        }else {
            GrdCircleItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.circle_item, parent, false);
            binding.setColorsData(ColorsHelper.INSTANCE);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof NormalHolder) {
            setNomalValues(((NormalHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof SmallHolder) {
            setSmallValues(((SmallHolder) holder).circularItemBinding,i);
        }
        else if (holder instanceof LargeHolder) {
            setLargeValues(((LargeHolder) holder).circularItemBinding,i);
        }
        else {
            setNomalValues(((NormalHolder) holder).circularItemBinding,i);
        }
    }

    private void setLargeValues(CircleItemLargeBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadCircleImageTo(itemBinding.itemImage, videos.get(i).getPosterURL());
            }
        }catch (Exception e){
            Logger.w(e);
        }
    }

    private void setSmallValues(CircleItemSmallBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });

        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadCircleImageTo(itemBinding.itemImage, videos.get(i).getPosterURL());            }
        }catch (Exception ignored){

        }}

    private void setNomalValues(GrdCircleItemBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);

        });
        try {
            Log.w("ImageOnCircle-->",videos.get(i).getPosterURL());
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadCircleImageTo(itemBinding.itemImage, videos.get(i).getPosterURL());            }
        }catch (Exception e){
            Log.w("ImageOnCircle-->",e.toString());
        }

    }
    public void itemClick(int position) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(railCommonData, position);
    }

    @Override
    public int getItemCount() {
        if(videos.size()>baseCategory.getListingLayoutContentSize()){
            return baseCategory.getListingLayoutContentSize();
        }else{
            return videos.size();
        }
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CircleItemBinding circularItemBinding;

        SingleItemRowHolder(CircleItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;
        }

    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        final GrdCircleItemBinding circularItemBinding;

        NormalHolder(GrdCircleItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final CircleItemSmallBinding circularItemBinding;

        SmallHolder(CircleItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final CircleItemLargeBinding circularItemBinding;

        LargeHolder(CircleItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
