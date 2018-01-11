package com.vlaovic.matej.jaga;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by matej on 9.1.2018..
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> {

    private List<Song> songList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist, difficulty;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.artist);
            difficulty = (TextView) view.findViewById(R.id.difficulty);
        }
    }

    public MusicListAdapter(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.difficulty.setText(String.valueOf(song.getDifficulty()));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}