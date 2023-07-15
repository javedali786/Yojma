package com.breadgangtvnetwork.activities.listing.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.breadgangtvnetwork.R;
import com.breadgangtvnetwork.activities.listing.callback.ItemClickListener;
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.breadgangtvnetwork.databinding.ListCircleItemBinding;
import com.breadgangtvnetwork.databinding.ListCommonItemBinding;
import com.breadgangtvnetwork.databinding.ListLdsItemBinding;
import com.breadgangtvnetwork.databinding.ListPrItemBinding;
import com.breadgangtvnetwork.databinding.ListPrtwoItemBinding;
import com.breadgangtvnetwork.databinding.ListSquareItemBinding;
import com.breadgangtvnetwork.utils.Logger;
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper;
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod;
import com.breadgangtvnetwork.utils.helpers.ImageHelper;
import com.breadgangtvnetwork.utils.helpers.StringUtils;
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int limitView = 5;
    private final KsPreferenceKeys preference;
    private final List<EnveuVideoItemBean> list;
    private final ItemClickListener listener;
    private boolean isWatchList = false;
    private boolean isWatchHistory = false;
    private final Context context;


    public ListAdapter(List<EnveuVideoItemBean> list, ItemClickListener listener, int viewType,Context context) {
        this.list = list;
        this.listener = listener;
        this.context = context;
        limitView = viewType;
        preference = KsPreferenceKeys.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        return limitView;
    }

    public void notifyAdapter(List<EnveuVideoItemBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 0:
                ListCircleItemBinding itemBinding;
                itemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_circle_item, viewGroup, false);
                itemBinding.setColorsData(ColorsHelper.INSTANCE);
                return new CircleItemRowHolder(itemBinding);
            case 1:
                ListLdsItemBinding listLdsItemBinding;
                listLdsItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_lds_item, viewGroup, false);
                listLdsItemBinding.setColorsData(ColorsHelper.INSTANCE);
                return new LandscapeItemRowHolder(listLdsItemBinding);

            case 2:
                ListPrItemBinding listPrItemBinding;
                listPrItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_pr_item, viewGroup, false);
                listPrItemBinding.setColorsData(ColorsHelper.INSTANCE);
                return new PortraiteItemRowHolder(listPrItemBinding);
            case 3:
                ListPrtwoItemBinding listPrtwoItemBinding;
                listPrtwoItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_prtwo_item, viewGroup, false);
                listPrtwoItemBinding.setColorsData(ColorsHelper.INSTANCE);
                return new PortraiteTwoItemRowHolder(listPrtwoItemBinding);
            case 4:
                ListSquareItemBinding listSquareItemBinding;
                listSquareItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_square_item, viewGroup, false);
                //ThemeHandler.getInstance().applySqureListing(viewGroup.getContext(),listSquareItemBinding);
                listSquareItemBinding.setColorsData(ColorsHelper.INSTANCE);
                return new SquareItemRowHolder(listSquareItemBinding);
            default:
                ListLdsItemBinding listLdsItemBinding2;
                listLdsItemBinding2 = DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_lds_item, viewGroup, false);
                listLdsItemBinding2.setColorsData(ColorsHelper.INSTANCE);
                return new LandscapeItemRowHolder(listLdsItemBinding2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof CircleItemRowHolder) {
            setCircleData(((CircleItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof LandscapeItemRowHolder) {
            setLandscapeData(((LandscapeItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof PortraiteItemRowHolder) {
            setPortraiteData(((PortraiteItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof PortraiteTwoItemRowHolder) {
            setPortraiteTwoData(((PortraiteTwoItemRowHolder) viewHolder), position);
        } else if (viewHolder instanceof SquareItemRowHolder) {
            setSquareData(((SquareItemRowHolder) viewHolder), position);
        } else {
            setLandscapeData(((LandscapeItemRowHolder) viewHolder), position);
        }


    }


    private void setCircleData(CircleItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));

        try {
            if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                Logger.w("valuesFinal", AppCommonMethod.getListCIRCLEImage(list.get(position).getPosterURL(), viewHolder.itemBinding.itemImage.getContext()));
                ImageHelper.getInstance(viewHolder.itemBinding.itemImage.getContext()).loadCIRImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListCIRCLEImage(list.get(position).getPosterURL(), viewHolder.itemBinding.itemImage.getContext()), null);

            }
        } catch (Exception e) {

        }

        viewHolder.itemBinding.tvGenre.setVisibility(View.VISIBLE);
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
    }

    private void setLandscapeData(LandscapeItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());

        viewHolder.itemBinding.clRoot.setOnClickListener(view -> {
            listener.onRowItemClicked(list.get(position), position);
        });
        viewHolder.itemBinding.clRoot.setOnLongClickListener(v -> {
            if (isWatchHistory)
                listener.onDeleteWatchHistoryClicked(list.get(position).getId(), position);

            if (isWatchList)
                listener.onDeleteWatchListClicked(list.get(position).getId(), list.get(position).getTitle(),position);
            return true;
        });
        viewHolder.itemBinding.flDeleteWatchlist.setOnClickListener(view -> {

            if (isWatchHistory)
                listener.onDeleteWatchHistoryClicked(list.get(position).getId(), position);


            if (isWatchList)
                listener.onDeleteWatchListClicked(list.get(position).getId(),list.get(position).getTitle(), position);
        });

        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            // Glide.with(context).load(list.get(position).getPosterURL()).into(viewHolder.itemBinding.itemImage);
          //  ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadListImage(itemBinding.itemImage, AppCommonMethod.getListLDSImage(videos.get(i).getPosterURL(), itemBinding.itemImage.getContext()));
            ImageHelper.getInstance(viewHolder.itemBinding.itemImage.getContext()).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), viewHolder.itemBinding.itemImage.getContext()));
        } else {
            viewHolder.itemBinding.itemImage.setImageResource(R.drawable.placeholder_landscape);
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
        if (isWatchList || isWatchHistory) {
            viewHolder.itemBinding.flDeleteWatchlist.setVisibility(View.VISIBLE);
        } else {
            viewHolder.itemBinding.flDeleteWatchlist.setVisibility(View.GONE);
        }

        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(), list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive, viewHolder.itemBinding.flNew, viewHolder.itemBinding.flEpisode, viewHolder.itemBinding.flNewMovie, list.get(position).getAssetType());

        } catch (Exception ignored) {

        }
    }

    private void setPortraiteData(PortraiteItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));
        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            ImageHelper.getInstance(viewHolder.itemBinding.itemImage.getContext()).loadImageTo(viewHolder.itemBinding.itemImage, AppCommonMethod.getListPRImage(list.get(position).getPosterURL(), viewHolder.itemBinding.itemImage.getContext()));
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }

        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(), list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive, viewHolder.itemBinding.flNew, viewHolder.itemBinding.flEpisode, viewHolder.itemBinding.flNewMovie, list.get(position).getAssetType());

        } catch (Exception ignored) {

        }
    }

    private void setPortraiteTwoData(PortraiteTwoItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));

        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            ImageHelper.getInstance(viewHolder.itemBinding.itemImage.getContext()).loadImageTo(viewHolder.itemBinding.itemImage, AppCommonMethod.getListPRTwoImage(list.get(position).getPosterURL(), viewHolder.itemBinding.itemImage.getContext()));
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(), list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive, viewHolder.itemBinding.flNew, viewHolder.itemBinding.flEpisode, viewHolder.itemBinding.flNewMovie, list.get(position).getAssetType());

        } catch (Exception ignored) {

        }
    }


    private void setSquareData(SquareItemRowHolder viewHolder, int position) {
        viewHolder.itemBinding.setPlaylistItem(list.get(position));
        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));

        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
            Logger.d("imageUrl-->>" + list.get(position).getName() + " --->>" + list.get(position).getPosterURL());
            ImageHelper.getInstance(viewHolder.itemBinding.itemImage.getContext()).loadListSQRImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListSQRImage(list.get(position).getPosterURL(), viewHolder.itemBinding.itemImage.getContext()));
        }
        viewHolder.itemBinding.tvGenre.setText(AppCommonMethod.getGenre(list.get(position)));
        if (list.get(position).getDescription() != null && !list.get(position).getDescription().equalsIgnoreCase("")) {
            viewHolder.itemBinding.descriptionTxt.setText(list.get(position).getDescription());
        } else {
            viewHolder.itemBinding.descriptionTxt.setVisibility(View.GONE);
        }
        try {
            AppCommonMethod.handleTags(list.get(position).getIsVIP(), list.get(position).getIsNewS(),
                    viewHolder.itemBinding.flExclusive, viewHolder.itemBinding.flNew, viewHolder.itemBinding.flEpisode, viewHolder.itemBinding.flNewMovie, list.get(position).getAssetType());

        } catch (Exception ignored) {

        }
    }


    public void setImage(String imageKey, String imageUrl, ImageView view) {
        try {

            String url1 = preference.getAppPrefCfep();
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String tranform = "/fit-in/160x90/filters:quality(100):max_bytes(400)";

            StringBuilder stringBuilder = new StringBuilder(url1 + tranform + imageUrl + imageKey);
            Logger.e("", "imageURL" + stringBuilder);
            Glide.with(view.getContext())
                    .asBitmap()
                    .load(stringBuilder.toString())
                    .apply(AppCommonMethod.optionsSearch)
                    .into(view);
        } catch (Exception e) {
            Logger.e("", "" + e);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setWatchList() {
        isWatchList = true;
    }

    public void setWatchHistory() {
        isWatchHistory = true;
    }

    public class CircleItemRowHolder extends RecyclerView.ViewHolder {

        final ListCircleItemBinding itemBinding;

        public CircleItemRowHolder(ListCircleItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class LandscapeItemRowHolder extends RecyclerView.ViewHolder {

        final ListLdsItemBinding itemBinding;

        public LandscapeItemRowHolder(ListLdsItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class PortraiteItemRowHolder extends RecyclerView.ViewHolder {

        final ListPrItemBinding itemBinding;

        public PortraiteItemRowHolder(ListPrItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class PortraiteTwoItemRowHolder extends RecyclerView.ViewHolder {

        final ListPrtwoItemBinding itemBinding;

        public PortraiteTwoItemRowHolder(ListPrtwoItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class SquareItemRowHolder extends RecyclerView.ViewHolder {

        final ListSquareItemBinding itemBinding;

        public SquareItemRowHolder(ListSquareItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final ListCommonItemBinding itemBinding;

        public SingleItemRowHolder(ListCommonItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }
}


