package com.breadgangtvnetwork.adapters.commonRails;

import static com.breadgangtvnetwork.OttApplication.getContext;
import static com.breadgangtvnetwork.utils.constants.AppConstants.ADS_BANNER;
import static com.breadgangtvnetwork.utils.constants.AppConstants.ADS_MREC;
import static com.breadgangtvnetwork.utils.constants.AppConstants.CAROUSEL_CIR_CIRCLE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.CAROUSEL_CST_CUSTOM;
import static com.breadgangtvnetwork.utils.constants.AppConstants.CAROUSEL_LDS_BANNER;
import static com.breadgangtvnetwork.utils.constants.AppConstants.CAROUSEL_LDS_LANDSCAPE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.CAROUSEL_PR_POSTER;
import static com.breadgangtvnetwork.utils.constants.AppConstants.CAROUSEL_PR_POTRAIT;
import static com.breadgangtvnetwork.utils.constants.AppConstants.CAROUSEL_SQR_SQUARE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.GRD_HORIZONTAL_CIR_CIRCLE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.GRD_HORIZONTAL_LDS_LANDSCAPE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.GRD_HORIZONTAL_PR_PORTRAIT;
import static com.breadgangtvnetwork.utils.constants.AppConstants.GRD_HORIZONTAL_PR_POSTER;
import static com.breadgangtvnetwork.utils.constants.AppConstants.GRD_HORIZONTAL_SQR_SQUARE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HERO_CIR_CIRCLE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HERO_CST_CUSTOM;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HERO_LDS_BANNER;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HERO_LDS_LANDSCAPE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HERO_PR_POSTER;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HERO_PR_POTRAIT;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HERO_SQR_SQUARE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HORIZONTAL_CIR_CIRCLE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HORIZONTAL_LDS_LANDSCAPE;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HORIZONTAL_PR_POSTER;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HORIZONTAL_PR_POTRAIT;
import static com.breadgangtvnetwork.utils.constants.AppConstants.HORIZONTAL_SQR_SQUARE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.breadgangtvnetwork.R;
import com.breadgangtvnetwork.SDKConfig;
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData;
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonRailtItemClickListner;
import com.breadgangtvnetwork.callbacks.commonCallbacks.MoreClickListner;
import com.breadgangtvnetwork.databinding.CircleRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.DfpBannerLayoutBinding;
import com.breadgangtvnetwork.databinding.GridCircleRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.GridPosterPotraitRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.GridRecylerItemBinding;
import com.breadgangtvnetwork.databinding.GridSquareRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.HeaderRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.HeadingRailsBinding;
import com.breadgangtvnetwork.databinding.HeroAdsLayoutBinding;
import com.breadgangtvnetwork.databinding.LandscapeRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.PosterPotraitRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.PotraitGridRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.PotraitRecyclerItemBinding;
import com.breadgangtvnetwork.databinding.SquareRecyclerItemBinding;
import com.breadgangtvnetwork.utils.CustomLayoutManager;
import com.breadgangtvnetwork.utils.Logger;
import com.breadgangtvnetwork.utils.ObjectHelper;
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper;
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod;
import com.breadgangtvnetwork.utils.constants.AppConstants;
import com.breadgangtvnetwork.utils.helpers.GravitySnapHelper;
import com.breadgangtvnetwork.utils.helpers.SpacingItemDecoration;
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.Properties;

import java.util.List;

public class CommonAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<RailCommonData> mList;
    private final CommonRailtItemClickListner listner;
    private final MoreClickListner moreClickListner;

    public CommonAdapterNew(List<RailCommonData> mList, CommonRailtItemClickListner listner, MoreClickListner moreClickListner) {
        this.mList = mList;
        this.listner = listner;
        this.moreClickListner = moreClickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getRailType();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Logger.e("position bind in", position + " ==>" + holder.getClass().getSimpleName());
        setFadeAnimation(holder.itemView);
        if (holder instanceof CarouselViewHolder) {
            carouselLandscape((CarouselViewHolder) holder, position);
        } else if (holder instanceof DfpBannerHolder) {
            dfpAdsLayout((DfpBannerHolder) holder, position);
        } else if (holder instanceof HeroAdsHolder) {
            heroAdsLayout((HeroAdsHolder) holder, position);
        } else if (holder instanceof CircleHolder) {
            circularRail((CircleHolder) holder, position);
        } else if (holder instanceof SquareHolder) {
            squareRail((SquareHolder) holder, position);
        } else if (holder instanceof PortraitHolder) {
            potraitRail((PortraitHolder) holder, position);
        } else if (holder instanceof GrdLandscapeHolder) {
            GrdRail((GrdLandscapeHolder) holder, position);
        } else if (holder instanceof GrdPortraitHolder) {
            GrdPortraitRail((GrdPortraitHolder) holder, position);
        } else if (holder instanceof GrdCircleHolder) {
            GrdCircleRail((GrdCircleHolder) holder, position);
        } else if(holder instanceof GrdSquareHolder) {
            GrdSquareRail((GrdSquareHolder) holder,position);
        } else if(holder instanceof GridPosterPotraitHolder) {
            GrdposterPotraitRail((GridPosterPotraitHolder) holder,position);
        }
        else if (holder instanceof LandscapeHolder) {
            LandscapeRail((LandscapeHolder) holder, position);
        }

        else {
            posterPotraitRail((PosterPotraitHolder) holder, position);
        }
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    private void posterPotraitRail(PosterPotraitHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.itemBinding.recyclerViewList2;
        CommonPosterPotrailRailAdapter adapter = new CommonPosterPotrailRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.itemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void LandscapeRail(LandscapeHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.landscapeRecyclerItemBinding.recyclerViewList3;
        CommonLandscapeRailAdapter adapter = new CommonLandscapeRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.landscapeRecyclerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void GrdposterPotraitRail(GridPosterPotraitHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.gridPosterPotraitRecyclerItemBinding.recyclerViewList2;
        GridCommonPosterPotrailRailAdapter adapter = new GridCommonPosterPotrailRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setGridCommonRailAdapter(viewHolder.gridPosterPotraitRecyclerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void GrdRail(GrdLandscapeHolder viewHolder, int position) {
        Log.d("grid", "setRailType: 4 ");
        RecyclerView recyclerView = viewHolder.gridRecylerItemBinding.recyclerViewList3;
        CommonGridRailAdapter adapter = new CommonGridRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setGridCommonRailAdapter(viewHolder.gridRecylerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void GrdPortraitRail(GrdPortraitHolder viewHolder, int position) {
        Log.d("grid", "setRailType: 4 ");
        RecyclerView recyclerView = viewHolder.potraitGridRecyclerItemBinding.recyclerViewList4;
        CommonGridPotraitRailAdapter adapter = new CommonGridPotraitRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setGridCommonRailAdapter(viewHolder.potraitGridRecyclerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void GrdCircleRail(GrdCircleHolder viewHolder, int position) {
        Log.d("grid", "setRailType: 4 ");
        RecyclerView recyclerView = viewHolder.gridCircleRecyclerItemBinding.recyclerViewList1;
        CommonGridCircleRailAdapter adapter = new CommonGridCircleRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setGridCommonRailAdapter(viewHolder.gridCircleRecyclerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void GrdSquareRail(GrdSquareHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.gridsquareRecyclerItemBinding.recyclerViewList2;
        GridCommonSquareRailAdapter gridcommonSquareRailAdapter = new GridCommonSquareRailAdapter(mList.get(position), listner, mList.get(position).getScreenWidget());
        setGridCommonRailAdapter(viewHolder.gridsquareRecyclerItemBinding.titleHeading, recyclerView, position, gridcommonSquareRailAdapter);
    }


    private void potraitRail(PortraitHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.potraitRecyclerItemBinding.recyclerViewList4;
        CommonPotraitRailAdapter adapter = new CommonPotraitRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.potraitRecyclerItemBinding.titleHeading, recyclerView, position, adapter);
    }

    private void squareRail(SquareHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.squareRecyclerItemBinding.recyclerViewList2;
        CommonSquareRailAdapter commonSquareRailAdapter = new CommonSquareRailAdapter(mList.get(position), listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.squareRecyclerItemBinding.titleHeading, recyclerView, position, commonSquareRailAdapter);
    }

    private void circularRail(CircleHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.circularRecyclerItemBinding.recyclerViewList1;
        CommonCircleRailAdapter commonCircleAdapter = new CommonCircleRailAdapter(mList.get(position), position, listner, mList.get(position).getScreenWidget());
        setCommonRailAdapter(viewHolder.circularRecyclerItemBinding.titleHeading, recyclerView, position, commonCircleAdapter);
    }


    private void setCommonRailAdapter(HeadingRailsBinding headingRailsBinding, RecyclerView recyclerView, int position, RecyclerView.Adapter adapter) {
        try {
            headingRailsBinding.setColorsData(ColorsHelper.INSTANCE);
            headingRailsBinding.setStringData(StringsHelper.INSTANCE);
            setTitle(headingRailsBinding, mList.get(position), position);
            if (mList.get(position).getEnveuVideoItemBeans().size() > 0) {
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setAdapter(null);
            }
        } catch (ClassCastException e) {
            Logger.e("CommonAdapter", "" + e);
        }
    }


    private void setGridCommonRailAdapter(HeadingRailsBinding headingRailsBinding, RecyclerView recyclerView, int position, RecyclerView.Adapter adapter) {
        Log.d("tag", "setGridCommonRailAdapter: " + mList.get(position).getScreenWidget().getColumns());
        Log.d("tag", "setGridCommonRailAdapter: " +  mList.get(position).getScreenWidget().getListingLayoutContentSize());

        try {
            headingRailsBinding.setColorsData(ColorsHelper.INSTANCE);
            setTitle(headingRailsBinding, mList.get(position), position);
            if (mList.get(position).getEnveuVideoItemBeans().size() > 0) {
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), mList.get(position).getScreenWidget().getColumns()));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setAdapter(null);
            }
        } catch (ClassCastException e) {
            Logger.e("CommonAdapter", "" + e);
        }
    }

    private void setRecylerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SpacingItemDecoration(8, SpacingItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recyclerView);
    }


    private void setGrdRecylerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SpacingItemDecoration(8, SpacingItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
        recyclerView.setHasFixedSize(true);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recyclerView);
    }


    private void carouselLandscape(CarouselViewHolder viewHolder, int position) {
        setTitle(viewHolder.itemBinding.titleHeading, mList.get(position), position);
        KsPreferenceKeys.getInstance().setAutoDuration(mList.get(position).getScreenWidget().getAutoRotateDuration() == null ? 0 : mList.get(position).getScreenWidget().getAutoRotateDuration());
        KsPreferenceKeys.getInstance().setAutoRotation(mList.get(position).getScreenWidget().getAutoRotate() == null || mList.get(position).getScreenWidget().getAutoRotate());
        viewHolder.itemBinding.slider.addSlides(mList.get(position), listner, position, mList.get(position).getRailType(), mList.get(position).getScreenWidget().getContentIndicator(), mList.get(position).getScreenWidget().getAutoRotate() == null || mList.get(position).getScreenWidget().getAutoRotate(), mList.get(position).getScreenWidget().getAutoRotateDuration() == null ? 0 : mList.get(position).getScreenWidget().getAutoRotateDuration(),"");
    }


    private void heroAdsLayout(HeroAdsHolder holder, int position) {
        CommonHeroRailAdapter adapter = new CommonHeroRailAdapter(mList.get(position), listner);
        holder.heroAdsHolder.rvHeroBanner.setNestedScrollingEnabled(false);
        holder.heroAdsHolder.rvHeroBanner.setHasFixedSize(true);
        holder.heroAdsHolder.rvHeroBanner.setLayoutManager(new LinearLayoutManager(holder.heroAdsHolder.rvHeroBanner.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.heroAdsHolder.rvHeroBanner.setAdapter(adapter);

    }


    private void dfpAdsLayout(DfpBannerHolder viewHolder, int position) {
        int viewType = (mList.get(position).getRailType()) % 10;
        try {
            DfpBannerAdapter adapter;
            if (viewType == 1)
                adapter = new DfpBannerAdapter(viewHolder.dfpBannerLayoutBinding.rvDfpBanner.getContext(), mList.get(position), AppConstants.KEY_BANNER);
            else
                adapter = new DfpBannerAdapter(viewHolder.dfpBannerLayoutBinding.rvDfpBanner.getContext(), mList.get(position), AppConstants.KEY_MREC);
            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setNestedScrollingEnabled(false);
            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setHasFixedSize(true);
            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setLayoutManager(new CustomLayoutManager(viewHolder.dfpBannerLayoutBinding.rvDfpBanner.getContext(), LinearLayoutManager.HORIZONTAL, false));
            SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
            snapHelperStart.attachToRecyclerView(viewHolder.dfpBannerLayoutBinding.rvDfpBanner);
            viewHolder.dfpBannerLayoutBinding.rvDfpBanner.setAdapter(adapter);
        } catch (ClassCastException ea) {
            Logger.e("CommonAdapter", "" + ea);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CAROUSEL_CIR_CIRCLE:
            case CAROUSEL_LDS_LANDSCAPE:
            case CAROUSEL_SQR_SQUARE:
            case CAROUSEL_CST_CUSTOM:
            case CAROUSEL_LDS_BANNER:
            case CAROUSEL_PR_POSTER:
            case CAROUSEL_PR_POTRAIT:
                HeaderRecyclerItemBinding headerRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.header_recycler_item, parent, false);
                return new CarouselViewHolder(headerRecyclerItemBinding, viewType);
            case HERO_CIR_CIRCLE:
            case HERO_CST_CUSTOM:
            case HERO_LDS_BANNER:
            case HERO_LDS_LANDSCAPE:
            case HERO_PR_POSTER:
            case HERO_PR_POTRAIT:
            case HERO_SQR_SQUARE:
                HeroAdsLayoutBinding heroAdsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.hero_ads_layout, parent, false);
                return new HeroAdsHolder(heroAdsLayoutBinding);
            case HORIZONTAL_LDS_LANDSCAPE:
                LandscapeRecyclerItemBinding landscapeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.landscape_recycler_item, parent, false);
                landscapeRecyclerItemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                LandscapeHolder landscapeHolder = new LandscapeHolder(landscapeRecyclerItemBinding);
                setRecylerProperties(landscapeHolder.landscapeRecyclerItemBinding.recyclerViewList3);
                return landscapeHolder;
            case GRD_HORIZONTAL_LDS_LANDSCAPE:
                Log.d("grid", "setRailType: 2 ");
                GridRecylerItemBinding landscapeGrdRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.grid_recyler_item, parent, false);
                landscapeGrdRecyclerItemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                GrdLandscapeHolder grdHolder = new GrdLandscapeHolder(landscapeGrdRecyclerItemBinding);
                setGrdRecylerProperties(grdHolder.gridRecylerItemBinding.recyclerViewList3);
                return grdHolder;

            case GRD_HORIZONTAL_PR_PORTRAIT:
                PotraitGridRecyclerItemBinding potraitGridRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_grid_recycler_item, parent, false);
                potraitGridRecyclerItemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                GrdPortraitHolder grdPortraitHolder = new GrdPortraitHolder(potraitGridRecyclerItemBinding);
                setGrdRecylerProperties(grdPortraitHolder.potraitGridRecyclerItemBinding.recyclerViewList4);
                return grdPortraitHolder;

            case GRD_HORIZONTAL_CIR_CIRCLE:
                GridCircleRecyclerItemBinding gridCircleRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.grid_circle_recycler_item, parent, false);
                gridCircleRecyclerItemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                GrdCircleHolder grdCircleHolder = new GrdCircleHolder(gridCircleRecyclerItemBinding);
                setGrdRecylerProperties(grdCircleHolder.gridCircleRecyclerItemBinding.recyclerViewList1);
                return grdCircleHolder;
            case GRD_HORIZONTAL_SQR_SQUARE:
                Log.d("grid", "GRD_HORIZONTAL_SQR_SQUARE");
                GridSquareRecyclerItemBinding gridSquareRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.grid_square_recycler_item, parent, false);
                gridSquareRecyclerItemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                GrdSquareHolder grdSquareHolder = new GrdSquareHolder(gridSquareRecyclerItemBinding);
                setGrdRecylerProperties(grdSquareHolder.gridsquareRecyclerItemBinding.recyclerViewList2);
                return grdSquareHolder;

            case GRD_HORIZONTAL_PR_POSTER:
                GridPosterPotraitRecyclerItemBinding gridPosterPotraitRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.grid_poster_potrait_recycler_item, parent, false);
                GridPosterPotraitHolder gridPosterPotraitHolder = new GridPosterPotraitHolder(gridPosterPotraitRecyclerItemBinding);
               // ThemeHandler.getInstance().applyheadingTextColorHoriPotrait(parent.getContext(),gridPosterPotraitRecyclerItemBinding);
                setGrdRecylerProperties(gridPosterPotraitHolder.gridPosterPotraitRecyclerItemBinding.recyclerViewList2);
                return gridPosterPotraitHolder;
            case HORIZONTAL_PR_POTRAIT:
                PotraitRecyclerItemBinding potraitRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_recycler_item, parent, false);
                PortraitHolder portraitHolder = new PortraitHolder(potraitRecyclerItemBinding);
                potraitRecyclerItemBinding.shimmer.setColorsData(ColorsHelper.INSTANCE);
                setRecylerProperties(portraitHolder.potraitRecyclerItemBinding.recyclerViewList4);
                return portraitHolder;

            case HORIZONTAL_PR_POSTER:
                PosterPotraitRecyclerItemBinding potraitRecyclerItemBinding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.poster_potrait_recycler_item, parent, false);
                potraitRecyclerItemBinding1.shimmer.setColorsData(ColorsHelper.INSTANCE);
                PosterPotraitHolder posterPotraitHolder = new PosterPotraitHolder(potraitRecyclerItemBinding1);
                setRecylerProperties(posterPotraitHolder.itemBinding.recyclerViewList2);
                return posterPotraitHolder;

            case HORIZONTAL_SQR_SQUARE:
                SquareRecyclerItemBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_recycler_item, parent, false);
                binding2.shimmer.setColorsData(ColorsHelper.INSTANCE);
                SquareHolder squareHolder = new SquareHolder(binding2);
                setRecylerProperties(squareHolder.squareRecyclerItemBinding.recyclerViewList2);
                return squareHolder;

            case HORIZONTAL_CIR_CIRCLE:
                CircleRecyclerItemBinding binding3 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.circle_recycler_item, parent, false);
                binding3.shimmer.setColorsData(ColorsHelper.INSTANCE);
                CircleHolder circleHolder = new CircleHolder(binding3);
                setRecylerProperties(circleHolder.circularRecyclerItemBinding.recyclerViewList1);
                return circleHolder;

            case ADS_BANNER:
            case ADS_MREC:
                DfpBannerLayoutBinding dfpBannerLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dfp_banner_layout, parent, false);
                return new DfpBannerHolder(dfpBannerLayoutBinding);
            default:
                DfpBannerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dfp_banner_layout, parent, false);
                return new DfpBannerHolder(binding);

        }
    }

    public void setTitle(HeadingRailsBinding headingRailsBinding, RailCommonData item, int position) {
        headingRailsBinding.setColorsData(ColorsHelper.INSTANCE);
//        int seeAllText;
//        if (!Objects.requireNonNull(ColorsHelper.INSTANCE.loadJsonFromAsset()).getData().getConfig().getRail_more_text_color().equals("")){
//            seeAllText = Color.parseColor(Objects.requireNonNull(ColorsHelper.INSTANCE.loadJsonFromAsset()).getData().getConfig().getRail_more_text_color());
//        }else {
//            seeAllText = getContext().getColor(R.color.rail_more_text_color);
//        }
//        headingRailsBinding.moreText.setTextColor(seeAllText);


        headingRailsBinding.shimmerTitleLayout.setVisibility(View.GONE);
        if (item.getScreenWidget().getShowHeader() != null && item.getScreenWidget().getShowHeader() && item.getEnveuVideoItemBeans().size() > 0) {
            headingRailsBinding.headerTitleLayout.setVisibility(View.VISIBLE);
            headingRailsBinding.mainHeaderTitle.setVisibility(View.VISIBLE);
            headingRailsBinding.headingTitle.bringToFront();

            //TODO COLOURS


            if (ObjectHelper.isSame(item.getScreenWidget().getReferenceName(), AppConstants.ContentType.MY_WATCHLIST.name())) {
                headingRailsBinding.headingTitle.setText(R.string.my_watchlist);
            } else if (item.isContinueWatching()) {
                setContinueWatchMultiLngTitle(item, headingRailsBinding);
            } else {
                setMultiLingTitle(item, headingRailsBinding);
            }
        } else {
            headingRailsBinding.headerTitleLayout.setVisibility(View.GONE);
            headingRailsBinding.mainHeaderTitle.setVisibility(View.GONE);
        }
        if (item.getScreenWidget().getContentShowMoreButton() != null && item.getScreenWidget().getContentShowMoreButton() && item.getEnveuVideoItemBeans().size() > 0) {
            headingRailsBinding.headerTitleLayout.setVisibility(View.VISIBLE);
            headingRailsBinding.mainHeaderTitle.setVisibility(View.VISIBLE);
            headingRailsBinding.headingTitle.bringToFront();
            headingRailsBinding.moreText.setVisibility(View.VISIBLE);
            headingRailsBinding.moreText.setOnClickListener(v -> {
                try {
                    if (item.getScreenWidget().getEnableMultilingualTitle() != null && item.getScreenWidget().getEnableMultilingualTitle() instanceof Boolean) {
                        boolean isMultilingualTitleEnable = (Boolean) item.getScreenWidget().getEnableMultilingualTitle();
                        String railName = "";
                        if (item.getScreenWidget().getName()!=null) {
                            railName  = (String) item.getScreenWidget().getName();
                        }
                        AppCommonMethod.MoEngageSeeAllEventTrack(getContext(),"HomeSliderActivity",item.getScreenWidget().getContentID(),railName,AppConstants.GALLERY_SELECT);
                        if (isMultilingualTitleEnable) {
                            String currentLang = KsPreferenceKeys.getInstance().getAppLanguage();
                            JsonObject jsonObject = new Gson().toJsonTree(item.getScreenWidget().getMultilingualTitle()).getAsJsonObject();

                            String name = AppCommonMethod.getMultilingualTitle(currentLang, jsonObject, SDKConfig.getInstance().getSpanishLangCode(), SDKConfig.getInstance().getEnglishCode());
                            if (!name.equalsIgnoreCase("")) {
                                moreClickListner.moreRailClick(item, position, name);
                            } else {
                                moreClickListner.moreRailClick(item, position, "");
                            }
                        } else {
                            moreClickListner.moreRailClick(item, position, "");
                        }
                    } else {
                        moreClickListner.moreRailClick(item, position, "");
                    }

                } catch (Exception e) {
                    moreClickListner.moreRailClick(item, position, "");
                }
            });
        } else {
            headingRailsBinding.headingTitle.setVisibility(View.VISIBLE);
            headingRailsBinding.moreText.setVisibility(View.INVISIBLE);
        }
    }

    private void setMultiLingTitle(RailCommonData item, HeadingRailsBinding headingRailsBinding) {
        try {
            headingRailsBinding.setColorsData(ColorsHelper.INSTANCE);
            String tittle = "";
            if (item.getScreenWidget().getEnableMultilingualTitle() != null && item.getScreenWidget().getEnableMultilingualTitle() instanceof Boolean) {
                boolean isMultilingualTitleEnable = (Boolean) item.getScreenWidget().getEnableMultilingualTitle();
                if (isMultilingualTitleEnable) {
                    String currentLang = KsPreferenceKeys.getInstance().getAppLanguage();
                    JsonObject jsonObject = new Gson().toJsonTree(item.getScreenWidget().getMultilingualTitle()).getAsJsonObject();
                    String name = AppCommonMethod.getMultilingualTitle(currentLang, jsonObject, SDKConfig.getInstance().getSpanishLangCode(), SDKConfig.getInstance().getEnglishCode());
                    Properties properties = new Properties();
                    properties.addAttribute(AppConstants.SEE_ALL_RAIL_TITTLE,name);
                    MoEHelper.getInstance(getApplicationContext()).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties);

                    if (!name.equalsIgnoreCase("")) {
                        headingRailsBinding.headingTitle.setText(name);
                    }
                } else {
                    tittle = (String) item.getScreenWidget().getName();
                    headingRailsBinding.headingTitle.setText(tittle);
                }
            } else {
                headingRailsBinding.headingTitle.setText((String) item.getScreenWidget().getName());
            }
        } catch (Exception ignored) {
            headingRailsBinding.headingTitle.setText((String) item.getScreenWidget().getName());
        }
    }

    private void setContinueWatchMultiLngTitle(RailCommonData item, HeadingRailsBinding headingRailsBinding) {
        try {
            headingRailsBinding.setColorsData(ColorsHelper.INSTANCE);
            if (item.getScreenWidget().getEnableMultilingualTitle() != null && item.getScreenWidget().getEnableMultilingualTitle() instanceof Boolean) {
                boolean isMultilingualTitleEnable = (Boolean) item.getScreenWidget().getEnableMultilingualTitle();
                if (isMultilingualTitleEnable) {
                    String currentLang = KsPreferenceKeys.getInstance().getAppLanguage();
                    JsonObject jsonObject = new Gson().toJsonTree(item.getScreenWidget().getMultilingualTitle()).getAsJsonObject();
                    String name = AppCommonMethod.getMultilingualTitle(currentLang, jsonObject, SDKConfig.getInstance().getSpanishLangCode(), SDKConfig.getInstance().getEnglishCode());
                    if (!name.equalsIgnoreCase("")) {
                        headingRailsBinding.headingTitle.setText(name);
                    }
                } else {
                    if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                        headingRailsBinding.headingTitle.setText("ดูต่อสำหรับ" + " " + headingRailsBinding.headingTitle.getContext().getResources().getString(R.string.For) + " " + KsPreferenceKeys.getInstance().getAppPrefUserName());
                    } else {
                        headingRailsBinding.headingTitle.setText(item.getScreenWidget().getName()+"");
                    }
                }
            } else {
                if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                    headingRailsBinding.headingTitle.setText("ดูต่อสำหรับ" + " " + headingRailsBinding.headingTitle.getContext().getResources().getString(R.string.For) + " " + KsPreferenceKeys.getInstance().getAppPrefUserName());
                } else {
                    headingRailsBinding.headingTitle.setText(
                            item.getScreenWidget().getName()+"");
                }
            }
        } catch (Exception ignored) {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                headingRailsBinding.headingTitle.setText("ดูต่อสำหรับ" + " " + headingRailsBinding.headingTitle.getContext().getResources().getString(R.string.For) + " " + KsPreferenceKeys.getInstance().getAppPrefUserName());
            } else {
                headingRailsBinding.headingTitle.setText(item.getScreenWidget().getName()+ "");
            }
        }

    }

    public class HeroAdsHolder extends RecyclerView.ViewHolder {
        HeroAdsLayoutBinding heroAdsHolder;

        HeroAdsHolder(@NonNull HeroAdsLayoutBinding itemHolder) {
            super(itemHolder.getRoot());
            this.heroAdsHolder = itemHolder;
        }
    }

    public class DfpBannerHolder extends RecyclerView.ViewHolder {
        DfpBannerLayoutBinding dfpBannerLayoutBinding;

        DfpBannerHolder(@NonNull DfpBannerLayoutBinding itemView) {
            super(itemView.getRoot());
            dfpBannerLayoutBinding = itemView;
        }
    }

    public class CarouselViewHolder extends RecyclerView.ViewHolder {
        HeaderRecyclerItemBinding itemBinding;

        CarouselViewHolder(HeaderRecyclerItemBinding flightItemLayoutBinding, int viewType) {
            super(flightItemLayoutBinding.getRoot());
            itemBinding = flightItemLayoutBinding;
            LinearLayout.LayoutParams layoutParams = null;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) itemBinding.slider.getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            boolean isTablet = itemBinding.constraintLayout.getResources().getBoolean(
                    R.bool.isTablet);

            switch (viewType) {
                case CAROUSEL_LDS_LANDSCAPE: {
                    int height = (int) (width / 1.80);
                    if (isTablet) {
                        if (itemBinding.slider.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            height = (int) (height / 1.70);
                        } else {
                            height = height + AppCommonMethod.convertDpToPixel(10);
                        }
                    }

                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + (itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_landscape_indicator_padding)));
                }
                break;
                case CAROUSEL_PR_POTRAIT: {
                    int height = (int) (width * 1.77);
                    if (isTablet) {
                        if (itemBinding.slider.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            height = width + AppCommonMethod.convertDpToPixel(10);
                        } else {
                            height = (int) (width / 2.4);
                        }
                    }
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_potrait_indicator_padding));
                    break;
                }
                case CAROUSEL_SQR_SQUARE: {
                    int height = width;
                    if (isTablet)
                        height = height / 2;
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
                    layoutParams.height = (int) (height + itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_square_indicator_padding));
                    break;
                }
                case CAROUSEL_CIR_CIRCLE: {
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) itemBinding.constraintLayout.getContext().getResources().getDimension(R.dimen.carousal_square_height));
                    break;
                }

            }
            if (layoutParams != null)
                itemBinding.constraintLayout.setLayoutParams(layoutParams);

        }

    }

    public class PortraitHolder extends RecyclerView.ViewHolder {
        final PotraitRecyclerItemBinding potraitRecyclerItemBinding;

        PortraitHolder(PotraitRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;
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

    public class SquareHolder extends RecyclerView.ViewHolder {
        final SquareRecyclerItemBinding squareRecyclerItemBinding;

        SquareHolder(SquareRecyclerItemBinding flightItemLayoutBinding) {
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

    public class GrdLandscapeHolder extends RecyclerView.ViewHolder {
        final GridRecylerItemBinding gridRecylerItemBinding;

        GrdLandscapeHolder(GridRecylerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            gridRecylerItemBinding = flightItemLayoutBinding;
        }
    }

    public class GrdPortraitHolder extends RecyclerView.ViewHolder {
        final PotraitGridRecyclerItemBinding potraitGridRecyclerItemBinding;

        GrdPortraitHolder(PotraitGridRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitGridRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    public class GrdCircleHolder extends RecyclerView.ViewHolder {
        final GridCircleRecyclerItemBinding gridCircleRecyclerItemBinding;

        GrdCircleHolder(GridCircleRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            gridCircleRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    public class GrdSquareHolder extends RecyclerView.ViewHolder {
        final GridSquareRecyclerItemBinding gridsquareRecyclerItemBinding;

        GrdSquareHolder(GridSquareRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            gridsquareRecyclerItemBinding = flightItemLayoutBinding;
        }
    }

    public class GridPosterPotraitHolder extends RecyclerView.ViewHolder {
        GridPosterPotraitRecyclerItemBinding gridPosterPotraitRecyclerItemBinding;

        GridPosterPotraitHolder(GridPosterPotraitRecyclerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.gridPosterPotraitRecyclerItemBinding = itemBinding;
        }
    }

}
