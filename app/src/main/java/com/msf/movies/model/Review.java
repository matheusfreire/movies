package com.msf.movies.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Review {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;
}
