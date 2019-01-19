package com.vlaovic.matej.jaga.chord;

import android.content.Context;
import android.widget.Toast;

public class TooltipChord implements ChordView {
    @Override
    public void showChord(Context context, String chord) {
        Toast.makeText(context, "TOOLTIP - " + chord, Toast.LENGTH_SHORT).show();
    }
}
