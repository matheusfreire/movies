package com.msf.movies.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.msf.movies.model.ReviewList;
import com.msf.movies.model.VideoList;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailViewModel extends ViewModel {

    @Getter
    private MutableLiveData<VideoList> liveDataVideos;

    @Getter
    private MutableLiveData<ReviewList> liveDataReview;


    public MovieDetailViewModel(Call<VideoList> callVideos, Call<ReviewList> callReviews) {
        liveDataVideos = new MutableLiveData<>();
        liveDataReview = new MutableLiveData<>();
        callVideos.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                liveDataVideos.postValue(response.body());
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                liveDataVideos.setValue(null);
            }
        });
        callReviews.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                liveDataReview.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                liveDataReview.setValue(null);
            }
        });
    }


}
