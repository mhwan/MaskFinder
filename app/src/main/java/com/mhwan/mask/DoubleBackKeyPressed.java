package com.mhwan.mask;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.mhwan.mask.Util.AppUtility;

public class DoubleBackKeyPressed {
    private long backKeyPressTime = 0;
    private Activity activity;
    private View view;
    private Snackbar snackbar;

    public DoubleBackKeyPressed(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        snackbar = Snackbar.make(view, activity.getString(R.string.quit_app), Snackbar.LENGTH_SHORT);
    }

    public void onBackPressed() {
        //2초이상 지났으면 마지막 시간을 현재 시간으로 갱신하고 토스트 메시지 실행
        if (System.currentTimeMillis() > backKeyPressTime + 2000) {
            backKeyPressTime = System.currentTimeMillis();
            snackbar.show();
            return;
        }
        //2초이상 안지났으면 액티비티 종료
        else
            AppUtility.getAppinstance().finishApplication(activity);
    }
}
