package com.msf.movies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @SerializedName("id")
    @PrimaryKey
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

    @SerializedName("tagline")
    private String tagLine;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("backdrop_path")
    private String backDropImage;

    @Ignore
    private boolean favorited;

    public Movie(long id, String title, String originalTitle, String overview, double voteAverage, String poster, String tagLine, Date releaseDate, String backDropImage) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.poster = poster;
        this.tagLine = tagLine;
        this.releaseDate = releaseDate;
        this.backDropImage = backDropImage;
    }

    @Ignore
    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        poster = in.readString();
        tagLine = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(poster);
        dest.writeString(tagLine);
    }
}
