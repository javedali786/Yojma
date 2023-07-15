package com.breadgangtvnetwork.utils.helpers.carousel.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.PagerAdapter;

import com.breadgangtvnetwork.BR;
import com.breadgangtvnetwork.R;
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData;
import com.breadgangtvnetwork.beanModelV3.playListModelV2.NewImages;
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonRailtItemClickListner;
import com.breadgangtvnetwork.utils.Logger;
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod;
import com.breadgangtvnetwork.utils.constants.AppConstants;
import com.breadgangtvnetwork.utils.helpers.ImageHelper;
import com.google.gson.Gson;

import java.util.List;


public class SliderAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private final RailCommonData items;
    private long mLastClickTime = 0;
    private  String Tag = "img5";
    private final CommonRailtItemClickListner listner;
    private final int viewType;
    private final List<EnveuVideoItemBean> videos;
    private final int pos;

    public SliderAdapter(@NonNull Context context, RailCommonData items, int viewType, CommonRailtItemClickListner listner,int position) {
        this.context = context;
        this.items = items;
       // notifyDataSetChanged();
        layoutInflater = LayoutInflater.from(context);
        this.listner = listner;
        this.viewType = viewType;
        this.videos = items.getEnveuVideoItemBeans();
        this.pos=position;
    }

    @Override
    public int getCount() {
        return items.getEnveuVideoItemBeans().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // The object returned by instantiateItem() is a key/identifier. This method checks whether
        // the View passed to it (representing the page) is associated with that key or not.
        // It is required by a PagerAdapter to function properly.
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        ViewDataBinding viewDataBinding;

        switch (viewType) {
            case AppConstants.CAROUSEL_LDS_LANDSCAPE:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousal_landscape_item, container, false);
                break;
            case AppConstants.CAROUSEL_PR_POTRAIT:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousal_potrait_item, container, false);
                TextView title = viewDataBinding.getRoot().findViewById(R.id.rail_sliderTitle_text);
              //  TextView titleAsset = viewDataBinding.getRoot().findViewById(R.id.image_title);
                ImageView titleImage = viewDataBinding.getRoot().findViewById(R.id.rail_sliderTitle_img);
                if (items.getEnveuVideoItemBeans().get(position).getDisplay_tags()!=null) {
                    String displayTags = items.getEnveuVideoItemBeans().get(position).getDisplay_tags();
                    displayTags = displayTags.replaceAll(","," \u25AA ");
                    title.setVisibility(View.VISIBLE);
                    title.setText(displayTags);
                } else {
                    title.setVisibility(View.GONE);
                }

                if (items.getEnveuVideoItemBeans().get(position).getPosterURL() != null) {
                    ImageHelper.getInstance(titleImage.getContext()).loadPortraitImage(titleImage, AppCommonMethod.getListPRImage((items.getEnveuVideoItemBeans().get(position).getPosterURL()), titleImage.getContext()));
                }
                break;
            case AppConstants.CAROUSEL_SQR_SQUARE:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousel_square_item, container, false);
                break;
            case AppConstants.CAROUSEL_CIR_CIRCLE:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousel_circle_item, container, false);
                break;
            case AppConstants.CAROUSEL_CST_CUSTOM:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_slider_live, container, false);
                break;
            case 18:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.home_slider_portrait_item, container, false);

                Button btTitle =viewDataBinding.getRoot().findViewById(R.id.btn_slider);
                    if (videos.get(position).getAssetType().equalsIgnoreCase(AppConstants.VIDEO)) {
                        if (videos.get(position).getVideoDetails().getVideoType() != null) {
                            if (videos.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(AppConstants.Movie)) {
                                btTitle.setText(R.string.watcho_now_title);
                            } else if (videos.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(AppConstants.SERIES)) {
                                btTitle.setText(R.string.view_series_title);
                            } else if (videos.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(AppConstants.Episode)) {
                                btTitle.setText(R.string.watcho_now_title);
                            } else if (videos.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(AppConstants.Shows)) {
                                btTitle.setText(R.string.watcho_now_title);
                            } else if (videos.get(position).getVideoDetails().getVideoType().equalsIgnoreCase(AppConstants.Trailer)) {
                                btTitle.setText(R.string.watch_trailer);
                            }
                        }
                    } else if (videos.get(position).getAssetType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                        if (videos.get(position).getCustomContent().getCustomType() != null) {
                            if (videos.get(position).getCustomContent().getCustomType().equalsIgnoreCase(AppConstants.News)) {
                                btTitle.setText(R.string.explore_more_title);
                            } else if (videos.get(position).getCustomContent().getCustomType().equalsIgnoreCase(AppConstants.contest)) {
                                btTitle.setText(R.string.explore_more_title);
                            } else if (videos.get(position).getCustomContent().getCustomType().equalsIgnoreCase(AppConstants.Event)) {
                                btTitle.setText(R.string.explore_more_title);
                            } else if (videos.get(position).getCustomContent().getCustomType().equalsIgnoreCase(AppConstants.OFFER)) {
                                btTitle.setText(R.string.see_offer_title);
                            } else if (videos.get(position).getCustomContent().getCustomType().equalsIgnoreCase(AppConstants.Expedition)) {
                                btTitle.setText(R.string.explore_more_title);
                            }
                        }
                    } else if (videos.get(position).getAssetType().equalsIgnoreCase(AppConstants.LIVE)) {
                        btTitle.setText(R.string.watcho_now_title);
                    }
                break;
            default:
                viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carousal_landscape_item, container, false);
                break;

        }
        //String buttonTitle = AppCommonMethod.getTitleAsPerMediaType();
        viewDataBinding.setVariable(BR.assetItem, videos.get(position));
        viewDataBinding.setVariable(BR.adapter, this);
        viewDataBinding.setVariable(BR.position, position);
        container.addView(viewDataBinding.getRoot());
        return viewDataBinding.getRoot();
    }

    private String getTitleImage(List<NewImages> images) {
        for (NewImages imageItem: images){
            if (imageItem.getTag().toString().equalsIgnoreCase(Tag))
                return imageItem.getSrc();
        }
       return  "";
    }

    public void itemClick(int position) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        listner.railItemClick(items, position);

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // Removes the page from the container for the given position. We simply removed object using removeView()
        // but could’ve also used removeViewAt() by passing it the position.
        try {
            // Remove the view from the container
            container.removeView((View) object);
            // Invalidate the object
          //  object = null;
        } catch (Exception e) {
            Logger.e("SliderAdapter", "" + e);
        }
    }
}