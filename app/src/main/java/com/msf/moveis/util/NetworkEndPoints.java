package com.msf.moveis.util;

public enum NetworkEndPoints {

    IMAGE_API {
        @Override
        public String getUrl() {
            return "http://image.tmdb.org/t/p/w185/";
        }
    },
    MOVIES_API {
        @Override
        public String getUrl() {
            return "https://api.themoviedb.org/3/";
        }
    };

    public abstract String getUrl();
}
