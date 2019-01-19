package com.vlaovic.matej.jaga.chord;

public class ChordViewFactory {

    public static ChordView getChordView(String chordViewType){

        switch(chordViewType) {
            case "tooltip":
                return new TooltipChord();
            case "popup":
                return new PopUpChord();
            default:
                return null;
        }
    }

}
