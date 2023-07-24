package com.tv.uscreen.yojmatv.utils.config;

import com.tv.uscreen.yojmatv.SDKConfig;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

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
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")){
            languageCode= SDKConfig.getInstance().getEnglishCode();
        }else {
            languageCode= "es-ES";
        }
        return languageCode;
    }
}

