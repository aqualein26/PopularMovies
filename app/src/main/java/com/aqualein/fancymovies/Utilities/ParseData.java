package com.aqualein.fancymovies.Utilities;

import android.util.Log;

import com.aqualein.fancymovies.MoviesClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by sandy on 02-Jul-17.
 */

public class ParseData {

    public static ArrayList<MoviesClass> getMoviesObject(String response) {


        ArrayList<MoviesClass> moviesObjectList = new ArrayList<>();


        final String CODE = "cod";
        final String TAG = ParseData.class.getSimpleName();

        if (response != null) {
            try {
                JSONObject baseObject = new JSONObject(response);
                if (baseObject.has(CODE)) {
                    int errorCode = baseObject.getInt(CODE);

                    switch (errorCode) {
                        case HttpURLConnection.HTTP_OK:

                            break;
                        case HttpURLConnection.HTTP_NOT_FOUND:


                            /* Location invalid */
                            return null;
                        default:
                            /* Server probably down */

                            return null;
                    }
                }
                JSONArray jsonArray = baseObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    MoviesClass movieDetails = new MoviesClass();
                    JSONObject movieObject = jsonArray.getJSONObject(i);
                    String posterPath = movieObject.getString("poster_path");
                    String movieTitle = movieObject.getString("title");
                    String userRating = movieObject.getString("vote_average");
                    String releaseDate = movieObject.getString("release_date");
                    String[] year = releaseDate.split("-");
                    String synopsis = movieObject.getString("overview");
                    String id = movieObject.getString("id");

                    movieDetails.setmPosterPath(posterPath);
                    movieDetails.setmReleaseDate(year[0]);
                    movieDetails.setmSynopsis(synopsis);
                    movieDetails.setmTitle(movieTitle);
                    movieDetails.setmRating(userRating);
                    movieDetails.setmId(id);
                    Log.i("rating ", userRating);
                    moviesObjectList.add(movieDetails);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return moviesObjectList;
    }


}
