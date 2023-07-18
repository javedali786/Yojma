package com.tv.uscreen.yojmatv.adapters.shimmer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.listing.callback.ItemClickListener;
import com.tv.uscreen.yojmatv.adapters.HeaderAdapter;
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonAdapter;
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonCircleAdapter;
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonLandscapeAdapter;
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonPotraitAdapter;
import com.tv.uscreen.yojmatv.adapters.commonRails.SquareCommonAdapter;
import com.tv.uscreen.yojmatv.beanModel.beanModel.SectionDataModel;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors;
import com.tv.uscreen.yojmatv.utils.helpers.carousel.model.Slide;
import com.tv.uscreen.yojmatv.utils.helpers.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

public class ShimmerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final RecyclerView.RecycledViewPool viewPool;
    private final ArrayList<SectionDataModel> dataList;
    private final int ITEM4 = 4;
    View view;


    public ShimmerAdapter(Activity activity, ArrayList<SectionDataModel> demoList, ArrayList<Slide> slides) {
        this.activity = activity;
        this.dataList = demoList;
        viewPool = new RecyclerView.RecycledViewPool();
    }
    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int HEADER_ITEM = 0;
        int ITEM3 = 3;
        int ITEM2 = 2;
        int ITEM1 = 1;
        if (viewType == HEADER_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shimmer_crousel, parent, false);
            ConstraintLayout constraintLayout = view.findViewById(R.id.constraintL);
            constraintLayout.setBackgroundColor(AppColors.INSTANCE.appBgColor());
            return new HeaderHolder(view);
        } else if (viewType == ITEM1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_recycler_item, parent, false);
            return new TrendingHolder(view);
        } else if (viewType == ITEM2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.landscape_recycler_item, parent, false);
            return new SqureHolder(view);
        } else if (viewType == ITEM3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.landscape_recycler_item, parent, false);
            return new LandscapeHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.potrait_recycler_item, parent, false);
            return new PortrateHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Logger.w("position bind insh", position + "");
        if (holder instanceof CommonAdapter.HeaderHolder) {
            try {
                ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
                HeaderAdapter itemListDataAdapter1 = new HeaderAdapter(activity, singleSectionItems);
                ((ShimmerAdapter.HeaderHolder) holder).recycler_view_list1.setNestedScrollingEnabled(false);
                ((ShimmerAdapter.HeaderHolder) holder).recycler_view_list1.setHasFixedSize(true);

                ((ShimmerAdapter.HeaderHolder) holder).recycler_view_list1.showShimmerAdapter();
                ((ShimmerAdapter.HeaderHolder) holder).recycler_view_list1.setDemoChildCount(1);


                ((ShimmerAdapter.HeaderHolder) holder).recycler_view_list1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                ((ShimmerAdapter.HeaderHolder) holder).recycler_view_list1.setAdapter(itemListDataAdapter1);

            } catch (Exception e) {
                Logger.w(e);
            }

        } else if (holder instanceof CommonAdapter.CircleHolder) {
            try {

                ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
                CommonCircleAdapter itemListDataAdapter1 = new CommonCircleAdapter(activity, singleSectionItems, "", new ArrayList<>(), new ItemClickListener() {
                    @Override
                    public void onRowItemClicked(EnveuVideoItemBean itemValue, int position) {

                    }
                }, null);
                ((TrendingHolder) holder).recycler_view_list1.setNestedScrollingEnabled(false);
                ((TrendingHolder) holder).recycler_view_list1.setHasFixedSize(true);

                ((TrendingHolder) holder).recycler_view_list1.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_HORIZONTAL);
                ((TrendingHolder) holder).recycler_view_list1.showShimmerAdapter();


                /*  *//*  ((TrendingHolder) holder).recycler_view_list1.addItemDecoration(new SpacingItemDecoration(10, SpacingItemDecoration.HORIZONTAL));*//*

                ((TrendingHolder) holder).recycler_view_list1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
                snapHelperStart.attachToRecyclerView( ((TrendingHolder) holder).recycler_view_list1);*/
                ((TrendingHolder) holder).recycler_view_list1.setAdapter(itemListDataAdapter1);

            } catch (ClassCastException e) {
                Logger.e("ShimmerAdapter", "" + e);
            }

        } else if (holder instanceof CommonAdapter.SqureHolder) {
            try {

                ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
                CommonLandscapeAdapter itemListDataAdapter1 = new CommonLandscapeAdapter(activity, singleSectionItems, new ArrayList<>(), "", new ArrayList<>());
                ((ShimmerAdapter.SqureHolder) holder).recycler_view_list1.setNestedScrollingEnabled(false);
                ((ShimmerAdapter.SqureHolder) holder).recycler_view_list1.setHasFixedSize(true);
                ((ShimmerAdapter.SqureHolder) holder).recycler_view_list1.showShimmerAdapter();
                /*
                 *//*  ((ShimmerAdapter.SqureHolder) holder).recycler_view_list1.addItemDecoration(new SpacingItemDecoration(10, SpacingItemDecoration.HORIZONTAL));*//*
                ((ShimmerAdapter.SqureHolder) holder).recycler_view_list1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
                snapHelperStart.attachToRecyclerView( ((ShimmerAdapter.SqureHolder) holder).recycler_view_list1);*/
                ((ShimmerAdapter.SqureHolder) holder).recycler_view_list1.setAdapter(itemListDataAdapter1);

            } catch (ClassCastException e) {
                Logger.e("ShimmerAdapter", "" + e);
            }
        } else if (holder instanceof CommonAdapter.LandscapeHolder) {
            try {
                ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
                SquareCommonAdapter itemListDataAdapter1 = new SquareCommonAdapter(activity, singleSectionItems, "", new ArrayList<>());
                ((ShimmerAdapter.LandscapeHolder) holder).recycler_view_list1.setNestedScrollingEnabled(false);
                ((ShimmerAdapter.LandscapeHolder) holder).recycler_view_list1.setHasFixedSize(true);
                ((ShimmerAdapter.LandscapeHolder) holder).recycler_view_list1.showShimmerAdapter();
                /*  *//*  ((ShimmerAdapter.LandscapeHolder) holder).recycler_view_list1.addItemDecoration(new SpacingItemDecoration(10, SpacingItemDecoration.HORIZONTAL));*//*
                ((ShimmerAdapter.LandscapeHolder) holder).recycler_view_list1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
                snapHelperStart.attachToRecyclerView( ((ShimmerAdapter.LandscapeHolder) holder).recycler_view_list1);*/

                ((ShimmerAdapter.LandscapeHolder) holder).recycler_view_list1.setAdapter(itemListDataAdapter1);

            } catch (ClassCastException e) {
                Logger.e("ShimmerAdapter", "" + e);
            }
        } else if (holder instanceof CommonAdapter.PortrateHolder) {
            try {
                ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
                CommonPotraitAdapter itemListDataAdapter1 = new CommonPotraitAdapter(activity, singleSectionItems, "", new ArrayList<>(), 0, new ItemClickListener() {
                    @Override
                    public void onRowItemClicked(EnveuVideoItemBean itemValue, int position) {

                    }
                },null,null);
                ((ShimmerAdapter.PortrateHolder) holder).recycler_view_list1.setNestedScrollingEnabled(false);
                ((ShimmerAdapter.PortrateHolder) holder).recycler_view_list1.setHasFixedSize(true);
                ((ShimmerAdapter.PortrateHolder) holder).recycler_view_list1.showShimmerAdapter();
                /*                *//**//*((ShimmerAdapter.PortrateHolder) holder).recycler_view_list1.addItemDecoration(new SpacingItemDecoration(10, SpacingItemDecoration.HORIZONTAL));*//**//*
                ((ShimmerAdapter.PortrateHolder) holder).recycler_view_list1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);*//*
                snapHelperStart.attachToRecyclerView( ((ShimmerAdapter.PortrateHolder) holder).recycler_view_list1);*/

                ((ShimmerAdapter.PortrateHolder) holder).recycler_view_list1.setAdapter(itemListDataAdapter1);


                Toast.makeText(activity, "potrait", Toast.LENGTH_SHORT).show();
                boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
                if (tabletSize) {
                    //landscape
                    ((PortrateHolder) holder).recycler_view_list1.getLayoutParams().height = 120;

                }
            } catch (ClassCastException e) {
                Logger.e("ShimmerAdapter", "" + e);
            }
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class HeaderHolder extends RecyclerView.ViewHolder {

        final ShimmerRecyclerView recycler_view_list1;

        public HeaderHolder(View itemView) {
            super(itemView);
            recycler_view_list1 = itemView.findViewById(R.id.recycler_view_list1);

        }
    }

    public class TrendingHolder extends RecyclerView.ViewHolder {
        final ShimmerRecyclerView recycler_view_list1;

        public TrendingHolder(View itemView) {
            super(itemView);
            recycler_view_list1 = view.findViewById(R.id.recycler_view_list1);
        }
    }

    public class SqureHolder extends RecyclerView.ViewHolder {
        final ShimmerRecyclerView recycler_view_list1;

        public SqureHolder(View itemView) {
            super(itemView);
            recycler_view_list1 = view.findViewById(R.id.recycler_view_list2);
        }
    }

    public class LandscapeHolder extends RecyclerView.ViewHolder {
        final ShimmerRecyclerView recycler_view_list1;

        public LandscapeHolder(View itemView) {
            super(itemView);
            recycler_view_list1 = view.findViewById(R.id.recycler_view_list3);
        }
    }

    public class PortrateHolder extends RecyclerView.ViewHolder {
        final ShimmerRecyclerView recycler_view_list1;

        public PortrateHolder(View itemView) {
            super(itemView);
            recycler_view_list1 = view.findViewById(R.id.recycler_view_list4);
        }
    }


}
