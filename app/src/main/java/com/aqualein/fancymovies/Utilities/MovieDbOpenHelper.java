package com.aqualein.fancymovies.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sanditha.a on 14-02-2018.
 */

public class MovieDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";
    private static final String VERSION = "1";

    public MovieDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE IF NOT EXISTS " + FavouritesContractClass.FavouriteMovies.TABLE_NAME +
                "(" + FavouritesContractClass.FavouriteMovies.MOVIE_URL + " TEXT NOT NULL ," + FavouritesContractClass.FavouriteMovies.MOVIE_ID + " VARCHAR NOT NULL,  UNIQUE( " + FavouritesContractClass.FavouriteMovies.MOVIE_ID  + ") ON CONFLICT REPLACE )";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
