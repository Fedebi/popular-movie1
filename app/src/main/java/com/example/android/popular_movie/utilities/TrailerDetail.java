package com.example.android.popular_movie.utilities;

public class TrailerDetail {

    private String name;
    private String source;

    public TrailerDetail(String name, String source) {

        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSource(String source) {
        this.source = source;
    }
}