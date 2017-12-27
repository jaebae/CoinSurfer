package com.sungjae.coinsurfer.activity.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;

import com.sungjae.coinsurfer.R;

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onResume() {
        super.onResume();
        registerPreferenceChangeListener();
        updateView(getPreferenceScreen());
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterPreferenceChangeListener();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        updateView(pref);
    }

    private void updateView(Preference pref) {
        if (pref instanceof PreferenceGroup) {
            for (int i = 0; i < ((PreferenceGroup) pref).getPreferenceCount(); i++) {
                updateView(((PreferenceGroup) pref).getPreference(i));
            }
        } else if (pref instanceof EditTextPreference) {
            pref.setSummary(((EditTextPreference) pref).getText());
        }

    }

    private void registerPreferenceChangeListener() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void unregisterPreferenceChangeListener() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
