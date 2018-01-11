package com.vlaovic.matej.jaga;


import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {

    private List<Song> songList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MusicListAdapter mAdapter;

    private EditText searchView;

    private int saved;

    private AppDatabase db;

    public MusicListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            saved = bundle.getInt("saved", 0);
        }

        final View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        searchView = (EditText)view.findViewById(R.id.music_search);

        recyclerView = (RecyclerView) view.findViewById(R.id.music_list_recycler_view);
        mAdapter = new MusicListAdapter(songList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                filterSongData();

            }
        });

        prepareSongData();

        return view;
    }

    private void prepareSongData() {
        ArrayList<Song> songs = new ArrayList<Song>();
        songs.add(new Song("American Pie","SAVED",1, 1));
        songs.add(new Song("Lose Yourself","SAVED",2, 1));
        songs.add(new Song("Throw It Up","NOT SAVED",3, 0));
        songs.add(new Song("Bohemian Rhapsody","NOT SAVED",4, 0));
        songs.add(new Song("I Want It All","NOT SAVED",5, 0));

        db.songDao().insertSongs(songs);

        switch(saved){
            case 0:
                songList.addAll(db.songDao().getAll());
                break;
            case 1:
                songList.addAll(db.songDao().getAllSaved());
                break;
            default:
                songList.addAll(db.songDao().getAll());
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void filterSongData() {

        songList.clear();

        switch(saved){
            case 0:
                songList.addAll(db.songDao().searchAll('%' + searchView.getText().toString() + '%'));
                break;
            case 1:
                songList.addAll(db.songDao().searchAllSaved('%' + searchView.getText().toString() + '%'));
                break;
            default:
                songList.addAll(db.songDao().searchAll('%' + searchView.getText().toString() + '%'));
                break;
        }
        mAdapter.notifyDataSetChanged();
    }



}
