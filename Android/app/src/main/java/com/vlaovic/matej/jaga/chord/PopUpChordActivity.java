package com.vlaovic.matej.jaga.chord;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.vlaovic.matej.jaga.R;

public class PopUpChordActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_chord_view);
        getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        String chordName = getIntent().getStringExtra("chordName");

        ImageView chordImg = findViewById(R.id.popup_chord_image);
        int imageSource = getResources().getIdentifier(chordName.toLowerCase() + "_large",
                "drawable", getPackageName());
        chordImg.setImageResource(imageSource);
    }
}
