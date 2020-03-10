package com.mhwan.mask.Fragment;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhwan.mask.GetMaskListener;
import com.mhwan.mask.Item.MaskStoreItem;
import com.mhwan.mask.Util.NetworkUtils.GetMaskInfoTask;
import com.mhwan.mask.R;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements LocationListener, MapView.OpenAPIKeyAuthenticationResultListener, GetMaskListener {
    private View view;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        initView();
        return view;
    }

    private void initView(){
        MapView mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        doGetMaskInfoWork();
    }

    /**
     * 도무지 왠지 알수없는 에러가뜸.. url도 인코딩도 제대로 된거같은데.. 헤더문제인지 살펴봐야할듯
     * 안되면 retrofit같은 다른 api를 사용해보는것도..
     */
    private void doGetMaskInfoWork(){
        /*
        GetMaskInfoTask task = new GetMaskInfoTask(getActivity(), this);
        String[] sample = new String[]{"서울특별시 강남구"};
        task.execute(sample);*/
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {
        Log.i("DaumMap", String.format("Open API Key Authentication Result : code=%d, message=%s", i, s));
    }

    @Override
    public void onDataReceived(ArrayList<MaskStoreItem> list) {
        Log.d("list_received", list.toString());
    }
}
