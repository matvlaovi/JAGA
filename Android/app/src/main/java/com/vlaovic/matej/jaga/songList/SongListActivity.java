package com.vlaovic.matej.jaga.songList;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.vlaovic.matej.jaga.R;
import com.vlaovic.matej.jaga.preferences.PreferencesActivity;
import com.vlaovic.matej.jaga.tuner.TunerActivity;
import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        Toolbar musicListToolbar = findViewById(R.id.toolbar);
        musicListToolbar.setTitle(R.string.app_name);
        setSupportActionBar(musicListToolbar);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundleSaved = new Bundle();
        bundleSaved.putInt("saved", 1);
        SongListFragment musicListSavedFragment = new SongListFragment();
        musicListSavedFragment.setArguments(bundleSaved);

        adapter.addFragment(new SongListFragment(), getString(R.string.all));
        adapter.addFragment(musicListSavedFragment, getString(R.string.favourite));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_guitar_tuner:
                Intent tunerIntent = new Intent(this, TunerActivity.class);
                startActivity(tunerIntent);
                break;
            case R.id.action_settings:
                Intent preferencesIntent = new Intent(this, PreferencesActivity.class);
                startActivity(preferencesIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
