package com.tv.activities.downloads;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tv.R;
import com.tv.databinding.SelectDownloadVideoQualityBinding;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;


public class SelectDownloadQualityAdapter extends RecyclerView.Adapter<SelectDownloadQualityAdapter.SingleItemRowHolder> {

    private final List<String> downloadQualities;
    private final VideoQualitySelectedListener videoQualitySelectedListener;
    int pos = -1;

    public SelectDownloadQualityAdapter(Activity activity, List<String> downloadQualities, VideoQualitySelectedListener videoQualitySelectedListener) {
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        pos = preference.getAppPrefLanguagePos();
        this.downloadQualities = downloadQualities;
        this.videoQualitySelectedListener = videoQualitySelectedListener;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SelectDownloadVideoQualityBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.select_download_video_quality, viewGroup, false);
        return new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        if (pos != -1 && pos == position) {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.INVISIBLE);
        }
        viewHolder.notificationItemBinding.titleText.setText(downloadQualities.get(position));
        viewHolder.notificationItemBinding.parentLayout.setOnClickListener(view -> {
            pos = position;
            videoQualitySelectedListener.videoQualitySelected(position);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return downloadQualities.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final SelectDownloadVideoQualityBinding notificationItemBinding;

        SingleItemRowHolder(SelectDownloadVideoQualityBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;
        }
    }
}
