package com.vlaovic.matej.jaga;


import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.in;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {

    private List<Song> songList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView emptyResult;
    private MusicListAdapter mAdapter;

    private EditText searchView;

    private int saved;

    private AppDatabase db;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        searchView = view.findViewById(R.id.music_search);

        emptyResult = view.findViewById(R.id.empty_view);

        recyclerView = view.findViewById(R.id.music_list_recycler_view);
        mAdapter = new MusicListAdapter(songList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        db = AppDatabase.getAppDatabase(getContext());

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


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 refreshItems();
             }
         });


        refreshItems();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshItems();
    }


    void refreshItems() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Song>> call = apiService.getAllSongs();
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>>call, Response<List<Song>> response) {
                List<Song> songs = response.body();

                db = AppDatabase.getAppDatabase(getContext());

                db.songDao().insertNewSongs(songs);

                onItemsLoadComplete();
            }

            @Override
            public void onFailure(Call<List<Song>>call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        // Load complete

    }

    void onItemsLoadComplete() {
        prepareSongData();
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void prepareSongData() {

        songList.clear();

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

        checkVisibility();

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

        checkVisibility();

        mAdapter.notifyDataSetChanged();
    }

    private void checkVisibility(){

        if(songList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyResult.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyResult.setVisibility(View.GONE);
        }

    }



}
