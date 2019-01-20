package com.vlaovic.matej.jaga.chord;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class PopUpChord implements ChordView{
    @Override
    public void showChord(Context context, String chord, View view) {
        Toast.makeText(context, "POPUP - " + chord, Toast.LENGTH_SHORT).show();
    }
}
