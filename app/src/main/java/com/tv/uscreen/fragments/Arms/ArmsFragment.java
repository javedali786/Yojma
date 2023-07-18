package com.tv.uscreen.fragments.Arms;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.tv.uscreen.baseModels.BaseBindingFragment;
import com.tv.uscreen.databinding.FragmentArmsBinding;

public class ArmsFragment extends BaseBindingFragment<FragmentArmsBinding> {
    @Override
    protected FragmentArmsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentArmsBinding.inflate(inflater);
    }
}
