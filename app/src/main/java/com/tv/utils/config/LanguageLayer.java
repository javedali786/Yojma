package com.tv.utils.config;

import com.tv.SDKConfig;
import com.tv.utils.Logger;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

public class LanguageLayer {
    private static LanguageLayer languageLayerInstance;

    private LanguageLayer() {

    }

    public static LanguageLayer getInstance() {
        if (languageLayerInstance == null) {
            languageLayerInstance = new LanguageLayer();
        }
        return (languageLayerInstance);
    }



    public static String getCurrentLanguageCode() {
        String languageCode="";
        Logger.d("languageValues: " + SDKConfig.getInstance().getSpanishLangCode());
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("spanish")){
            languageCode= SDKConfig.getInstance().getSpanishLangCode();
        }else {
            languageCode= SDKConfig.getInstance().getEnglishCode();
        }
        return languageCode;
    }
}

