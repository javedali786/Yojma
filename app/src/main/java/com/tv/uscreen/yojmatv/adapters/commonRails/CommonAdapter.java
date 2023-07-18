package com.tv.uscreen.yojmatv.adapters.commonRails;

import static com.tv.uscreen.yojmatv.utils.constants.AppConstants.APP_CONTINUE_WATCHING;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.beanModel.ContinueRailModel.CommonContinueRail;
import com.tv.uscreen.yojmatv.beanModel.responseModels.landingTabResponses.CommonRailData;
;
;
;
;
;
;
;
;
import com.tv.uscreen.yojmatv.databinding.BannerLayoutBinding;
import com.tv.uscreen.yojmatv.databinding.CircleRecyclerItemBinding;
import com.tv.uscreen.yojmatv.databinding.HeaderRecyclerItemBinding;
import com.tv.uscreen.yojmatv.databinding.LandscapeRecyclerItemBinding;
import com.tv.uscreen.yojmatv.databinding.PosterLandscapeRecyclerItemBinding;
import com.tv.uscreen.yojmatv.databinding.PosterPotraitRecyclerItemBinding;
import com.tv.uscreen.yojmatv.databinding.PotraitRecyclerItemBinding;
import com.tv.uscreen.yojmatv.databinding.SquareRecyclerItemBinding;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.helpers.GravitySnapHelper;
import com.tv.uscreen.yojmatv.utils.helpers.carousel.model.Slide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdsLoader.AdsLoadedListener {
    private final Activity activity;
    private final List<CommonRailData> dataList;
    private ArrayList<Slide> slides;


    public CommonAdapter(Activity activity, List<CommonRailData> demoList, ArrayList<Slide> slides) {
        this.activity = activity;
        this.dataList = demoList;
        this.slides = slides;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                //TYPE_HEADER
                HeaderRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.header_recycler_item, parent, false);
                return new HeaderHolder(binding);
            }
            case 1: {
                //TYPE_SQUARE
                SquareRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_recycler_item, parent, false);
                binding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                return new SqureHolder(binding);
            }
            case 2: {
                //TYPE_CIRCLE
                CircleRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.circle_recycler_item, parent, false);
                binding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                return new CircleHolder(binding);
            }
            case 3: {
                //TYPE_LANDSCAPE
                LandscapeRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_recycler_item, parent, false);
                binding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                return new LandscapeHolder(binding);
            }
            case 4: {
                //TYPE_BANNER
                BannerLayoutBinding bannerLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.banner_layout, parent, false);
                return new BannerHolder(bannerLayoutBinding);
            }
            case 5: {
                //TYPE_POSTER_LANDSCAPE
                PosterLandscapeRecyclerItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.poster_landscape_recycler_item, parent, false);
                itemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                return new PosterLandscapeHolder(itemBinding);
            }
            case 6: {
                //TYPE_POSTER_POTRAIT
                PosterPotraitRecyclerItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.poster_potrait_recycler_item, parent, false);
                itemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                return new PosterPotraitHolder(itemBinding);
            }
            default: {
                PotraitRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_recycler_item, parent, false);
                binding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                return new PortrateHolder(binding);
            }
        }

    }

    public void onClear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

    }


    private void setMoreVisibilty(View view, int count) {
        if (count > 10)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }

    private void setDivider(View mView, int position) {
        mView.setVisibility(View.GONE);
       /* AppCommonMethod.adsRail.size();
        int check = position / 5;
        if (position % 5 == 0 && check < AppCommonMethod.adsRail.size()) {
            Logger.e("check", "check" + check);
            Logger.e("adsRail", "adsRail" + AppCommonMethod.adsRail.size());
            mView.setVisibility(View.GONE);
        } else {
            mView.setVisibility(View.VISIBLE);
        }*/
    }

    private void setRecylerProperties(RecyclerView recyclerView, TextView header, String text) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        header.setText(text);
        // recyclerView.addItemDecoration(new SpacingItemDecoration(12, SpacingItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public void notifyAdapter(List<CommonRailData> commonRailData, ArrayList<Slide> slides) {
        if (this.dataList != null) {
            this.dataList.clear();
            this.dataList.addAll(commonRailData);
        }
        if (this.slides != null) {
            this.slides.clear();
            this.slides = slides;
        }
        notifyDataSetChanged();
    }

    public boolean hasContinueRail() {
        boolean hasContinueRailInList = false;
        Iterator itr = dataList.iterator();
        while (itr.hasNext()) {
            CommonRailData x = (CommonRailData) itr.next();
            if (x.getRailData() != null && x.getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                hasContinueRailInList = true;
                return hasContinueRailInList;
            }
        }
        return hasContinueRailInList;
    }

    public void addDataToList(int index, CommonRailData newRail) {
        dataList.add(index, newRail);
        notifyItemInserted(index);
    }

    public void notifyAdapterContinue(ArrayList<CommonContinueRail> continueRailAdapter) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getRailData() != null && dataList.get(i).getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                if (continueRailAdapter.isEmpty()) {
                    removeItem();
                    notifyItemRemoved(i);
                } else {
                    addItem(continueRailAdapter);
                    notifyItemChanged(i);
                }
            }
        }
     /*   if(dataList.size()>3)
        notifyItemRangeChanged(2, (null != dataList ? dataList.size() : 0));
        else
            notifyItemRangeChanged(0, (null != dataList ? dataList.size() : 0));
        notifyDataSetChanged();*/

    }

    public void addItem(ArrayList<CommonContinueRail> continueRailAdapter) {
        Iterator itr = dataList.iterator();
        while (itr.hasNext()) {
            CommonRailData x = (CommonRailData) itr.next();
            if (x.getRailData() != null && x.getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                if (x.getContinueRailAdapter() != null) {
                    x.getContinueRailAdapter().clear();
                    x.getContinueRailAdapter().addAll(continueRailAdapter);
                }
            }
        }
    }

    public void removeItem() {
        Iterator itr = dataList.iterator();
        while (itr.hasNext()) {
            CommonRailData x = (CommonRailData) itr.next();
            if (x.getRailData() != null && x.getRailData().getData().getDisplayName().toUpperCase().contains(APP_CONTINUE_WATCHING)) {
                itr.remove();
            }
        }
    }

    @Override
    public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {

    }


    public class HeaderHolder extends RecyclerView.ViewHolder {

        final HeaderRecyclerItemBinding headerRecyclerItemBinding;

        HeaderHolder(HeaderRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            headerRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    public class CircleHolder extends RecyclerView.ViewHolder {

        final CircleRecyclerItemBinding circularRecyclerItemBinding;

        CircleHolder(CircleRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            circularRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    public class PosterPotraitHolder extends RecyclerView.ViewHolder {

        PosterPotraitRecyclerItemBinding itemBinding;

        PosterPotraitHolder(PosterPotraitRecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class PosterLandscapeHolder extends RecyclerView.ViewHolder {

        final PosterLandscapeRecyclerItemBinding itemBinding;

        PosterLandscapeHolder(PosterLandscapeRecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public class SqureHolder extends RecyclerView.ViewHolder {
        final SquareRecyclerItemBinding squareRecyclerItemBinding;

        SqureHolder(SquareRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            squareRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    public class LandscapeHolder extends RecyclerView.ViewHolder {
        final LandscapeRecyclerItemBinding landscapeRecyclerItemBinding;

        LandscapeHolder(LandscapeRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    class BannerHolder extends RecyclerView.ViewHolder {
        final BannerLayoutBinding bannerLayoutBinding;

        BannerHolder(BannerLayoutBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            bannerLayoutBinding = flightItemLayoutBinding;
        }
    }

    public class PortrateHolder extends RecyclerView.ViewHolder {
        final PotraitRecyclerItemBinding potraitRecyclerItemBinding;

        PortrateHolder(PotraitRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;
        }
    }
}

