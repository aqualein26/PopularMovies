package com.aqualein.fancymovies.Utilities;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Sanditha.a on 14-02-2018.
 */

public class FavouritesContentProvider extends ContentProvider {


    public static final int MOVIES = 100;
    public static final int MOVIE_ID = 101;
    private static final String DATABASE_NAME = "favourites.db";
    private static final String VERSION = "1";
    public UriMatcher sUriMatcher = buildUriMatcher();
    Cursor cursor;
    private MovieDbOpenHelper dbOpenHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbOpenHelper = new MovieDbOpenHelper(context, DATABASE_NAME, null, 1);
        return true;
    }

    private UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavouritesContractClass.AUTHORITY, FavouritesContractClass.PATH_FAVOURITES, MOVIES);
        uriMatcher.addURI(FavouritesContractClass.AUTHORITY, FavouritesContractClass.PATH_FAVOURITES + "/#", MOVIE_ID);

        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {

            case MOVIES:
                cursor = sqLiteDatabase.query(FavouritesContractClass.FavouriteMovies.TABLE_NAME, strings, s, strings1, null, null, s1);

                break;

            case MOVIE_ID:

                cursor = sqLiteDatabase.query(FavouritesContractClass.FavouriteMovies.TABLE_NAME, strings, s, strings1, null, null, s1);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                // directory
                return "vnd.android.cursor.dir" + "/" + FavouritesContractClass.AUTHORITY + "/" + FavouritesContractClass.FavouriteMovies.CONTENT_URI;
            case MOVIE_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + FavouritesContractClass.AUTHORITY + "/" + FavouritesContractClass.FavouriteMovies.CONTENT_URI;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        switch (match) {
            case MOVIES:
                long id = sqLiteDatabase.insertWithOnConflict(FavouritesContractClass.FavouriteMovies.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(FavouritesContractClass.FavouriteMovies.CONTENT_URI, id);
                else
                    Toast.makeText(getContext(), "Already exists.", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;


    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
