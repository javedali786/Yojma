package com.tv.adapters.commonRails;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.enveu.client.enums.RailCardSize;
import com.tv.R;
import com.tv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.callbacks.commonCallbacks.CommonRailtItemClickListner;
import com.tv.databinding.GridSquareItemBinding;
import com.tv.databinding.SquareItemLargeBinding;
import com.tv.databinding.SquareItemSmallBinding;
import com.tv.utils.Logger;
import com.tv.utils.colorsJson.converter.ColorsHelper;
import com.tv.utils.commonMethods.AppCommonMethod;
import com.tv.utils.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;


public class GridCommonSquareRailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private long mLastClickTime = 0;
    private final RailCommonData railCommonData;
    private List<EnveuVideoItemBean> videos;
    private final CommonRailtItemClickListner listner;
    BaseCategory baseCategory;
    public GridCommonSquareRailAdapter(RailCommonData railCommonData, CommonRailtItemClickListner listner, BaseCategory baseCat) {
        this.railCommonData = railCommonData;
        this.videos = new ArrayList<>();
        this.videos = railCommonData.getEnveuVideoItemBeans();
        this.listner = listner;
        this.baseCategory=baseCat;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                GridSquareItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.grid_square_item, parent, false);
                //ThemeHandler.getInstance().applyheadingGridSqaureRailItem(parent.getContext(),binding);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                SquareItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item_small, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new SmallHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                SquareItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item_large, parent, false);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new LargeHolder(binding);
            }
            else {
                GridSquareItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item_small, parent, false);
                //ThemeHandler.getInstance().applyheadingGridSqaureRailItem(parent.getContext(),binding);
                binding.setColorsData(ColorsHelper.INSTANCE);
                return new NormalHolder(binding);
            }
        }else {
            GridSquareItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.grid_square_item, parent, false);
            //ThemeHandler.getInstance().applyheadingGridSqaureRailItem(parent.getContext(),binding);
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

    private void setLargeValues(SquareItemLargeBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);
        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception ignored){
            Logger.w("crashonTags",ignored.toString());
        }
        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListSQRImage(itemBinding.itemImage, AppCommonMethod.getListSQRImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
    }

    private void setSmallValues(SquareItemSmallBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);
        });
        try {
            AppCommonMethod.handleTags(videos.get(i).getIsVIP(),videos.get(i).getIsNewS(),
                    itemBinding.flExclusive,itemBinding.flNew,itemBinding.flEpisode,itemBinding.flNewMovie,videos.get(i).getAssetType());

        }catch (Exception ignored){
            Logger.w("crashonTags",ignored.toString());
        }
        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListSQRImage(itemBinding.itemImage, AppCommonMethod.getListSQRImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

        }
    }

    private void setNomalValues(GridSquareItemBinding itemBinding, int i) {
        itemBinding.setPlaylistItem(videos.get(i));
        itemBinding.rippleView.setOnClickListener(v -> {
            itemClick(i);
        });
        try {
            if (videos.get(i).getPosterURL() != null && !videos.get(i).getPosterURL().equalsIgnoreCase("")) {
                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListSQRImage(itemBinding.itemImage, AppCommonMethod.getListSQRImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            }
        }catch (Exception ignored){

        }

        try {
            AppCommonMethod.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory);
            itemBinding.tvTitle.setText(videos.get(i).getTitle());
            itemBinding.tvDescription.setText(videos.get(i).getDescription());
        }catch (Exception ignored){

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
    public class NormalHolder extends RecyclerView.ViewHolder {

        final GridSquareItemBinding circularItemBinding;

        NormalHolder(GridSquareItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class SmallHolder extends RecyclerView.ViewHolder {

        final SquareItemSmallBinding circularItemBinding;

        SmallHolder(SquareItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }

    public class LargeHolder extends RecyclerView.ViewHolder {

        final SquareItemLargeBinding circularItemBinding;

        LargeHolder(SquareItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;

        }

    }


}
