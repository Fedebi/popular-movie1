package com.example.android.popular_movie;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetail implements Parcelable{

    private String poster_path;
    private String original_title;
    private String overview;
    private String vote_average;
    private String release_date;


    public MovieDetail(String poster_path,String original_title,String overview, String vote_average, String release_date){

        this.poster_path = poster_path ;
        this.original_title=original_title;
        this.overview=overview;
        this.vote_average=vote_average;
        this.release_date=release_date;
    }


    public String getPoster_path() {return poster_path;}

    public String getOriginal_title() {
        return original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }


    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }


    public MovieDetail(Parcel in) {
        poster_path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}