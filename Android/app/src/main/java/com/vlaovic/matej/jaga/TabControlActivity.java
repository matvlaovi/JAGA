package com.vlaovic.matej.jaga;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TabControlActivity extends AppCompatActivity {

    private Toolbar musicListToolbar;
    private Song songData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_control);

        songData = getIntent().getParcelableExtra("songTag");
        fillViewWithData();

        musicListToolbar = findViewById(R.id.toolbar);
        musicListToolbar.setTitle("JAGA");
        setSupportActionBar(musicListToolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_guitar_tuner:
                Toast.makeText(TabControlActivity.this, "štimer", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_settings:
                Toast.makeText(TabControlActivity.this, "postavke", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillViewWithData(){
        TextView titleView = findViewById(R.id.title);
        TextView artistView = findViewById(R.id.artist);
        TextView tabsView = findViewById(R.id.tabs);

        titleView.setText(songData.getTitle());
        artistView.setText(songData.getArtist());
        tabsView.setText(songData.getTabs());
    }

}
