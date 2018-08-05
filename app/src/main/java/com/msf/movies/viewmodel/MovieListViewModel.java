package com.msf.movies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.msf.movies.db.MovieDatabase;
import com.msf.movies.model.Movie;
import com.msf.movies.model.MovieList;

import java.util.List;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListViewModel extends AndroidViewModel {

    @Getter
    private MutableLiveData<List<Movie>> multableLiveDataListMovie;

    @Getter
    private LiveData<List<Movie>> liveDataMovies;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        if (multableLiveDataListMovie == null) {
            multableLiveDataListMovie = new MutableLiveData();
        }
    }

    public void getMovies(Call<MovieList> callMovies){
        if(isOnline()){
            callMovies.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    multableLiveDataListMovie.postValue(response.body().getResults());
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    multableLiveDataListMovie.postValue(null);
                }

            });
        } else {
            multableLiveDataListMovie.postValue(null);
        }
    }

    public void getFavMovies(){
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        liveDataMovies = database.movieDao().loadFavMovie();
    }

    private boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
