package com.msf.movies.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.msf.movies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {


    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> loadFavMovie();

    @Insert
    void insertFavMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> loadMovieById(long id);
}
