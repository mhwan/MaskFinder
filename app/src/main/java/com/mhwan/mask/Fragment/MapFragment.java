package com.mhwan.mask.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mhwan.mask.Activity.MainActivity;
import com.mhwan.mask.Item.MaskStoreItem;
import com.mhwan.mask.Item.Store;
import com.mhwan.mask.R;
import com.mhwan.mask.Util.AppContext;
import com.mhwan.mask.Util.AppUtility;
import com.mhwan.mask.Util.GetMaskListener;
import com.mhwan.mask.Util.GpsTracker;
import com.mhwan.mask.Util.MyPreference;
import com.mhwan.mask.Util.NetworkUtils.NewGetMaskInfo;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements MapView.OpenAPIKeyAuthenticationResultListener, View.OnClickListener, MapView.POIItemEventListener {
    private View view;
    //private EditText search_places;
    private MapView mapView;
    private List<Store> storeList;
    private ViewPager viewPager;
    private TextView noDataText;
    private int selection = -1;
    private String[] typeArray;
    private StorePagerAdapter pagerAdapter;
    private MyPreference preference;
    private AutocompleteSupportFragment autocompleteFragment;
    private static final int GPS_ENABLE_REQUEST_CODE = 1102;

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

    private void initView() {
        preference = new MyPreference(getContext());
        ((MainActivity) getActivity()).setListener(new GetMaskListener() {
            @Override
            public void onDataUpdate() {
                updateLocation();
            }
        });


        typeArray = getResources().getStringArray(R.array.store_type_array);
        mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        setupAutoCompleteFragment();

        view.findViewById(R.id.fab_map).setOnClickListener(this::onClick);
        view.findViewById(R.id.fab_now_location).setOnClickListener(this::onClick);
        noDataText = (TextView) view.findViewById(R.id.info_nodata);
        viewPager = view.findViewById(R.id.info_viewpager);
        pagerAdapter = new StorePagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selection = position;
                MapPOIItem[] poiItem = mapView.getPOIItems();
                if (poiItem != null && poiItem.length > 0) {
                    mapView.selectPOIItem(poiItem[selection], true);
                    mapView.setMapCenterPoint(poiItem[selection].getMapPoint(), true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //search_places = (EditText) view.findViewById(R.id.search_locations);
        updateLocation();
    }

    private void setupAutoCompleteFragment() {
        // Initialize the SDK
        if (!Places.isInitialized())
            Places.initialize(getActivity().getApplicationContext(), AppContext.getContext().getString(R.string.google_sdk_api_key));

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(AppContext.getContext());
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("KR");
// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                Log.i("place_autocomplete", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                LatLng latLng = place.getLatLng();
                setLocationWork(latLng.latitude, latLng.longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("place_autocomplete", "An error occurred: " + status);
            }
        });
    }


    /**
     * 도무지 왠지 알수없는 에러가뜸.. url도 인코딩도 제대로 된거같은데.. 헤더문제인지 살펴봐야할듯
     * 안되면 retrofit같은 다른 api를 사용해보는것도..
     */
    private void doGetMaskInfoWork() {


        /*
        Retrofit retrofit = NewGetMaskInfo.getInstance().getRetrofitClient();
        NewGetMaskInfo.ApiService apiService = retrofit.create(NewGetMaskInfo.ApiService.class);
        Call<ResponseBody> call = apiService.getStoreFromAddress("서울특별시 강남구");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i("response", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("response_error", "error");
                t.printStackTrace();
            }
        });

         */

        /*

        Retrofit retrofit = NewGetMaskInfo.getInstance().getRetrofitClient();
        NewGetMaskInfo.ApiService apiService = retrofit.create(NewGetMaskInfo.ApiService.class);
        Map map = new HashMap<String, String>();
        map.put("lat", "37.2765655");
        map.put("lng", "127.133972");
        map.put("m", "1000");
        Call<MaskStoreItem> call = apiService.getStoreFromGeo(map);
        call.enqueue(new Callback<MaskStoreItem>() {
            @Override
            public void onResponse(Call<MaskStoreItem> call, Response<MaskStoreItem> response) {
                MaskStoreItem storeItem = response.body();
                Log.i("response", storeItem.toString());
            }

            @Override
            public void onFailure(Call<MaskStoreItem> call, Throwable t) {
                Log.e("response_error", "error");
                t.printStackTrace();
            }
        });*/
    }

    private void addPinToMap(MaskStoreItem maskStoreItem) {
        if (maskStoreItem != null) {
            if (maskStoreItem.getCount() > 0) {
                if (storeList != null)
                    storeList.clear();
                if (mapView.getPOIItems().length > 0)
                    mapView.removeAllPOIItems();

                storeList = maskStoreItem.getStores();
                pagerAdapter.resetList(storeList);
                MapPOIItem[] poiItems = new MapPOIItem[storeList.size()];
                for (int i = 0; i < storeList.size(); i++) {
                    poiItems[i] = new MapPOIItem();
                    poiItems[i].setMapPoint(MapPoint.mapPointWithGeoCoord(storeList.get(i).getLat(), storeList.get(i).getLng()));
                    poiItems[i].setTag(i);
                    poiItems[i].setItemName(storeList.get(i).getName());
                    poiItems[i].setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    String remain = storeList.get(i).getRemainStat();
                    int resourceId = R.drawable.ic_img_marker_grey;
                    if (remain != null) {
                        if (remain.equals("plenty")) {
                            resourceId = R.drawable.ic_img_marker_green;
                        } else if (remain.equals("some")) {
                            resourceId = R.drawable.ic_img_marker_yellow;
                        } else if (remain.equals("few")) {
                            resourceId = R.drawable.ic_img_marker_red;
                        }
                    }

                    poiItems[i].setCustomImageResourceId(resourceId);
                    poiItems[i].setCustomImageAutoscale(true);
                    poiItems[i].setCustomImageAnchor(0.5f, 1.0f);
                    //poiItems[i].setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                }
                mapView.setPOIItemEventListener(this);
                mapView.addPOIItems(poiItems);

                changeViewVisible(true, "");
                selection = 0;
                mapView.selectPOIItem(poiItems[selection], true);
                viewPager.setCurrentItem(selection, true);
            } else {
                changeViewVisible(false, AppContext.getContext().getString(R.string.message_no_sale_mask));
            }
        }
    }

    private void getMaskInfoFromGeo(double lat, double lng) {
        if (preference == null)
            preference = new MyPreference(getContext());
        Map map = new HashMap<String, String>();
        map.put("lat", String.valueOf(lat));
        map.put("lng", String.valueOf(lng));
        map.put("m", String.valueOf(preference.getRange() * 500));

        NewGetMaskInfo.getInstance().getStorebyGeo(map, new Callback<MaskStoreItem>() {
            @Override
            public void onResponse(Call<MaskStoreItem> call, Response<MaskStoreItem> response) {
                if (response.isSuccessful()) {
                    MaskStoreItem storeItem = response.body();
                    Log.i("response", storeItem.toString());

                    addPinToMap(storeItem);
                } else
                    Toast.makeText(AppContext.getContext(), AppContext.getContext().getString(R.string.message_fail_get_data), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<MaskStoreItem> call, Throwable t) {
                Log.e("response_error", "error");
                t.printStackTrace();
                Toast.makeText(AppContext.getContext(), AppContext.getContext().getString(R.string.message_fail_get_data), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {
        Log.i("DaumMap", String.format("Open API Key Authentication Result : code=%d, message=%s", i, s));
    }


    /*----------Method to create an AlertBox ------------- */
    protected void showAlertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치서비스 비활성화");
        builder.setMessage("현위치를 탐색하기 위해서 위치서비스가 필요합니다. 위치 설정을 켜시고 다시 시도해주세요.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_now_location:
                updateLocation();
                break;
            case R.id.fab_map:
                Uri uri = Uri.parse("geo:" + storeList.get(selection).getLat() + "," + storeList.get(selection).getLng());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }

    private void updateLocation() {
        if (!checkLocationServicesStatus()) {
            showAlertbox();
        } else {
            Log.d("find locations", "");
            GpsTracker gpsTracker = new GpsTracker(getActivity());

            if (gpsTracker != null && gpsTracker.getLocation() != null) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                System.out.println(latitude + ", " + longitude);

                setLocationWork(latitude, longitude);
            } else {
                changeViewVisible(false, AppContext.getContext().getString(R.string.message_no_data_mask));
                Toast.makeText(AppContext.getContext(), AppContext.getContext().getString(R.string.message_cannot_access_denied), Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void changeViewVisible(boolean isVisible, String m){
        if (isVisible) {
            noDataText.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        } else {
            noDataText.setVisibility(View.VISIBLE);
            noDataText.setText(m);
            viewPager.setVisibility(View.GONE);
        }
    }
    private void setLocationWork(double latitude, double longitude) {
        getMaskInfoFromGeo(latitude, longitude);
        changeMapCenterZoom(latitude, longitude);
        setAddressText(AppUtility.getAppinstance().getAddress(latitude, longitude));
    }

    private void changeMapCenterZoom(double lat, double lng) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat, lng), getZoomLevel(), true);
    }

    private void setAddressText(String address) {
        //search_places.setText(address);
        autocompleteFragment.setText(address);
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) AppContext.getContext().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private int getZoomLevel() {
        if (preference == null)
            preference = new MyPreference(getContext());
        int a = preference.getRange();

        int result = 3;

        if (a >= 2 && a < 4)
            result = 4;
        else if (a >= 4 && a < 8)
            result = 5;
        else if (a >= 8)
            result = 6;

        return result;
    }

    private class StorePagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Context context;
        private ArrayList<Store> lists;

        public StorePagerAdapter(Context context) {
            this.context = context;
            this.layoutInflater = (LayoutInflater) this.context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.lists = new ArrayList<>();
        }

        public void resetList(List<Store> list) {
            lists.clear();
            lists.addAll(list);

            System.out.println("list : " + lists.toString());
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = this.layoutInflater.inflate(R.layout.ui_store_info, container, false);
            ViewGroup frame = view.findViewById(R.id.frame);
            TextView name = view.findViewById(R.id.info_name);
            TextView type = view.findViewById(R.id.info_type);
            TextView address = view.findViewById(R.id.info_address);
            TextView count = view.findViewById(R.id.info_count);
            TextView last = view.findViewById(R.id.info_lastStock);
            TextView update = view.findViewById(R.id.info_update);

            name.setText(lists.get(position).getName());
            type.setText(typeArray[Integer.parseInt(lists.get(position).getType()) - 1]);
            address.setText(lists.get(position).getAddr());
            String s = lists.get(position).getRemainStat();
            if (s != null) {
                if (s.equals("plenty")) {
                    count.setBackgroundResource(R.drawable.bg_outerbox_green);
                    count.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    count.setText("100개 이상");
                } else if (s.equals("some")) {
                    count.setBackgroundResource(R.drawable.bg_outerbox_yellow);
                    count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentYellow));
                    count.setText("30 ~ 100개");
                } else if (s.equals("few")) {
                    count.setBackgroundResource(R.drawable.bg_outerbox_red);
                    count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentRed));
                    count.setText("30개 이하");
                } else if (s.equals("empty")) {
                    count.setBackgroundResource(R.drawable.bg_outerbox_grey);
                    count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLightGrey));
                    count.setText("재고 없음");
                }
            } else {
                count.setBackgroundResource(R.drawable.bg_outerbox_grey);
                count.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLightGrey));
                count.setText("정보 없음");
            }
            String l = (lists.get(position).getStockAt() == null)? "정보없음" : lists.get(position).getStockAt();
            last.setText("최근 입고 : "+l);
            String u = (lists.get(position).getCreatedAt() == null)? "정보없음" : lists.get(position).getCreatedAt();
            update.setText("정보 업데이트 : "+u);

            container.addView(view);
            return view;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        int i = mapPOIItem.getTag();
        viewPager.setCurrentItem(i, true);

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
