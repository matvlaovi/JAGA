package com.vlaovic.matej.jaga.preferences;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.vlaovic.matej.jaga.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

}