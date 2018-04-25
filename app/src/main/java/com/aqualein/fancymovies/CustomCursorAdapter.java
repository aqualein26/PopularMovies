package com.aqualein.fancymovies;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.aqualein.PM.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sanditha.a on 21-02-2018.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.ViewHolder> {
    Cursor mCursor;
    Activity mContext;
    ArrayList<String> mImagesList;
    private CustomCursorAdapterOnClickHandler mClick;
    public CustomCursorAdapter(CustomCursorAdapter.CustomCursorAdapterOnClickHandler CustomAdapterOnClick, Cursor cursor, Activity context) {

        Log.i("inside cursor","in1");
        mContext = context;
        mCursor = cursor;
        mClick = CustomAdapterOnClick;

    }

    @Override
    public CustomCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.i("inside cursor","i2");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());


        View imagesView = layoutInflater.inflate(R.layout.item_layout, parent, false);
        CustomCursorAdapter.ViewHolder viewHolder = new CustomCursorAdapter.ViewHolder(imagesView);
        return viewHolder;


    }


    public void swapCursor(Cursor cursor) {


       mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }

    }


    @Override
    public void onBindViewHolder(CustomCursorAdapter.ViewHolder holder, int position) {


        Log.i("inside cursor", "in3");
        int columnName = mCursor.getColumnIndex("url");

        if (mCursor.moveToFirst()) {
            do {

                Picasso.with(mContext)
                        .load(mCursor.getString(columnName))
                        .placeholder(R.drawable.popcorn_placeholder)   // optional
                        .error(R.drawable.error)      // optional
                        .resize(1200, 2000)                     // optional
                        .into(holder.imageView);

            } while (mCursor.moveToNext());

            mCursor.close();
        }
    }

    @Override
    public int getItemCount() {

        return mCursor.getCount();
    }

    public void setAdapterData(ArrayList<String> list) {

        mImagesList = list;
        notifyDataSetChanged();
    }

    public interface CustomCursorAdapterOnClickHandler {

        void click(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);


            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            mClick.click(position);

        }
    }
}
