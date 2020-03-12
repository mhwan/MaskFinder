package com.mhwan.mask.Util;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mhwan.mask.R;


public class PermissionChecker {
    // 받고싶은 권한을 배열로 전달받음
    private String[] permission_list;
    // 권한을 요구하고싶은 액티비티
    private AppCompatActivity activity;
    // 권한을 사용자가 허용 / 거절 했을때 행동
    private PermissioEventCallback eventCallback;

    public PermissionChecker(String[] permission_list, AppCompatActivity activity) {
        this.permission_list = permission_list;
        this.activity = activity;
    }

    public PermissionChecker(String[] permission_list, AppCompatActivity activity, PermissioEventCallback eventCallback) {
        this.permission_list = permission_list;
        this.activity = activity;
        this.eventCallback = eventCallback;
    }

    // OnCreate에서 API 버전과 상관없이 무조건 호출필요
    // API 버전에 따른 체크는 함수내부에서 진행
    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = ContextCompat.checkSelfPermission(activity, permission);

            if(chk != PackageManager.PERMISSION_GRANTED){
                //권한 허용을여부를 확인하는 창을 띄운다
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        permission)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("위치정보 권한이 거부되었습니다. 앱에서 사용자의 현재 위치를 기반으로 마스크 정보를 가져오려면 설정에서 권한을 허용해주세요.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    activity.requestPermissions(permission_list, 0);
                }
            }
        }
    }
    // 각 AppCompatActivity의 onRequestPermissionsResult 메소드를 재정의 한 후 이 PermissionChecker의 requestPermissionsResult 호출
    public void requestPermissionsResult(int requestCode,int[] grantResults){
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){

                }
                else {
                    if(eventCallback!=null){
                        eventCallback.OnDenial();
                    }
                    else {
                    }
                    return;
                }
            }
            if(eventCallback!=null){
                eventCallback.OnPermit();
            }
        }
    }

}
