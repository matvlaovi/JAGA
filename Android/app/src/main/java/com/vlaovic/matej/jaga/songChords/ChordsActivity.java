package com.vlaovic.matej.jaga.songChords;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.vlaovic.matej.jaga.R;
import com.vlaovic.matej.jaga.chord.ChordView;
import com.vlaovic.matej.jaga.chord.ChordViewFactory;
import com.vlaovic.matej.jaga.database.AppDatabase;
import com.vlaovic.matej.jaga.database.Song;
import com.vlaovic.matej.jaga.preferences.PreferencesActivity;
import com.vlaovic.matej.jaga.tuner.TunerActivity;
import com.xw.repo.BubbleSeekBar;
import java.util.Objects;
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
        setToolbar();

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

        switch (id) {
            case R.id.action_guitar_tuner:
                Intent tunerIntent = new Intent(this, TunerActivity.class);
                startActivity(tunerIntent);
                break;
            case R.id.action_settings:
                Intent preferenceIntent = new Intent(this, PreferencesActivity.class);
                startActivity(preferenceIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setToolbar(){
        Toolbar t = findViewById(R.id.toolbar);
        t.setTitle(R.string.chords);
        setSupportActionBar(t);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void fillViewWithData(){
        songData = db.songDao().getSongById(songData.getId());

        TextView titleView = findViewById(R.id.title);
        TextView artistView = findViewById(R.id.artist);

        titleView.setText(songData.getTitle());
        artistView.setText(songData.getArtist());

        swapFavouriteVisibility(songData.getSaved());

        setTabsTextView(songData.getTabs());
    }

    private void pauseAutoScroll(){
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);

        svScrolling = false;

        if(svThread != null){
            svThread.interrupt();
        }
    }

    private void startAutoScroll(){
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);

        svScrolling = true;
        startSvScrollThread();
    }

    private void swapFavouriteVisibility(int saved){
        if(saved == 1){
            favouriteBorderBtn.setVisibility(View.GONE);
            favouriteFilledBtn.setVisibility(View.VISIBLE);
        }
        else{
            favouriteBorderBtn.setVisibility(View.VISIBLE);
            favouriteFilledBtn.setVisibility(View.GONE);
        }
    }

    private void setTabsTextView(String text){

        SpannableString spannable = new SpannableString(text);

        String chordRegex = "(?<!\\S)([A-G]|Am|Em|Dm|A7|B7|D7)(?!\\S)";
        Pattern chordPattern = Pattern.compile(chordRegex);
        final Matcher chordMatcher = chordPattern.matcher(text);

        while (chordMatcher.find()) {
            int start = chordMatcher.start();
            int end = chordMatcher.end();

            final String chordName = chordMatcher.group(1);

            ClickableSpan clickSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {

                    // Magically getting the X and Y location of click
                    TextView parentTextView = (TextView) view;
                    Rect parentTextViewRect = new Rect();
                    SpannableString completeText = (SpannableString)(parentTextView).getText();
                    Layout textViewLayout = parentTextView.getLayout();
                    double startOffsetOfClickedText = completeText.getSpanStart(this);
                    double endOffsetOfClickedText = completeText.getSpanEnd(this);
                    double startXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int)startOffsetOfClickedText);
                    double endXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int)endOffsetOfClickedText);
                    int currentLineStartOffset = textViewLayout.getLineForOffset((int)startOffsetOfClickedText);
                    int currentLineEndOffset = textViewLayout.getLineForOffset((int)endOffsetOfClickedText);
                    boolean keywordIsInMultiLine = currentLineStartOffset != currentLineEndOffset;
                    textViewLayout.getLineBounds(currentLineStartOffset, parentTextViewRect);
                    int[] parentTextViewLocation = {0,0};
                    parentTextView.getLocationOnScreen(parentTextViewLocation);
                    double parentTextViewTopAndBottomOffset = (
                            parentTextViewLocation[1] -
                                    parentTextView.getScrollY() +
                                    parentTextView.getCompoundPaddingTop()
                    );
                    parentTextViewRect.top += parentTextViewTopAndBottomOffset;
                    parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;
                    parentTextViewRect.left += (
                            parentTextViewLocation[0] +
                                    startXCoordinatesOfClickedText +
                                    parentTextView.getCompoundPaddingLeft() -
                                    parentTextView.getScrollX()
                    );
                    parentTextViewRect.right = (int) (
                            parentTextViewRect.left +
                                    endXCoordinatesOfClickedText -
                                    startXCoordinatesOfClickedText
                    );
                    int x = (parentTextViewRect.left + parentTextViewRect.right) / 2;
                    int y = parentTextViewRect.bottom;
                    if (keywordIsInMultiLine) {
                        x = parentTextViewRect.left;
                    }

                    View v = new View(ChordsActivity.this);

                    FrameLayout root = findViewById(R.id.frameLayoutRoot);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1, 1);
                    params.leftMargin = (int) (x + (0 * ChordsActivity.this.getResources().getDisplayMetrics().density));
                    params.topMargin  = (int)(y - (40 * ChordsActivity.this.getResources().getDisplayMetrics().density));
                    root.addView(v, params);

                    pauseAutoScroll();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChordsActivity.this);
                    Integer chordViewType = Integer.parseInt(preferences.getString("chord_type", null));

                    ChordView chordView = ChordViewFactory.getChordView(chordViewType);
                    chordView.showChord(ChordsActivity.this, chordName, v);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };

            spannable.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPureWhite)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        String bracketsRegex = "\\[(.*?)]";
        Pattern bracketsPattern = Pattern.compile(bracketsRegex);
        final Matcher bracketsMatcher = bracketsPattern.matcher(text);

        while (bracketsMatcher.find()) {
            int start = bracketsMatcher.start();
            int end = bracketsMatcher.end();

            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorLightBlue)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        TextView tabsView = findViewById(R.id.tabs);
        tabsView.setText(spannable, TextView.BufferType.SPANNABLE);
        tabsView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startSvScrollThread(){
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
                        Thread.sleep(21 - svInterval);
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

    private void setSvListeners(){
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

    private void setSeekBarListeners(){
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

    private void setPlayPauseListeners(){
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

    private void setFavouriteButtonsListeners(){
        favouriteBorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapFavouriteVisibility(1);
                db.songDao().updateSaved(1, songData.getId());

                Snackbar
                        .make(view, R.string.favourite_add, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                swapFavouriteVisibility(0);
                                db.songDao().updateSaved(0, songData.getId());
                            }
                        })
                        .show();
            }
        });

        favouriteFilledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapFavouriteVisibility(0);
                db.songDao().updateSaved(0, songData.getId());

                Snackbar
                        .make(view, R.string.favourite_remove, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                swapFavouriteVisibility(1);
                                db.songDao().updateSaved(1, songData.getId());
                            }
                        })
                        .show();
            }
        });
    }
}
