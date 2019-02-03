package com.vlaovic.matej.jaga.songList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vlaovic.matej.jaga.R;
import com.vlaovic.matej.jaga.songChords.ChordsActivity;
import com.vlaovic.matej.jaga.database.Song;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {

    private List<Song> songList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, artist;
        ImageView difficulty1;
        ImageView difficulty2;
        ImageView difficulty3;

        MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.title);
            artist = view.findViewById(R.id.artist);
            difficulty1 = view.findViewById(R.id.difficulty1);
            difficulty2 = view.findViewById(R.id.difficulty2);
            difficulty3 = view.findViewById(R.id.difficulty3);
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
                .inflate(R.layout.song_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());

        switch(song.getDifficulty()) {
            // Easy
            case 1:
                holder.difficulty1.setVisibility(View.GONE);
                holder.difficulty2.setVisibility(View.GONE);
                break;
            // Medium
            case 2:
                holder.difficulty1.setVisibility(View.GONE);
                holder.difficulty2.setVisibility(View.VISIBLE);
                break;
            // Hard
            case 3:
                holder.difficulty1.setVisibility(View.VISIBLE);
                holder.difficulty2.setVisibility(View.VISIBLE);
                break;
        }

        if(position%2 == 0)
        {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPureBlack));
        }
        else
        {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    private Song getItem(int position) {
        return songList.get(position);
    }
}