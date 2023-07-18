package com.tv.fragments.Arms;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.tv.baseModels.BaseBindingFragment;
import com.tv.databinding.FragmentArmsBinding;


public class ArmsFragment extends BaseBindingFragment<FragmentArmsBinding> {
    @Override
    protected FragmentArmsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentArmsBinding.inflate(inflater);
    }
}
