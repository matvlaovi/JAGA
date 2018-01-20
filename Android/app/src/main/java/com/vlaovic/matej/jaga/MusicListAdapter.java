package com.vlaovic.matej.jaga;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> {

    private List<Song> songList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, artist, difficulty;


        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.title);
            artist = view.findViewById(R.id.artist);
            difficulty = view.findViewById(R.id.difficulty);
        }

        @Override
        public void onClick(View view) {
            Song song = getItem(this.getAdapterPosition());

            Intent intentBundle = new Intent(context, TabControlActivity.class);
            intentBundle.putExtra("songTag", song);

            context.startActivity(intentBundle);

        }
    }


    public MusicListAdapter(List<Song> songList, Context context) {

        this.songList = songList;
        this.context = context;
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

    public Song getItem(int position) {
        return songList.get(position);
    }
}