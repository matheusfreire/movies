package com.msf.movies.model;


import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Video {

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;


}
