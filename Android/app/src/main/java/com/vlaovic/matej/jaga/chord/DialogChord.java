package com.vlaovic.matej.jaga.chord;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class DialogChord implements ChordView{
    @Override
    public void showChord(Context context, String chordName, View view) {
        DialogFragment fragment = new DialogChordFragment();

        Bundle bundle = new Bundle();
        bundle.putString("chordName", chordName);
        fragment.setArguments(bundle);

        fragment.show(((Activity)context).getFragmentManager(), "chordDialogView");
    }
}
