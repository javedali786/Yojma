package com.tv.uscreen.yojmatv.utils.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tv.uscreen.yojmatv.OttApplication;
import com.tv.uscreen.yojmatv.utils.colorsJson.model.ColorsModel;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.security.CryptUtil;
import com.tv.uscreen.yojmatv.utils.stringsJson.model.StringsData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefHelper {
    private static SharedPrefHelper instance;
    private static final String PREF_FILE = "Session";
    private static final String COLOR_JSON = "ColorJson";
    private static final String STRING_JSON = "StringJson";
    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.Editor mEditor;
    private CryptUtil cryptUtil;

    @SuppressLint("CommitPrefEdits")
    protected SharedPrefHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        cryptUtil = CryptUtil.getInstance();
    }

    public static SharedPrefHelper getInstance() {
        if (instance == null) {
            instance = new SharedPrefHelper(OttApplication.getInstance());
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    public void clear() {
        mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();
    }

   /* public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }*/

    public String getString(String key, String defValue) {
        String decryptedValue = cryptUtil.decrypt(mSharedPreferences.getString(key, defValue), AppConstants.MY_VIPA_ENCRYPTION_KEY);
        if (decryptedValue == null || decryptedValue.equalsIgnoreCase("")||key.equalsIgnoreCase("DMS_Response")) {
            decryptedValue = mSharedPreferences.getString(key, defValue);
        }
        return decryptedValue;
    }

    public void setString(String key, String value) {
        String encryptedValue;
        encryptedValue = cryptUtil.encrypt(value, AppConstants.MY_VIPA_ENCRYPTION_KEY);
        if (key.equalsIgnoreCase("DMS_Response")||value.equalsIgnoreCase("")) {
            mEditor.putString(key, value);
        } else {
            mEditor.putString(key, encryptedValue);
        }
        mEditor.commit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    protected boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    protected void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }
    public void saveDataGenre(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("genre", json);
        editor.apply();     // This line is IMPORTANT !!!

    }

    public List<String> getDataGenreList(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("genre", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveSpeciesList(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("speciesList", json);
        editor.apply();     // This line is IMPORTANT !!!

    }

    public List<String> getSpeciesList(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("speciesList", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveTypeList(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("typesList", json);
        editor.apply();     // This line is IMPORTANT !!!
    }


    public List<String> getTypeList(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("typesList", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void saveDataGenreKeyValue(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("genreKey", json);
        editor.apply();     // This line is IMPORTANT !!!

    }

    public List<String> getDataGenreListKeyValue(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("genreKey", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveDataFeature(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("feature", json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<String> getDataFeatureList() {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("feature", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveDataFeatureKeyValue(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("featureKey", json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<String> getDataFeatureListKeyValue() {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("featureKey", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveDataSort(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("sort", json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<String> getDataSortList(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("sort", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }


    public void saveDataSortKeyValue(List<String> data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("sortKey", json);
        editor.apply();     // This line is IMPORTANT !!!

    }

    public List<String> getDataSortListKeyValue(){
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("sortKey", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public  void saveKidsMode( boolean kidsMode) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("kidMode", kidsMode);
        editor.apply();
    }

    public  boolean getKidsMode() {
        boolean text = mSharedPreferences.getBoolean("kidMode", false);
        return text;
    }

    public  void savePrimaryAccountId( String primaryId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("primaryId", primaryId);
        editor.apply();
    }

    public  String getPrimaryAccountId() {
        String text = mSharedPreferences.getString("primaryId", "");
        return text;
    }

    public  void saveSecondaryAccountId( String secondaryId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("secondaryId", secondaryId);
        editor.apply();
    }

    public  String getSecondaryAccountId() {
        String text = mSharedPreferences.getString("secondaryId", "");
        return text;
    }

    public  void saveNotificationEnable( boolean noti) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("notificationEnable", noti);
        editor.apply();
    }

    public  boolean getNotificationEnable() {
        boolean text = mSharedPreferences.getBoolean("notificationEnable", false);
        return text;
    }

    public  void saveVia( String via) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("via", via);
        editor.apply();
    }

    public  String getVia() {
        String text = mSharedPreferences.getString("via", "");
        return text;
    }
    public void setColorJson(ColorsModel colorsModel) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(COLOR_JSON, new Gson().toJson(colorsModel));
        editor.apply();
    }

    public String getColorJson() {
        return mSharedPreferences.getString(COLOR_JSON, "");
    }

     public void setStringJson(StringsData stringData) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(STRING_JSON, new Gson().toJson(stringData));
        editor.apply();
    }

    public String getStringJson() {
        return mSharedPreferences.getString(STRING_JSON, "");
    }
}
