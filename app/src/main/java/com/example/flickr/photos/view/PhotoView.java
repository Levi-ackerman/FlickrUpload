package com.example.flickr.photos.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.googlecode.flickrjandroid.photos.Photo;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public interface PhotoView {
    void loadPhoto(Bitmap bitmap);

    void gotoPhotoDetail(Photo photo);

    void showError(String err);
}
