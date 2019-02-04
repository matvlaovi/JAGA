package com.vlaovic.matej.jaga.preferences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.vlaovic.matej.jaga.R;
import java.util.Objects;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar musicListToolbar = findViewById(R.id.toolbar);
        musicListToolbar.setTitle(R.string.settings);
        setSupportActionBar(musicListToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            Fragment preferenceFragment = new PreferencesFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.preferences_container, preferenceFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
