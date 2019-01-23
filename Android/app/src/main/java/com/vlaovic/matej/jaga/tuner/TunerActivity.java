package com.vlaovic.matej.jaga.tuner;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.vlaovic.matej.jaga.R;

import java.util.Objects;

public class TunerActivity extends AppCompatActivity {

    MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        setToolbar();
        setAudioButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setToolbar(){
        Toolbar t = findViewById(R.id.toolbar);
        t.setTitle("Tuner");
        setSupportActionBar(t);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setAudioButtons(){
        // E1
        Button btE1 = this.findViewById(R.id.tune_e_1);
        btE1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playSound(TunerStringTypes.E_1);
            }
        });

        // B2
        Button btB2 = this.findViewById(R.id.tune_b_2);
        btB2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playSound(TunerStringTypes.B_2);
            }
        });

        // G3
        Button btG3 = this.findViewById(R.id.tune_g_3);
        btG3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playSound(TunerStringTypes.G_3);
            }
        });

        // D4
        Button btD4 = this.findViewById(R.id.tune_d_4);
        btD4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playSound(TunerStringTypes.D_4);
            }
        });

        // A5
        Button btA5 = this.findViewById(R.id.tune_a_5);
        btA5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playSound(TunerStringTypes.A_5);
            }
        });

        // E6
        Button btE6 = this.findViewById(R.id.tune_e_6);
        btE6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                playSound(TunerStringTypes.E_6);
            }
        });
    }

    private void playSound(Integer type) {

        if (player != null) {
            player.reset();
            player.release();
        }

        int audioId;

        switch(type) {
            case TunerStringTypes.E_1:
                audioId = R.raw.e_1;
                break;
            case TunerStringTypes.B_2:
                audioId = R.raw.b_2;
                break;
            case TunerStringTypes.G_3:
                audioId = R.raw.g_3;
                break;
            case TunerStringTypes.D_4:
                audioId = R.raw.d_4;
                break;
            case TunerStringTypes.A_5:
                audioId = R.raw.a_5;
                break;
            case TunerStringTypes.E_6:
                audioId = R.raw.e_6;
                break;
            default:
                audioId = 0;
        }

        if(audioId != 0){
            player = MediaPlayer.create(this, audioId);
            player.start();
        }
    }

}
