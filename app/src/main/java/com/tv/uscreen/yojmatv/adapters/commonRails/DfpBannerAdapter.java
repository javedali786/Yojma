package com.tv.uscreen.yojmatv.adapters.commonRails;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
;
import com.tv.uscreen.yojmatv.databinding.RowDfpBannerBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DfpBannerAdapter extends RecyclerView.Adapter<DfpBannerAdapter.ViewHolder> {

    private final RailCommonData item;
    private final String deviceId;
    private AdRequest adRequest;
    private final String adsType;

    public DfpBannerAdapter(Context context, RailCommonData item, String adsType) {
        this.item = item;
        this.adsType = adsType;
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = md5(android_id).toUpperCase();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RowDfpBannerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_dfp_banner, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.GONE);
        try {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (holder).rowDfpBannerBinding.adMobView.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            RelativeLayout adContainer = holder.rowDfpBannerBinding.adMobView;
            AdView adView = new AdView(holder.rowDfpBannerBinding.adMobView.getContext());
            fetchBannerSize(adsType, adView);
            adView.setLayoutParams(params);
            adContainer.addView(adView);

            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Logger.d( "AdsLoaded-->" + "success");
                    holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.VISIBLE);
                }


                @Override
                public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    // Code to be executed when an ad request fails.
                    holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.GONE);
                    Logger.d("onAdFailedToLoad" + loadAdError);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }



                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });


        } catch (Exception e) {


        }
    }


    public AdSize fetchBannerSize(String bannerType, AdView adView) {
        AdSize adSize;
        if (bannerType.equalsIgnoreCase(AppConstants.KEY_MREC)) {
            adSize = AdSize.MEDIUM_RECTANGLE;
            adView.setAdUnitId(item.getScreenWidget().getAdID());
            adView.setAdSize(adSize);
        } else if (bannerType.equalsIgnoreCase(AppConstants.KEY_BANNER)) {
            adSize = AdSize.BANNER;
            adView.setAdUnitId(item.getScreenWidget().getAdID());
            adView.setAdSize(adSize);
        } else adSize = AdSize.MEDIUM_RECTANGLE;
        return adSize;

    }


    private String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-512");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final RowDfpBannerBinding rowDfpBannerBinding;

        ViewHolder(RowDfpBannerBinding squareItemBind) {
            super(squareItemBind.getRoot());
            rowDfpBannerBinding = squareItemBind;

        }

    }


}

