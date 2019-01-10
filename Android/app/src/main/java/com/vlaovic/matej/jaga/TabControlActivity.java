package com.vlaovic.matej.jaga;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.repo.BubbleSeekBar;

public class TabControlActivity extends AppCompatActivity {

    private Toolbar musicListToolbar;
    private Song songData;
    private AppDatabase db;

    private int interval = 10;

    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            ScrollView sv = findViewById(R.id.tabsScrollView);

            timerHandler.postDelayed(this, interval * 1000);
            sv.smoothScrollTo(0,sv.getScrollY() + 400);

            if (sv.getChildAt(0).getBottom() == (sv.getHeight() + sv.getScrollY())) {
                swapPlayPause();
                timerHandler.removeCallbacks(timerRunnable);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_control);


        db = AppDatabase.getAppDatabase(getApplicationContext());

        songData = getIntent().getParcelableExtra("songTag");
        fillViewWithData();

        musicListToolbar = findViewById(R.id.toolbar);
        musicListToolbar.setTitle("JAGA");
        setSupportActionBar(musicListToolbar);

        final ImageView playBtn = findViewById(R.id.play_button);
        final ImageView pauseBtn = findViewById(R.id.pause_button);

        com.xw.repo.BubbleSeekBar mBubbleSeekBar = findViewById(R.id.bubbleSeekBar);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapPlayPause();
                timerHandler.postDelayed(timerRunnable, 0);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapPlayPause();
                timerHandler.removeCallbacks(timerRunnable);
            }
        });

        mBubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                interval = progress;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                timerHandler.removeCallbacks(timerRunnable);
                swapPlayPause();
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });


        final ImageView favoriteFilledBtn = findViewById(R.id.favorite_filled);
        final ImageView favoriteBorderBtn = findViewById(R.id.favorite_border);

        favoriteBorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapVisibility(1);
                db.songDao().updateSaved(1, songData.getId());
                Toast.makeText(TabControlActivity.this, "Added to favorites!", Toast.LENGTH_LONG).show();
            }
        });

        favoriteFilledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapVisibility(0);
                db.songDao().updateSaved(0, songData.getId());
                Toast.makeText(TabControlActivity.this, "Removed from favorites!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        songData = db.songDao().getSongById(songData.getId());

        TextView titleView = findViewById(R.id.title);
        TextView artistView = findViewById(R.id.artist);
        TextView tabsView = findViewById(R.id.tabs);

        titleView.setText(songData.getTitle());
        artistView.setText(songData.getArtist());
        tabsView.setText(songData.getTabs());

        swapVisibility(songData.getSaved());
    }

    public void swapPlayPause(){

        ImageView playBtn = findViewById(R.id.play_button);
        ImageView pauseBtn = findViewById(R.id.pause_button);

        if (playBtn.getVisibility() == View.VISIBLE){
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
        }
        else{
            pauseBtn.setVisibility(View.GONE);
            playBtn.setVisibility(View.VISIBLE);
        }
    }

    public void swapVisibility(int saved){
        final ImageView favoriteFilledBtn = findViewById(R.id.favorite_filled);
        final ImageView favoriteBorderBtn = findViewById(R.id.favorite_border);

        if(saved == 1){
            favoriteBorderBtn.setVisibility(View.GONE);
            favoriteFilledBtn.setVisibility(View.VISIBLE);
        }
        else{
            favoriteBorderBtn.setVisibility(View.VISIBLE);
            favoriteFilledBtn.setVisibility(View.GONE);
        }
    }



}
