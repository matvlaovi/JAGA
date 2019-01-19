package com.vlaovic.matej.jaga.songList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vlaovic.matej.jaga.R;
import com.vlaovic.matej.jaga.songChords.ChordsActivity;
import com.vlaovic.matej.jaga.database.Song;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {

    private List<Song> songList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, artist, difficulty;

        MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.title);
            artist = view.findViewById(R.id.artist);
            difficulty = view.findViewById(R.id.difficulty);
        }

        @Override
        public void onClick(View view) {
            Song song = getItem(this.getAdapterPosition());

            Intent intentBundle = new Intent(context, ChordsActivity.class);
            intentBundle.putExtra("songTag", song);

            context.startActivity(intentBundle);
        }
    }

    SongListAdapter(List<Song> songList, Context context) {
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

    private Song getItem(int position) {
        return songList.get(position);
    }
}