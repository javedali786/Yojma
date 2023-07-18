package com.tv.utils.helpers.ksPreferenceKeys;

public class KidsModeSinglton {


    private static KidsModeSinglton single_instance = null;
    public boolean aBoolean;


    private KidsModeSinglton()
    {

    }
    public static KidsModeSinglton getInstance()
    {
        if (single_instance == null)
            single_instance = new KidsModeSinglton();

        return single_instance;
    }
}
