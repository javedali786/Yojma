package com.tv.activities.profile.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tv.R;
import com.tv.callbacks.commonCallbacks.AvatarImageCallback;
import com.tv.databinding.AvatarItemBinding;
import com.tv.utils.config.bean.AvatarImages;

import java.util.List;
import java.util.Objects;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.SingleItemRowHolder> {

    private final Activity activity;
    //    String type;
    private final List<AvatarImages> arrayList;
    private final int count = 0;
    private String  selectedItemId = "";
    private final AvatarImageCallback imageCallback;

    public AvatarAdapter(Activity ctx, List<AvatarImages> list, AvatarImageCallback avatarImageCallback) {
        activity = ctx;
        this.arrayList = list;
        this.imageCallback = avatarImageCallback;
    }





    @NonNull
    @Override
    public AvatarAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        AvatarItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.avatar_item, viewGroup, false);

        return  new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(activity).load(arrayList.get(position).getUrl())
                .placeholder(R.drawable.placeholder_square)
                .error(R.drawable.placeholder_square)
                .into(viewHolder.genreItemBinding.itemImage);

        viewHolder.genreItemBinding.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemId = arrayList.get(position).getUrl();
                imageCallback.onClick(selectedItemId,arrayList.get(position).getIdentifier());
                notifyDataSetChanged();
            }
        });
        if(Objects.equals(arrayList.get(position).getUrl(), selectedItemId)){

        }
           // viewHolder.genreItemBinding.imageSelection.setBackgroundResource(R.drawable.selected_image);
        //else
           // viewHolder.genreItemBinding.imageSelection.setBackgroundResource(R.drawable.unselected_image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final AvatarItemBinding genreItemBinding;

        private SingleItemRowHolder(AvatarItemBinding binding) {
            super(binding.getRoot());
            this.genreItemBinding = binding;
        }

    }


}
