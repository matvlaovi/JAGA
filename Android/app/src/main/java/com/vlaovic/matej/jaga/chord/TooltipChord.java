package com.vlaovic.matej.jaga.chord;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fenchtose.tooltip.Tooltip;
import com.vlaovic.matej.jaga.R;

public class TooltipChord implements ChordView {

    @Override
    public void showChord(Context context, String chord, View view) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View content = inflater.inflate( R.layout.tooltip_chord, null );

        new Tooltip.Builder(context)
                .anchor(view, Tooltip.RIGHT)
                .autoAdjust(true)
                .content(content)
                .withTip(new Tooltip.Tip(20, 20, R.color.colorPrimary, 4))
                .into((ViewGroup)((Activity)context).findViewById(R.id.chordsActivityRoot))
                .show()

        ;
    }
}
