package com.aqualein.fancymovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aqualein.PM.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        String movieReviewsExtra = getIntent().getStringExtra("movieReviewList");

        Gson gson = new Gson();
        ArrayList<MovieReviewResults> movieReviewResults = gson.fromJson(movieReviewsExtra, new TypeToken<List<MovieReviewResults>>() {
        }.getType());


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        for (MovieReviewResults movieReviewObject : movieReviewResults) {
            TextView textView = new TextView(this);

            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setGravity((Gravity.CENTER));
            linearLayout.addView(textView);
            textView.setText(movieReviewObject.getContent());


            TextView textView1 = new TextView(this);
            textView1.setGravity((Gravity.CENTER));
            textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(textView1);
            textView1.setText(movieReviewObject.getAuthor());

        }

    }
}
