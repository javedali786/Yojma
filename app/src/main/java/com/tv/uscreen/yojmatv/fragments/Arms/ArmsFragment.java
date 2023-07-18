package com.tv.uscreen.yojmatv.fragments.Arms;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment;
import com.tv.uscreen.yojmatv.databinding.FragmentArmsBinding;
;

public class ArmsFragment extends BaseBindingFragment<FragmentArmsBinding> {
    @Override
    protected FragmentArmsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentArmsBinding.inflate(inflater);
    }
}
