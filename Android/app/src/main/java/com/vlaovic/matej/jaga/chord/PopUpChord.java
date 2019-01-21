package com.vlaovic.matej.jaga.chord;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class PopUpChord implements ChordView{
    @Override
    public void showChord(Context context, String chordName, View view) {
        Intent intent = new Intent(context, PopUpChordActivity.class);
        intent.putExtra("chordName", chordName);
        context.startActivity(intent);
    }
}
