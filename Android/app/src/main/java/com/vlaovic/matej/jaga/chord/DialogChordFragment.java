package com.vlaovic.matej.jaga.chord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlaovic.matej.jaga.R;

public class DialogChordFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_chord_view, null);
        Bundle bundle = this.getArguments();
        String chordName = bundle.getString("chordName");

        ImageView chordImg = view.findViewById(R.id.dialog_chord_image);
        int imageSource = getResources().getIdentifier(chordName.toLowerCase() + "_large",
                "drawable", getActivity().getPackageName());
        chordImg.setImageResource(imageSource);

        TextView title = new TextView(getActivity().getBaseContext());
        title.setText(chordName);
        title.setBackgroundColor(getResources().getColor(R.color.colorDarkBackground));
        title.setPadding(10, 30, 10, 30);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.colorPrimaryWhite));
        title.setTextSize(20);

        builder
                .setCustomTitle(title)
                .setView(view);

        return builder.create();
    }
}
