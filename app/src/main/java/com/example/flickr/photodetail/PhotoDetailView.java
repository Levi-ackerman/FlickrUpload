package com.example.flickr.photodetail;

import android.graphics.Bitmap;

import com.googlecode.flickrjandroid.photos.Photo;

/**
 * Created by Khắc Vỹ on 12/31/2015.
 */
public interface PhotoDetailView {
    void loadPhoto(Bitmap bitmap);

    Photo getPhoto();

    void setTitle(String title);

    void showError(String err);
}
