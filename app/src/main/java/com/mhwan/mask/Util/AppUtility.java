package com.mhwan.mask.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AppUtility {
    private static AppUtility Appinstance;
    public static final int NO_DISTANCE_ERR = 9009978;
    public static final int NO_DISTANCE_ERR_WITH_NO_LOCATION = 9209978;

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
        String nowAddress ="주소를 알 수가 없습니다.";
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

    public String getDistanceString(int dis){
        if (dis >= 1000) {
            return String.format("%.1fkm", dis/1000.0);
        } else
            return dis+"m";
    }
    public int getDistanceFromLocation(float lat1, float lng1, float lat2, float lng2){
        if (lat1 >= 0 && lng1 >= 0 && lat2 >= 0 && lng2 >= 0) {
            Location loc1 = new Location("");
            loc1.setLatitude(lat1);
            loc1.setLongitude(lng1);
            Location loc2 = new Location("");
            loc2.setLatitude(lat2);
            loc2.setLongitude(lng2);
            int distanceInMeters = (int) loc1.distanceTo(loc2);
            return distanceInMeters;
        }else
            return NO_DISTANCE_ERR;
    }

    public int getOrderRemainStat(String stat) {
        if (stat != null){
            if (stat.equals("plenty"))
                return 1;
            else if (stat.equals("some"))
                return 2;
            else if (stat.equals("few"))
                return 3;
            else if (stat.equals("empty"))
                return 4;
            else if (stat.equals("break"))
                return 5;
            else return 6;
        }
        return 6;
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(AppContext.getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    public class UrlList {
        public static final String BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/";
        public static final String BY_GEO_URL = "/storesByGeo/json";
        public static final String BY_ADDR_URL = "/storesByAddr/json";
        public static final String MASK_PURCHASE_INFO_URL = "http://blog.naver.com/kfdazzang/221839489769";
    }
}
