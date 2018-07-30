package com.msf.movies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.msf.movies.model.ReviewList;
import com.msf.movies.model.VideoList;

import retrofit2.Call;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Call<VideoList> mCallVideos;
    private Call<ReviewList> mCallReview;

    public MovieDetailViewModelFactory(Call<VideoList> callVideos, Call<ReviewList> callReview){
        this.mCallReview = callReview;
        this.mCallVideos = callVideos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(mCallVideos,mCallReview);
    }
}
