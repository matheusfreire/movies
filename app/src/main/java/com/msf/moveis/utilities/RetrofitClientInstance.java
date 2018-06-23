package com.msf.moveis.utilities;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(createGson()))
                    .build();
        }
        return retrofit;
    }

    @NonNull
    private static Gson createGson() {
        return new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
    }
}