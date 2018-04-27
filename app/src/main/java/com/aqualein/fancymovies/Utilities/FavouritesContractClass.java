package com.aqualein.fancymovies.Utilities;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sanditha.a on 14-02-2018.
 */

public final class FavouritesContractClass {


    public static final String AUTHORITY = "com.aqualein.popularmovies.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITES = "favourites";

    private FavouritesContractClass() {
    }

    public static final class FavouriteMovies implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();
        public static final String TABLE_NAME = "favourites";
        public static final String MOVIE_URL = "url";
        public static final String MOVIE_ID = "movie_id";


    }


}
