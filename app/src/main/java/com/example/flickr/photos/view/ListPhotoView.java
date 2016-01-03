package com.example.flickr.photos.view;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.photos.PhotoList;

import java.util.List;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public interface ListPhotoView {
    void showPhotos(PhotoList photos);

    void showLoading();

    void hideLoading();

    void showError(String err);

    void showEmpty();

    OAuth loadOAuth();
}
