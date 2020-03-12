package com.mhwan.mask.Util;

import android.content.Context;

public class MyPreference extends BaseSharedPreference{
    private final static String key_range = "ranges";
    private final static String key_show_intro = "intro";
    public MyPreference(Context context) {
        super(context, "MyMaskFinderPreferences");
    }

    public void setRange(int r){
        putValue(key_range, r);
    }
    public int getRange(){
        return getValue(key_range, 2);
    }

    public boolean getIntro() {
        return getValue(key_show_intro, false);
    }

    public void setIntro(boolean b) {
        putValue(key_show_intro, b);
    }
}
