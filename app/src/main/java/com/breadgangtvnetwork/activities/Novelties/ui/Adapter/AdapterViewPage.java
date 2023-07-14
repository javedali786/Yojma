package com.breadgangtvnetwork.activities.Novelties.ui.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.breadgangtvnetwork.fragments.All.ALLFragment;
import com.breadgangtvnetwork.fragments.Arms.ArmsFragment;

public class AdapterViewPage extends FragmentPagerAdapter {


    public AdapterViewPage(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ALLFragment();
            case 1:
                return new ArmsFragment();
            default:
                return  new ALLFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
