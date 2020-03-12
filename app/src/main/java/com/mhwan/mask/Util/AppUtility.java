package com.mhwan.mask.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    public String getAddress(double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(AppContext.getContext(), Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            nowAddress = "주소를 가져오는데 실패했습니다.";
            e.printStackTrace();
        }
        return nowAddress;
    }

    public String getAppVersion() {
        String version = "";
        try {
            PackageInfo i = AppContext.getContext().getPackageManager().getPackageInfo(AppContext.getContext().getPackageName(), 0);
            version = i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }

    public class UrlList {
        public static final String BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/";
        public static final String BY_GEO_URL = "/storesByGeo/json";
        public static final String BY_ADDR_URL = "/storesByAddr/json";
        public static final String MASK_PURCHASE_INFO_URL = "http://blog.naver.com/kfdazzang/221839489769";
    }
}
