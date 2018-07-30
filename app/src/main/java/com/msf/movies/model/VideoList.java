package com.msf.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class VideoList {

    @SerializedName("id")
    private long id;

    @SerializedName("results")
    private List<Video> videos;

}
