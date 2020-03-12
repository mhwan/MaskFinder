package com.mhwan.mask.CustomUI;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class StorePagerAdapter extends PagerAdapter {
    public StorePagerAdapter(){

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
