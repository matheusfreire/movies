package com.msf.moveis.utilities;

import com.msf.moveis.model.Movie;
import com.msf.moveis.model.MovieList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET("movie/{id}")
    Call<Movie> callDetail(@Path("id") int code, @Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieList> callListPopular(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<List<Movie>> callListTopRated(@Query("api_key") String apiKey);
}
