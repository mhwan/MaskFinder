package com.mhwan.mask.CustomUI;

import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.mhwan.mask.R;
import com.mhwan.mask.Util.MyPreference;

public class CustomPreferenceSeekbar extends Preference {
    private Context context;
    private int current;
    private MyPreference preference;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomPreferenceSeekbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        preference = new MyPreference(context);
        setLayoutResource(R.layout.layout_preference_seekbar);
    }

    public CustomPreferenceSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        preference = new MyPreference(context);
        setLayoutResource(R.layout.layout_preference_seekbar);
    }

    public CustomPreferenceSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        preference = new MyPreference(context);
        setLayoutResource(R.layout.layout_preference_seekbar);
    }

    public CustomPreferenceSeekbar(Context context) {
        super(context);
        this.context = context;
        preference = new MyPreference(context);
        setLayoutResource(R.layout.layout_preference_seekbar);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        int value = preference.getRange();

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        TextView textView = (TextView) view.findViewById(android.R.id.summary);

        seekBar.setProgress(value);
        textView.setText(String.format(context.getString(R.string.mask_search_range_format), String.valueOf(value * 0.5)));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(String.format(context.getString(R.string.mask_search_range_format), String.valueOf(progress * 0.5)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preference.setRange(seekBar.getProgress());
            }
        });

    }

}
