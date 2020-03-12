package com.mhwan.mask.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mhwan.mask.R;
import com.mhwan.mask.Util.AppUtility;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setToolbar();
        getFragmentManager().beginTransaction()
                .replace(R.id.setting_frame, new SettingsFragment(), null).commit();
    }



    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.findViewById(R.id.toolbar_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private Preference pref_appver, pref_mail, pref_share;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_settings);

            pref_appver = findPreference("key_app_ver");
            pref_mail = findPreference("key_mail_to_developer");
            pref_share = findPreference("key_share_friends");

            pref_appver.setOnPreferenceClickListener(this);
            pref_share.setOnPreferenceClickListener(this);
            pref_mail.setOnPreferenceClickListener(this);

            pref_appver.setSummary(AppUtility.getAppinstance().getAppVersion());
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            //super.onViewCreated(view, savedInstanceState);
            ListView listView = view.findViewById(android.R.id.list);
            listView.setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.colorAccentLightGrey))); // or some other color int
            listView.setDividerHeight(1);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key.equals(pref_appver.getKey())) {

                return true;
            } else if (key.equals(pref_mail.getKey())) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "mhwanbae21@gmail.com", null));
                startActivity(Intent.createChooser(intent, null));
                return true;
            } else if (key.equals(pref_share.getKey())) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "마스크파인더, 이 앱을 한번 설치해보세요! https://play.google.com/store/apps/details?id=com.mhwan.mask");
                startActivity(Intent.createChooser(emailIntent, "앱 초대"));
                return true;
            }
            return false;
        }
    }
}
