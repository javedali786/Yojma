package com.breadgangtvnetwork.fragments.All;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.breadgangtvnetwork.baseModels.BaseBindingFragment;
import com.breadgangtvnetwork.databinding.FragmentsAllBinding;

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
