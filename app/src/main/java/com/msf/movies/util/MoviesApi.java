package com.msf.movies.util;

import com.msf.movies.model.Movie;
import com.msf.movies.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET("movie/{id}")
    Call<Movie> callDetail(@Path("id") long code, @Query("api_key") String apiKey);

    @GET("movie/popular")
    @Headers({"Accept: application/json"})
    Call<MovieList> callListPopular(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieList> callListTopRated(@Query("api_key") String apiKey);
}
