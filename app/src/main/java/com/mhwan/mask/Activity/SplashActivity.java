package com.mhwan.mask.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.mhwan.mask.CustomUI.IntroDialog;
import com.mhwan.mask.R;
import com.mhwan.mask.Util.MyPreference;

public class SplashActivity extends AppCompatActivity {
    private final Handler handler = new Handler();
    private final int SPLASH_DELAY_TIME = 1400;
    private View decorView;
    private int uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        setContentView(R.layout.activity_splash);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateScreen();
            }
        }, SPLASH_DELAY_TIME);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            decorView.setSystemUiVisibility(uiOption);
        }
    }


    private void launchMainActiviy(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 초기화면에 안내 다이얼로그를 띄어야함
     */
    private void navigateScreen() {
        MyPreference preference = new MyPreference(SplashActivity.this);

        if (!preference.getIntro()) {
            preference.setIntro(true);
            IntroDialog introDialog = new IntroDialog(SplashActivity.this);
            introDialog.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    introDialog.dismiss();
                    launchMainActiviy();
                }
            });
            introDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    launchMainActiviy();
                }
            });

            introDialog.show();

        } else {
            launchMainActiviy();
        }
    }

}
