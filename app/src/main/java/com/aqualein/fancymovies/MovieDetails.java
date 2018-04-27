package com.aqualein.fancymovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aqualein.PM.R;
import com.aqualein.fancymovies.Utilities.FavouritesContractClass;
import com.aqualein.fancymovies.Utilities.MovieDbOpenHelper;
import com.aqualein.fancymovies.Utilities.NetworkUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieDetails extends AppCompatActivity implements View.OnClickListener {

    public final String trailerBaseUri = "https://api.themoviedb.org/3/movie/";
    String movieId;
    Uri movieFinalUri, movieReviewUri;
    ArrayList<MovieTrailerClass> movieTrailersList;
    String movieReviewsList;
    Uri.Builder builder;
    MoviesClass currentMovie;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        TextView title = (TextView) findViewById(R.id.title_tv);
        TextView synopsis = (TextView) findViewById(R.id.synopsis_tv);
        TextView userRating = (TextView) findViewById(R.id.rating_tv);
        TextView date = (TextView) findViewById(R.id.date_tv);
        ImageView imageView = (ImageView) findViewById(R.id.poster_thumbnail);
        Intent intent = getIntent();
        ImageView trailer1 = (ImageView) findViewById(R.id.trailer1);
        ImageView trailer2 = (ImageView) findViewById(R.id.trailer2);

        trailer1.setOnClickListener(this);
        trailer2.setOnClickListener(this);

        if (intent.hasExtra("current movie")) {


            currentMovie = intent.getParcelableExtra("current movie");

            builder = new Uri.Builder();
            builder.scheme("http")
                    .appendEncodedPath("/image.tmdb.org/t/p/w185")
                    .appendEncodedPath(currentMovie.getmPosterPath()).build();

            movieId = currentMovie.getmId();

            title.setText(currentMovie.getmTitle());
            synopsis.setText(currentMovie.getmSynopsis());

            userRating.setText(currentMovie.getmRating() + "/10");

            date.setText(currentMovie.getmReleaseDate());

            movieFinalUri = Uri.parse(trailerBaseUri).buildUpon().appendPath(movieId).appendPath("videos").appendQueryParameter("api_key", "ebd331efd1f9bec67a9aa215b256ebe1")
                    .appendQueryParameter("language", "en").build();

            Picasso.with(getApplicationContext())
                    .load(builder.toString())
                    .placeholder(R.drawable.popcorn_placeholder)
                    .error(R.drawable.error)
                    .resize(600, 1000)
                    .into(imageView);

            movieTrailersList = new ArrayList<>();
            DownloadTask task = new DownloadTask();
            task.execute(movieFinalUri.toString());


            movieReviewUri = Uri.parse(trailerBaseUri).buildUpon().appendPath(movieId).appendPath("reviews").appendQueryParameter("api_key", "ebd331efd1f9bec67a9aa215b256ebe1")
                    .appendQueryParameter("language", "en").build();
            ReviewDownloadTask task1 = new ReviewDownloadTask();
            task1.execute(movieReviewUri.toString());




        }
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.trailer1:

                openTrailer(movieTrailersList.get(0).getKey());
                break;

            case R.id.rating_tv:
                Intent intent = new Intent(MovieDetails.this, ReviewActivity.class);
                intent.putExtra("movieReviewList", movieReviewsList);
                startActivity(intent);


        }
    }

    public void openTrailer(String key) {


        Uri uri = Uri.parse("https://www.youtube.com/watch?").buildUpon().appendQueryParameter("v", key).build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);


    }

    public void markAsFavourite(View view) {


        ContentValues contentValues = new ContentValues();

        contentValues.put(FavouritesContractClass.FavouriteMovies.MOVIE_URL, builder.toString());
        contentValues.put(FavouritesContractClass.FavouriteMovies.MOVIE_ID, movieId);

        Uri uri = getContentResolver().insert(FavouritesContractClass.FavouriteMovies.CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(this, R.string.movie_saved_toast, Toast.LENGTH_SHORT).show();

        } else
            Log.i("insert not successsful", "suc");

    }



    class DownloadTask extends AsyncTask<String, Void, ArrayList<MovieTrailerClass>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected ArrayList<MovieTrailerClass> doInBackground(String... strings) {


            try {

                String response = NetworkUtility.makeHttpRequest(movieFinalUri.toString());
                if (response != null) {
                    JSONObject baseObj = new JSONObject(response);
                    JSONArray resObject = baseObj.getJSONArray("results");


                    Gson gson = new Gson();

                    movieTrailersList = gson.fromJson(resObject.toString(), new TypeToken<List<MovieTrailerClass>>() {
                    }.getType());

                } else {

                    Toast.makeText(MovieDetails.this, R.string.error_message, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieTrailersList;

        }

    }

    public class ReviewDownloadTask extends AsyncTask<String, Void, String> {

        String response = null;

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            ArrayList<MovieReviewResults> movieReviewResults = gson.fromJson(movieReviewsList, new TypeToken<List<MovieReviewResults>>() {
            }.getType());


            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

            for (MovieReviewResults movieReviewObject : movieReviewResults) {
                TextView textView = new TextView(getApplicationContext());

                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity((Gravity.CENTER));
                textView.setPadding(8,8,8,8);
                linearLayout.addView(textView);
                textView.setTextColor(getResources().getColor(android.R.color.black));
                textView.setText(movieReviewObject.getContent());


                TextView textView1 = new TextView(getApplicationContext());
                textView1.setGravity((Gravity.CENTER));
                textView1.setTextColor(getResources().getColor(android.R.color.black));
                textView1.setPadding(8,8,8,24);
                textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(textView1);
                textView1.setText(movieReviewObject.getAuthor());

                View v = new View(getApplicationContext());
                v.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        5
                ));
                v.setBackgroundColor(Color.parseColor("#000000"));

                linearLayout.addView(v);
            }

        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                response = NetworkUtility.makeHttpRequest(strings[0]);
                if (response != null) {
                    JSONObject baseObj = new JSONObject(response);
                    JSONArray resObject = baseObj.getJSONArray("results");
                    movieReviewsList = resObject.toString();


                } else {

                    Toast.makeText(MovieDetails.this, R.string.error_message, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieReviewsList;

        }
    }


}








