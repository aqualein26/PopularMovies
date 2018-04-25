package com.aqualein.fancymovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aqualein.PM.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sandy on 28-Jun-17.
 */


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> mImagesList;
    private ImagesAdapterOnClickHandler mImagesAdapterOnClickHandler;

    public ImagesAdapter(ImagesAdapterOnClickHandler ImagesAdapterOnClick) {


        mImagesAdapterOnClickHandler = ImagesAdapterOnClick;

    }

    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);


        View imagesView = layoutInflater.inflate(R.layout.item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(imagesView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ImagesAdapter.ViewHolder holder, int position) {


        Picasso.with(mContext)
                .load(mImagesList.get(position))
                .placeholder(R.drawable.popcorn_placeholder)   // optional
                .error(R.drawable.error)      // optional
                .resize(1200, 2000)                     // optional
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (mImagesList == null) return 0;
        return mImagesList.size();
    }

    public void setAdapterData(ArrayList<String> list) {

        mImagesList = list;
        notifyDataSetChanged();
    }

    public interface ImagesAdapterOnClickHandler {

        void mClick(int position);
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

            mImagesAdapterOnClickHandler.mClick(position);

        }
    }
}