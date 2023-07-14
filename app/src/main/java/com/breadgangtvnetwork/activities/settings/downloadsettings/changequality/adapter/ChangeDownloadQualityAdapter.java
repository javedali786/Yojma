package com.breadgangtvnetwork.activities.settings.downloadsettings.changequality.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.breadgangtvnetwork.R;
import com.breadgangtvnetwork.databinding.ChangeLanguageItemBinding;
import com.breadgangtvnetwork.utils.constants.SharedPrefesConstants;
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;


public class ChangeDownloadQualityAdapter extends RecyclerView.Adapter<ChangeDownloadQualityAdapter.SingleItemRowHolder> {

    private final List<String> downloadQualities;
    int pos = 0;

    public ChangeDownloadQualityAdapter(Activity activity, List<String> downloadQualities) {
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        pos = preference.getAppPrefLanguagePos();
        this.downloadQualities = downloadQualities;
        pos = KsPreferenceKeys.getInstance().getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 4);
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ChangeLanguageItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),

                R.layout.change_language_item, viewGroup, false);
     //   ThemeHandler.getInstance().applychangedownloadsettingsItem(viewGroup.getContext(),itemBinding);

        return new SingleItemRowHolder(itemBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        if (pos == position) {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.INVISIBLE);
        }
        viewHolder.notificationItemBinding.titleText.setText(downloadQualities.get(position));

        viewHolder.notificationItemBinding.parentLayout.setOnClickListener(view -> {
            pos = position;
            KsPreferenceKeys.getInstance().setInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, position);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return downloadQualities.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final ChangeLanguageItemBinding notificationItemBinding;

        SingleItemRowHolder(ChangeLanguageItemBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;
        }
    }
}
