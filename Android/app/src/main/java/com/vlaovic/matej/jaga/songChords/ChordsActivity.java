package com.vlaovic.matej.jaga.songChords;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.vlaovic.matej.jaga.R;
import com.vlaovic.matej.jaga.chord.ChordView;
import com.vlaovic.matej.jaga.chord.ChordViewFactory;
import com.vlaovic.matej.jaga.database.AppDatabase;
import com.vlaovic.matej.jaga.database.Song;
import com.xw.repo.BubbleSeekBar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChordsActivity extends AppCompatActivity {

    private Song songData;
    private AppDatabase db;

    Thread svThread = null;
    ScrollView sv = null;
    private volatile boolean svScrolling = false;
    private int svInterval = 5;

    ImageView playBtn = null;
    ImageView pauseBtn = null;

    ImageView favouriteFilledBtn = null;
    ImageView favouriteBorderBtn = null;

    com.xw.repo.BubbleSeekBar mBubbleSeekBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        songData = getIntent().getParcelableExtra("songTag");

        // Toolbar
        Toolbar musicListToolbar = findViewById(R.id.toolbar);
        musicListToolbar.setTitle("JAGA");
        setSupportActionBar(musicListToolbar);

        // ScrollView
        sv = findViewById(R.id.tabsScrollView);
        setSvListeners();

        // Play/Pause
        playBtn = findViewById(R.id.play_button);
        pauseBtn = findViewById(R.id.pause_button);
        setPlayPauseListeners();

        // SeekBar
        mBubbleSeekBar = findViewById(R.id.bubbleSeekBar);
        setSeekBarListeners();

        // Favourite buttons
        favouriteFilledBtn = findViewById(R.id.favorite_filled);
        favouriteBorderBtn = findViewById(R.id.favorite_border);
        setFavouriteButtonsListeners();

        fillViewWithData();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseAutoScroll();
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
                Toast.makeText(ChordsActivity.this, "štimer", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_settings:
                Toast.makeText(ChordsActivity.this, "postavke", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillViewWithData(){
        songData = db.songDao().getSongById(songData.getId());

        TextView titleView = findViewById(R.id.title);
        TextView artistView = findViewById(R.id.artist);

        titleView.setText(songData.getTitle());
        artistView.setText(songData.getArtist());

        swapFavouriteVisibility(songData.getSaved());

        setTabsTextView(songData.getTabs());
    }

    public void pauseAutoScroll(){
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);

        svScrolling = false;

        if(svThread != null){
            svThread.interrupt();
        }

    }

    public void startAutoScroll(){
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);

        svScrolling = true;
        startSvScrollThread();
    }

    public void swapFavouriteVisibility(int saved){
        if(saved == 1){
            favouriteBorderBtn.setVisibility(View.GONE);
            favouriteFilledBtn.setVisibility(View.VISIBLE);
        }
        else{
            favouriteBorderBtn.setVisibility(View.VISIBLE);
            favouriteFilledBtn.setVisibility(View.GONE);
        }
    }

    public void setTabsTextView(String text){
        String regex = "(?<!\\S)([A-G]|Am|Em|Dm|A7|B7|D7)(?!\\S)";
        Pattern pattern = Pattern.compile(regex);

        SpannableString spannable = new SpannableString(text);
        final Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            final String chordName = matcher.group(1);

            ClickableSpan clickSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    ChordView chordView = ChordViewFactory.getChordView("popup");
                    chordView.showChord(ChordsActivity.this, chordName);
                }
            };
            spannable.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        TextView tabsView = findViewById(R.id.tabs);

        tabsView.setText(spannable, TextView.BufferType.SPANNABLE);
        tabsView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void startSvScrollThread(){
        svThread = new Thread() {
            @Override
            public void run() {
                while (svScrolling) {
                    sv.smoothScrollTo(0, sv.getScrollY() + 1);

                    if (sv.getChildAt(0).getBottom() == (sv.getHeight() + sv.getScrollY())) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pauseAutoScroll();
                            }
                        });

                        break;
                    }

                    try {
                        Thread.sleep(svInterval);
                    }
                    catch (InterruptedException e) {
                        svScrolling = false;
                        break;
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        svScrolling = false;
                        break;
                    }
                }
            }
        };

        svThread.start();
    }

    public void setSvListeners(){
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_UP:{
                        pauseAutoScroll();
                    }
                }
                return false;
            }
        });
    }

    public void setSeekBarListeners(){
        mBubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                svInterval = progress;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
    }

    public void setPlayPauseListeners(){
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAutoScroll();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseAutoScroll();
            }
        });
    }

    public void setFavouriteButtonsListeners(){
        favouriteBorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapFavouriteVisibility(1);
                db.songDao().updateSaved(1, songData.getId());
                Toast.makeText(ChordsActivity.this, R.string.favourite_add, Toast.LENGTH_SHORT).show();
            }
        });

        favouriteFilledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapFavouriteVisibility(0);
                db.songDao().updateSaved(0, songData.getId());
                Toast.makeText(ChordsActivity.this, R.string.favourite_remove, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
