package com.mhwan.mask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

public class MapActivity extends AppCompatActivity {
    // 카카오에서 쓰이는 키 해시값
    //pgHvGxUG/kVQo8SEv+BnhxY+tv4=


    private PermissionChecker pc;

    private String[] permission_list = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        pc = new PermissionChecker(permission_list,this);
        pc.checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        pc.requestPermissionsResult(requestCode,grantResults);
    }


}
