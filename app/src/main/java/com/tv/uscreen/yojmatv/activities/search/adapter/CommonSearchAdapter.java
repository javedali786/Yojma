package com.tv.uscreen.yojmatv.activities.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.yojmatv.beanModel.popularSearch.ItemsItem;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.SearchClickCallbacks;
;
import com.tv.uscreen.yojmatv.databinding.CommonSearchAdapterBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper;
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class CommonSearchAdapter extends RecyclerView.Adapter<CommonSearchAdapter.SingleItemRowHolder> {
    private final Context context;
    private final RailCommonData jsonObject;
    private final KsPreferenceKeys preference;
    private final SearchClickCallbacks listener;

    public CommonSearchAdapter(Context context, RailCommonData jsonObject, SearchClickCallbacks listener) {
        this.context = context;
        this.jsonObject = jsonObject;
        preference = KsPreferenceKeys.getInstance();
        this.listener = listener;
    }


    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CommonSearchAdapterBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.common_search_adapter, viewGroup, false);
        itemBinding.setColorsData(ColorsHelper.INSTANCE);
        //ThemeHandler.getInstance().applyRowSearch(viewGroup.getContext(),itemBinding);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, final int position) {
        List<EnveuVideoItemBean> itemList = jsonObject.getEnveuVideoItemBeans();
        viewHolder.searchItemBinding.tvTitle.setTextColor(context.getColor(R.color.series_detail_all_episode_txt_color));
        viewHolder.searchItemBinding.tvEpisode.setTextColor(context.getColor(R.color.unselected_indicator_color));

        if (itemList.size() > 0) {

            viewHolder.searchItemBinding.setPlaylistItem(itemList.get(position));
            if (itemList.get(position).getTitle() != null && !itemList.get(position).getTitle().equalsIgnoreCase("")) {
                viewHolder.searchItemBinding.tvTitle.setText(itemList.get(position).getTitle());
            } else {
                viewHolder.searchItemBinding.tvTitle.setText("");
            }

            if (itemList.get(position).getDescription() != null && !itemList.get(position).getDescription().equalsIgnoreCase("")) {
                viewHolder.searchItemBinding.tvEpisode.setText(itemList.get(position).getDescription());
            } else {
                viewHolder.searchItemBinding.tvEpisode.setText("");
            }
            if (itemList.get(position).getPosterURL()!=null && !itemList.get(position).getPosterURL().equalsIgnoreCase("")){
                ImageHelper.getInstance(context).loadListImage(viewHolder.searchItemBinding.itemImage, AppCommonMethod.getListLDSImage(itemList.get(position).getPosterURL(),context));
            }
            viewHolder.searchItemBinding.clRoot.setOnClickListener(view -> listener.onEnveuItemClicked(itemList.get(position)));
        }
    }


    private void setImage(String imageKey, String imageUrl, ImageView view) {
        try {

            String url1 = preference.getAppPrefCfep();
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String tranform = "/fit-in/70x40/filters:quality(40):max_bytes(100)/";
            StringBuilder stringBuilder = new StringBuilder(url1 + tranform + imageUrl + imageKey);
            Logger.e("", "" + stringBuilder);
            Glide.with(context)
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
        return jsonObject.getEnveuVideoItemBeans().size();
    }

    public interface CommonSearchListener {
        void onItemClicked(ItemsItem itemValue);

        default void onEnveuItemClick(EnveuVideoItemBean itemValue) {

        }
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CommonSearchAdapterBinding searchItemBinding;

        SingleItemRowHolder(CommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.searchItemBinding = binding;
        }

    }

}

