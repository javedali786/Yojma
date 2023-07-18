package com.tv.uscreen.yojmatv.activities.videoquality.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.videoquality.bean.LanguageItem;
import com.tv.uscreen.yojmatv.activities.videoquality.callBack.ItemClick;
;
import com.tv.uscreen.yojmatv.databinding.VideoQualityItemBinding;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper;

import java.util.List;


public class ChangeLanguageAdapter extends RecyclerView.Adapter<ChangeLanguageAdapter.SingleItemRowHolder> {

    private final List<LanguageItem> inboxMessages;
    private final ItemClick itemClickListener;
    int pos;
    private final KsPreferenceKeys preference;
    public ChangeLanguageAdapter(Activity activity, List<LanguageItem> itemsList, ItemClick listener) {
        preference = KsPreferenceKeys.getInstance();
        pos=preference.getAppPrefLanguagePos();
        this.inboxMessages = itemsList;
        this.itemClickListener = listener;

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        VideoQualityItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.video_quality_item, viewGroup, false);
     //   ThemeHandler.getInstance().applyChangeLangeAdapter(viewGroup.getContext(),itemBinding);
        itemBinding.setColorsData(ColorsHelper.INSTANCE);
        itemBinding.setStringData(StringsHelper.INSTANCE);
        return new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        if (pos == position) {
            viewHolder.notificationItemBinding.parentLayout.setBackgroundResource(R.drawable.setting_shadow);
        } else {
            viewHolder.notificationItemBinding.parentLayout.setBackgroundResource(0);
        }

        viewHolder.notificationItemBinding.titleText.setText(inboxMessages.get(position).getLanguageName());
       // viewHolder.notificationItemBinding.secondTitleText.setText(inboxMessages.get(position).getDefaultLangageName());

        viewHolder.notificationItemBinding.parentLayout.setOnClickListener(view -> {
            pos = position;
            itemClickListener.itemClicked(inboxMessages.get(position).getLanguageName(),pos);

        });
    }


    @Override
    public int getItemCount() {
        return inboxMessages.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final VideoQualityItemBinding notificationItemBinding;

        SingleItemRowHolder(VideoQualityItemBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;

        }

    }


}
