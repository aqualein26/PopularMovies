package com.aqualein.fancymovies;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aqualein.PM.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.ViewHolder> {
    Cursor mCursor;
    Activity mContext;
    ArrayList<String> mImagesList;
    private CustomCursorAdapterOnClickHandler mClick;

    public CustomCursorAdapter(CustomCursorAdapter.CustomCursorAdapterOnClickHandler CustomAdapterOnClick, Cursor cursor, Activity context) {


        mContext = context;
        mCursor = cursor;
        mClick = CustomAdapterOnClick;

    }

    @Override
    public CustomCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


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



        int columnName = mCursor.getColumnIndex("url");


        if (mCursor != null) {


            mCursor.moveToPosition(position);

            Picasso.with(mContext)
                    .load(mCursor.getString(columnName))
                    .placeholder(R.drawable.popcorn_placeholder)   // optional
                    .error(R.drawable.error)      // optional
                    .resize(1200, 2000)                     // optional
                    .into(holder.imageView);


        }
    }

    @Override
    public int getItemCount() {

        return mCursor.getCount();
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
