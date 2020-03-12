package com.mhwan.mask.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mhwan.mask.Util.AppContext;
import com.mhwan.mask.Util.AppUtility;
import com.mhwan.mask.Util.DoubleBackKeyPressed;
import com.mhwan.mask.Fragment.InfoFragment;
import com.mhwan.mask.Fragment.MapFragment;
import com.mhwan.mask.Fragment.MaskFiveFragment;
import com.mhwan.mask.Util.GetMaskListener;
import com.mhwan.mask.Util.PermissioEventCallback;
import com.mhwan.mask.Util.PermissionChecker;
import com.mhwan.mask.R;
import com.mhwan.mask.Util.SignetureUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissioEventCallback {
    private BottomNavigationView bottomNavigationView;
    private static final int REQUEST_CODE = 1008;
    private PermissionChecker pc;
    private List<Fragment> fragmentList;
    private ViewGroup rootlayout;
    private String[] permission_list = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private DoubleBackKeyPressed doubleBackKeyPressed;
    private GetMaskListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        initView();
        doubleBackKeyPressed = new DoubleBackKeyPressed(MainActivity.this, rootlayout);
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.findViewById(R.id.toolbar_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }
    private void initView(){
        if (!AppUtility.getAppinstance().isNetworkConnection())
            Toast.makeText(AppContext.getContext(), AppContext.getContext().getString(R.string.message_no_network), Toast.LENGTH_SHORT).show();
        rootlayout = findViewById(R.id.root);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        initFragment();
        transaction.replace(R.id.fragment_container, fragmentList.get(0)).commitAllowingStateLoss();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        switch (item.getItemId()) {
                            case R.id.menu_mask:
                                item.setChecked(true);
                                transaction.replace(R.id.fragment_container, fragmentList.get(0)).commitAllowingStateLoss();
                                break;
                            case R.id.menu_five:
                                item.setChecked(true);
                                transaction.replace(R.id.fragment_container, fragmentList.get(1)).commitAllowingStateLoss();
                                break;
                            case R.id.menu_info:
                                item.setChecked(true);
                                transaction.replace(R.id.fragment_container, fragmentList.get(2)).commitAllowingStateLoss();
                                break;
                        }
                        return false;
                    }
                });


        pc = new PermissionChecker(permission_list,this, this);
        pc.checkPermission();

        //Log.i("keyHash", SignetureUtil.getSigneture(getApplicationContext()));
    }

    private List initFragment(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new MapFragment());
        fragmentList.add(new MaskFiveFragment());
        fragmentList.add(new InfoFragment());

        return fragmentList;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        pc.requestPermissionsResult(requestCode,grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && listener != null) {
            listener.onDataUpdate();
        }
    }

    public void setListener(GetMaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackKeyPressed != null) {
            doubleBackKeyPressed.onBackPressed();
        } else
            super.onBackPressed();
    }

    @Override
    public void OnPermit() {
        if (listener != null)
            listener.onDataUpdate();
    }

    @Override
    public void OnDenial() {
        Toast.makeText(AppContext.getContext(), AppContext.getContext().getString(R.string.permission_denied_message),Toast.LENGTH_SHORT).show();
    }
}
