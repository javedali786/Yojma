package com.tv.uscreen.yojmatv.fragments.All;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment;
import com.tv.uscreen.yojmatv.databinding.FragmentsAllBinding;
;


public class ALLFragment extends BaseBindingFragment<FragmentsAllBinding> {
    @Override
    protected FragmentsAllBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentsAllBinding.inflate(inflater);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
