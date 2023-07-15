package com.breadgangtvnetwork.activities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.breadgangtvnetwork.activities.profile.adapter.CountryListAdapter;
import com.breadgangtvnetwork.activities.profile.model.CountryListModel;
import com.breadgangtvnetwork.baseModels.BaseBindingActivity;
import com.breadgangtvnetwork.databinding.ActivityCountryListBinding;
import com.breadgangtvnetwork.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CountryListActivity extends BaseBindingActivity<ActivityCountryListBinding> implements CountryListAdapter.ItemSelected {
    private ArrayList<CountryListModel> countryList = new ArrayList<>();
    private CountryListAdapter countryListAdapter;


    @Override
    public ActivityCountryListBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityCountryListBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCountryList();

        countryListAdapter = new CountryListAdapter(countryList,CountryListActivity.this);
        getBinding().rvCountryList.setAdapter(countryListAdapter);

        callSearch();

        getBinding().cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });

    }

    private void callSearch() {
        getBinding().searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                countryListAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getCountryList() {
        try {
            Type listType = new TypeToken<ArrayList<CountryListModel>>() {}.getType();
            ArrayList<CountryListModel> users = new Gson().fromJson(loadJsonFromAsset(), listType);

            for (int i = 0; i < users.size(); i++) {
                CountryListModel countryListModel = new CountryListModel();
                countryListModel.setName(users.get(i).getName());
                countryListModel.setCode(users.get(i).getCode());
                countryList.add(countryListModel);
            }

        } catch (Exception e) {
            Logger.w("error","error");
        }
    }

    private String loadJsonFromAsset() {
        String json = null;

        try {
            InputStream is = getAssets().open("CountryList.json");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    public void getSelectedCountry(String countryName) {
        Intent intent = new Intent();
        intent.putExtra("countryName", countryName);
        setResult(RESULT_OK, intent);
        finish();
    }


}