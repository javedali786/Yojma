package com.tv.uscreen.yojmatv.activities.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.search.ui.ActivitySearch;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
;
import com.tv.uscreen.yojmatv.databinding.CommonSearchAdapterBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper;
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class RowSearchAdapter extends RecyclerView.Adapter<RowSearchAdapter.SingleItemRowHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final Context context;
    private final KsPreferenceKeys preference;
    private final List<EnveuVideoItemBean> list;
    private final RowSearchListener listener;
    private final int limitView = 5;
    private int totalCount = 0;
    private ActivitySearch itemListener;
    private String customContentType;
    private String videoType;

    public RowSearchAdapter(Context context, List<EnveuVideoItemBean> list, boolean isLimit, RowSearchListener listener, int totalCount, String customContentType, String videoType) {
        this.context = context;
        this.list = list;
        this.totalCount = totalCount;
        this.listener = listener;
        this.customContentType = customContentType;
        this.videoType = videoType;
        preference = KsPreferenceKeys.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        boolean isLoadingAdded = false;
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void notifyAdapter(List<EnveuVideoItemBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    public int allCount() {
        return list.size();
    }

    @NonNull
    @Override
    public RowSearchAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        CommonSearchAdapterBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.common_search_adapter, viewGroup, false);
        itemBinding.setColorsData(ColorsHelper.INSTANCE);
        //ThemeHandler.getInstance().applyRowSearch(viewGroup.getContext(), itemBinding);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RowSearchAdapter.SingleItemRowHolder viewHolder, final int position) {

//        viewHolder.itemBinding.tvTitle.setTextColor(context.getColor(R.color.series_detail_now_playing_title_color_search));
//        viewHolder.itemBinding.tvEpisode.setTextColor(context.getColor(R.color.signup_et_hint));

        viewHolder.itemBinding.tvTitle.setText(list.get(position).getTitle());
        viewHolder.itemBinding.clRoot.setOnClickListener(view -> listener.onRowItemClicked(list.get(position), position));

        if (list.get(position) != null) {
            viewHolder.itemBinding.setPlaylistItem(list.get(position));
        }
        try {
            if (list.get(position).getAssetType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (list.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries()) ||
                        list.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())
                        || list.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
                    if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                          ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                    }
                    viewHolder.itemBinding.tvEpisode.setText(list.get(position).getDescription());
                }

            } else {
                if (list.get(position).getAssetType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                    if (list.get(position).getCustomContent().getCustomType().equalsIgnoreCase(MediaTypeConstants.getInstance().getContest())) {
                        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                            ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                        }
                        viewHolder.itemBinding.tvEpisode.setText(list.get(position).getDescription());
                    } else if (list.get(position).getCustomContent().getCustomType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEvent())) {
                        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                            ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                        }
                        viewHolder.itemBinding.tvEpisode.setText(list.get(position).getDescription());
                    } else if (list.get(position).getCustomContent().getCustomType().equalsIgnoreCase(MediaTypeConstants.getInstance().getExpedition())) {
                        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                            ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                        }
                        viewHolder.itemBinding.tvEpisode.setText(list.get(position).getDescription());
                    } else if (list.get(position).getCustomContent().getCustomType().equalsIgnoreCase(MediaTypeConstants.getInstance().getOffer())) {
                        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                            ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                        }
                        viewHolder.itemBinding.tvEpisode.setText(list.get(position).getDescription());
                    } else if (list.get(position).getCustomContent().getCustomType().equalsIgnoreCase(MediaTypeConstants.getInstance().getMarketPlace())) {
                        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                            ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                        }
                        viewHolder.itemBinding.tvEpisode.setText(list.get(position).getDescription());
                    } else if (list.get(position).getCustomContent().getCustomType().equalsIgnoreCase(MediaTypeConstants.getInstance().getNews())) {
                        if (list.get(position).getPosterURL() != null && !list.get(position).getPosterURL().equalsIgnoreCase("")) {
                            ImageHelper.getInstance(context).loadListImage(viewHolder.itemBinding.itemImage, AppCommonMethod.getListLDSImage(list.get(position).getPosterURL(), context));
                        }
                        viewHolder.itemBinding.tvEpisode.setText(list.get(position).getDescription());
                    }
                }
            }


        } catch (Exception e) {
            Logger.w(e);
        }
    }

    private void setSeasonEpisodeValue(EnveuVideoItemBean enveuVideoItemBean, TextView tvEpisode) {
        Logger.e("Seasons: " + enveuVideoItemBean);
        if (enveuVideoItemBean != null) {
            if (enveuVideoItemBean.getSeasonCount() > 0 && enveuVideoItemBean.getVodCount() > 0) {
                tvEpisode.setText(context.getResources().getString(R.string.seasons) + " " + enveuVideoItemBean.getSeasonCount() + "  " + context.getResources().getString(R.string.episodes) + " " + enveuVideoItemBean.getVodCount() + "");
            } else {
                if (enveuVideoItemBean.getSeasonCount() == 0 && enveuVideoItemBean.getVodCount() == 0) {
                    tvEpisode.setText("");

                } else if (enveuVideoItemBean.getSeasonCount() == 0) {
                    if (enveuVideoItemBean.getVodCount() > 1) {
                        tvEpisode.setText(context.getResources().getString(R.string.episodes) + " " + enveuVideoItemBean.getVodCount() + "");
                    } else {
                        tvEpisode.setText(context.getResources().getString(R.string.episode) + " " + enveuVideoItemBean.getVodCount() + "");
                    }
                } else if (enveuVideoItemBean.getVodCount() == 0) {
                    tvEpisode.setText(enveuVideoItemBean.getSeasonCount() + "");
                    if (enveuVideoItemBean.getSeasonCount() > 1) {
                        tvEpisode.setText(context.getResources().getString(R.string.seasons) + " " + enveuVideoItemBean.getSeasonCount() + "");
                    } else {
                        tvEpisode.setText(context.getResources().getString(R.string.season) + " " + enveuVideoItemBean.getSeasonCount() + "");
                    }
                }
            }
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
        return list.size();
    }

    public interface RowSearchListener {
        void onRowItemClicked(EnveuVideoItemBean itemValue, int position);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CommonSearchAdapterBinding itemBinding;

        public SingleItemRowHolder(CommonSearchAdapterBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

}


