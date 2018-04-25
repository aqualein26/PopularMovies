package com.aqualein.fancymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sandy on 28-Jun-17.
 */

public class MoviesClass implements Parcelable {

    public static final Creator<MoviesClass> CREATOR = new Creator<MoviesClass>() {
        @Override
        public MoviesClass createFromParcel(Parcel in) {
            return new MoviesClass(in);
        }

        @Override
        public MoviesClass[] newArray(int size) {
            return new MoviesClass[size];
        }
    };
    String mPosterPath = null;
    String mTitle = null;
    String mSynopsis = null;
    String mRating = null;
    String mId = null;
    String mReleaseDate = null;

    public MoviesClass() {
    }

    private MoviesClass(Parcel in) {

        mPosterPath = in.readString();
        mTitle = in.readString();
        mSynopsis = in.readString();
        mRating = in.readString();
        mReleaseDate = in.readString();
        mId = in.readString();
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public void setmSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mPosterPath);
        parcel.writeString(mTitle);
        parcel.writeString(mSynopsis);
        parcel.writeString(mRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mId);
    }


}
