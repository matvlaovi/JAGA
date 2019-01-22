package com.vlaovic.matej.jaga.chord;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fenchtose.tooltip.Tooltip;
import com.fenchtose.tooltip.TooltipAnimation;
import com.vlaovic.matej.jaga.R;

public class TooltipChord implements ChordView {

    @Override
    public void showChord(Context context, String chordName, View view) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.tooltip_chord_view, null);

        ImageView chordImg = content.findViewById(R.id.chordTooltipViewImage);
        int imageSource = context.getResources().getIdentifier(chordName.toLowerCase() + "_small",
                "drawable", context.getPackageName());
        chordImg.setImageResource(imageSource);

        new Tooltip.Builder(context)
                .anchor(view, Tooltip.RIGHT)
                .animate(new TooltipAnimation(TooltipAnimation.FADE, 400))
                .autoAdjust(true)
                .content(content)
                .withTip(new Tooltip.Tip(20, 20, R.color.colorAccent, 5))
                .into((ViewGroup)((Activity) context).findViewById(R.id.chordsActivityRoot))
                .show()

        ;
    }
}
