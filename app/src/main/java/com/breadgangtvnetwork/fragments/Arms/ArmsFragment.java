package com.breadgangtvnetwork.fragments.Arms;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.breadgangtvnetwork.baseModels.BaseBindingFragment;
import com.breadgangtvnetwork.databinding.FragmentArmsBinding;


public class ArmsFragment extends BaseBindingFragment<FragmentArmsBinding> {
    @Override
    protected FragmentArmsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentArmsBinding.inflate(inflater);
    }
}
