package com.vlaovic.matej.jaga.chord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        title.setBackgroundColor(getResources().getColor(R.color.colorPureWhite));
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.colorPureBlack));
        title.setTextSize(20);

        builder
                .setCustomTitle(title)
                .setView(view)
        ;

        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Window view=((AlertDialog)dialog).getWindow();
                view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                view.setBackgroundDrawableResource(R.drawable.border_white);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int displayWidth = displayMetrics.widthPixels;
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(((AlertDialog) dialog).getWindow().getAttributes());
                int dialogWindowWidth = (int) (displayWidth * 0.7f);
                layoutParams.width = dialogWindowWidth;
                ((AlertDialog) dialog).getWindow().setAttributes(layoutParams);
            }
        });

        return dialog;
    }
}
