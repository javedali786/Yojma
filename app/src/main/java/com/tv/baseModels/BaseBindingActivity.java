package com.tv.baseModels;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.tv.utils.Logger;

public abstract class BaseBindingActivity<B extends ViewDataBinding> extends BaseActivity {

    private B mBinding;

    public abstract B inflateBindingLayout(@NonNull LayoutInflater inflater);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = setupBinding(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Logger.d("current activity: " + this);

    }


    public B getBinding() {
        return mBinding;
    }


    private B setupBinding(@NonNull LayoutInflater inflater) {
        return inflateBindingLayout(inflater);
    }




}
