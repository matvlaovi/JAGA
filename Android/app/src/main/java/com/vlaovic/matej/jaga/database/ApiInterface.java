package com.vlaovic.matej.jaga.database;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("song")
    Call<List<Song>> getAllSongs();

    @GET("song/{id}")
    Call<Song> getSongById(@Path("id") int id);
}
