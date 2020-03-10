package com.mhwan.mask.Util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class AppUtility {
    private static AppUtility Appinstance;

    private AppUtility(){
    }
    public synchronized static AppUtility getAppinstance() {
        if (Appinstance == null)
            Appinstance = new AppUtility();
        return Appinstance;
    }

    public void finishApplication(Activity activity) {
        activity.moveTaskToBack(true);
        activity.finish();
    }

    public boolean isNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public int getMaskDay(int finalnum){
        if (finalnum > 5)
            return finalnum - 6;
        else if (finalnum == 0)
            return 4;
        else
            return finalnum-1;
    }

    public class UrlList {
        public static final String BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1";
        public static final String BY_GEO_URL = "/storesByGeo/json";
        public static final String BY_ADDR_URL = "/storesByAddr/json";
        public static final String MASK_PURCHASE_INFO_URL = "http://blog.naver.com/kfdazzang/221839489769";
    }
}
