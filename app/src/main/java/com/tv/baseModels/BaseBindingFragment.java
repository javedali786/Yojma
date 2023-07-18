package com.tv.baseModels;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.tv.utils.Logger;
import com.tv.utils.commonMethods.AppCommonMethod;


public abstract class BaseBindingFragment<B extends ViewDataBinding> extends BaseFragment {
    private B mBinding;


    protected abstract B inflateBindingLayout(@NonNull LayoutInflater inflater);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = setupBinding(inflater);
        Logger.d("current fragment: " + this);
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppCommonMethod.updateLanguage("en", requireContext());
    }


    public B getBinding() {
        return mBinding;
    }


    private B setupBinding(@NonNull LayoutInflater inflater) {
        return inflateBindingLayout(inflater);
    }
}
