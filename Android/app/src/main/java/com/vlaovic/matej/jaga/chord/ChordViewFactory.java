package com.vlaovic.matej.jaga.chord;

public class ChordViewFactory {

    public static ChordView getChordView(Integer chordViewType){

        switch(chordViewType) {
            case ChordViewTypes.TOOLTIP:
                return new TooltipChord();
            case ChordViewTypes.DIALOG:
                return new DialogChord();
            default:
                return null;
        }
    }

}
