package com.vlaovic.matej.jaga;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "song", indices={@Index(value="id", unique=true)})
public class Song {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localId")
    private int LocalId;

    @ColumnInfo(name = "id")
    private int Id;

    @ColumnInfo(name = "artist")
    private String Artist;

    @ColumnInfo(name = "title")
    private String Title;

    @ColumnInfo(name = "tabs")
    private String Tabs;

    @ColumnInfo(name = "difficulty")
    private int Difficulty;

    @ColumnInfo(name = "saved")
    private int Saved;

    public Song(int Id, String Artist, String Title, String Tabs, int Difficulty, int Saved) {
        this.Id = Id;
        this.Artist = Artist;
        this.Title = Title;
        this.Tabs = Tabs;
        this.Difficulty = Difficulty;
        this.Saved = Saved;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        this.Title = title;
    }

    public String getArtist() {
        return Artist;
    }
    public void setArtist(String artist) {
        this.Artist = artist;
    }

    public int getDifficulty() {
        return Difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.Difficulty = difficulty;
    }

    public int getSaved() {
        return Saved;
    }
    public void setSaved(int saved) {
        this.Saved = saved;
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        this.Id = id;
    }

    public int getLocalId() {return LocalId; }
    public void setLocalId(int localId) {LocalId = localId; }

    public String getTabs() { return Tabs;}
    public void setTabs(String tabs) { Tabs = tabs;}
}
