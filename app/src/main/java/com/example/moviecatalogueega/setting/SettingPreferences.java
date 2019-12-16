package com.example.moviecatalogueega.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.moviecatalogueega.R;

import static com.example.moviecatalogueega.setting.NotifReceiver.*;

public class SettingPreferences extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private NotifReceiver notifReceiver;
    private String LANGUAGE;
    private String DAILY_REMINDER;
    private String RELEASE_DAILY_REMINDER;

    private Preference language_preference;
    private SwitchPreference daily_reminder_preference;
    private SwitchPreference daily_release_reminder_preference;


    private void init(){
        LANGUAGE = getResources().getString(R.string.key_lang);
        DAILY_REMINDER = getResources().getString(R.string.key_today_reminder);
        RELEASE_DAILY_REMINDER = getResources().getString(R.string.key_release_reminder);

        language_preference = findPreference(LANGUAGE);
        daily_reminder_preference = findPreference(DAILY_REMINDER);
        daily_release_reminder_preference = findPreference(RELEASE_DAILY_REMINDER);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        notifReceiver = new NotifReceiver();
        addPreferencesFromResource(R.xml.preferences);
        init();
        language_preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DAILY_REMINDER)){

            if (!sharedPreferences.getBoolean(DAILY_REMINDER, false)){
                notifReceiver.cancelAlarm(getActivity(), ID_REPEATING_REMINDER);
            } else {
                notifReceiver.setRepeatingAlarm(getActivity(), ID_REPEATING_REMINDER, ID_REPEATING_REMINDER);
                Log.d("Preference" ,"Dijalankan");
            }
        }

        if (key.equals(RELEASE_DAILY_REMINDER)){
            if (!sharedPreferences.getBoolean(RELEASE_DAILY_REMINDER, false)){
                notifReceiver.cancelAlarm(getActivity(), ID_REPEATING_RELEASE);
            } else {
                notifReceiver.setRepeatingAlarm(getActivity(),ID_REPEATING_RELEASE, ID_REPEATING_RELEASE);

            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
