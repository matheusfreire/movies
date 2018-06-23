package com.msf.moveis.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Movie {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("poster_path")
    private String poster;
}
