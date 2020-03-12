package com.mhwan.mask.CustomUI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.mhwan.mask.R;


public class IntroDialog extends Dialog {
    private Context context;
    private View.OnClickListener listener;
    public IntroDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_intro_dialog);
        findViewById(R.id.positive_button).setOnClickListener(listener);
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
