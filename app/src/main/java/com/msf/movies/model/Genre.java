package com.msf.movies.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Genre {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

}
