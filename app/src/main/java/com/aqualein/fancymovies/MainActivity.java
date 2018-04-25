package com.aqualein.fancymovies;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import com.aqualein.PM.R;
import com.aqualein.fancymovies.Utilities.FavouritesContractClass;
import com.aqualein.fancymovies.Utilities.NetworkUtility;
import com.aqualein.fancymovies.Utilities.ParseData;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CustomCursorAdapter.CustomCursorAdapterOnClickHandler,ImagesAdapter.ImagesAdapterOnClickHandler,LoaderManager.LoaderCallbacks<Cursor> {

    private final String POPULAR_WEBSITE = "https://api.themoviedb.org/3/movie/popular?api_key=3e60cca493d6bc6d4fc9dbda4c4ea407";
    private final String RATED_WEBSITE = "https://api.themoviedb.org/3/movie/top_rated?api_key=3e60cca493d6bc6d4fc9dbda4c4ea407";
  //  private final String RATED_WEBSITE = "https://api.themoviedb.org/3/movie/top_rated?api_key=ebd331efd1f9bec67a9aa215b256ebe1";

    private ArrayList<String> list = new ArrayList<>();
    ImagesAdapter adapter;
    TextView errorTextView ;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Bundle savedState = null;

    ArrayList<MoviesClass> moviesClassList = new ArrayList<>();
    final String TAG = MainActivity.class.getSimpleName();

CustomCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorTextView = (TextView)findViewById(R.id.tv_error_message);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);



getLoaderManager().initLoader(1,null,this);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
simpleCursorAdapter = new CustomCursorAdapter(this,null,this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImagesAdapter(this);
        recyclerView.setAdapter(adapter);

        this.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (NetworkUtility.isOnline()) {

                        loadData(POPULAR_WEBSITE);

                    } else {

                       // Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_Material_Light_Dialog_Alert);

                        // Setting Dialog Title
                        alertDialog.setTitle(R.string.no_internet_connection);

                        // Setting Dialog Message
                        alertDialog.setMessage(R.string.exit);



                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                // Write your code here to invoke YES event
                                Toast.makeText(getApplicationContext(), R.string.exit_activity, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(),R.string.connection_check, Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                           MainActivity.this.recreate();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }


    });
    }

    private void loadData(String data) {

        showDataView();
        new DownloadTask().execute(data);
    }

    @Override
    public void mClick(int position) {


        Intent intent = new Intent(this, MovieDetails.class);
        MoviesClass currentMovie = moviesClassList.get(position);
        intent.putExtra("current movie", currentMovie);
        startActivity(intent);

    }

    private void showDataView() {

        errorTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showError() {

        errorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public CursorLoader onCreateLoader(int i, Bundle bundle) {

        CursorLoader cursorLoader = new CursorLoader(this,FavouritesContractClass.FavouriteMovies.CONTENT_URI,null,null,null,null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor o) {

     //   simpleCursorAdapter = new CustomCursorAdapter(this,o,this);
simpleCursorAdapter.swapCursor(o);

    }

    @Override
    public void onLoaderReset(Loader loader) {


        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public void click(int position) {

    }

    public class DownloadTask extends AsyncTask<String,Void,ArrayList<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            progressBar.setVisibility(View.INVISIBLE);

            showDataView();
            adapter.setAdapterData(s);
            //  onSaveInstanceState(savedState);

        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String response = null;
            ArrayList<String> imagesList = new ArrayList<>();
            ArrayList<String> uriList = new ArrayList<>();
            if (strings.length == 0)
                return null;





                    try {
                        response = NetworkUtility.makeHttpRequest(strings[0]);
                        if (response == null)
                            Toast.makeText(MainActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
                        moviesClassList = ParseData.getMoviesObject(response);
                        for(int i =0; i< moviesClassList.size();i++)
                            Log.i(TAG, moviesClassList.get(0).getmId());
                        MoviesClass moviesClass = null;
                        for (int i = 0; i < moviesClassList.size(); i++) {



                            moviesClass = moviesClassList.get(i);
Log.i("movie name is",moviesClass.getmTitle());
                            imagesList.add(moviesClass.getmPosterPath());

                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("http")
                                    .appendEncodedPath("/image.tmdb.org/t/p/w185")
                                    .appendEncodedPath(imagesList.get(i)).build();
                            uriList.add(builder.toString());

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return uriList;







        }

    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.popular_sort:
                loadData(POPULAR_WEBSITE);
                return true;
            case R.id.rated_sort:
                loadData(RATED_WEBSITE);
                return true;
            case R.id.fav_sort:
/*try {
    while (cursor.moveToNext()) {
        Log.i("url is ", cursor.getString(cursor.getColumnIndex("url")));
        list.add(cursor.getString(cursor.getColumnIndex("url")));
    }

}finally {
    cursor.close();
}*/

recyclerView.setAdapter(simpleCursorAdapter);
simpleCursorAdapter.notifyDataSetChanged();


                return true;
            default:
                return false;


        }

    }




  /*  class AlertDialogFragment extends AlertDialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Popular Movies");
            alertDialog.setMessage("close activity?");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();
                }
            });

            alertDialog.show();
        }*/
}

