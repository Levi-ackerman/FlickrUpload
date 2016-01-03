package com.example.flickr.photos.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.flickr.base.BasePresenter;
import com.example.flickr.flickr.FlickrHelper;
import com.example.flickr.flickr.tasks.LoadPhotoStreamTask;
import com.example.flickr.photos.view.ListPhotoView;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public class ListPhotoPresenter extends BasePresenter<PhotoList, ListPhotoView> {
    private boolean isLoadingData = false;

    @Override
    protected void updateView() {
        //business logic in this presenter
        if (model.size() == 0) {
            view().showEmpty();
        } else {
            view().showPhotos(model);
        }
    }

    @Override
    public void bindView(@NonNull ListPhotoView view) {
        super.bindView(view);
        //let's not reload data if it's already present
        if (model == null && !isLoadingData) {
            if (view().loadOAuth() != null) {
                view.showLoading();
                new LoadPhotoStreamTask(listener).execute(view().loadOAuth());
            } else {
                view().showEmpty();
            }
        }
    }

    private LoadPhotoStreamTask.OnLoadPhotoStream listener = new LoadPhotoStreamTask.OnLoadPhotoStream() {
        @Override
        public void onSuccess(PhotoList photos) {
            setModel(photos);
            view().hideLoading();
        }

        @Override
        public void onError(String err) {
            view().hideLoading();
            view().showError(err);
        }

        @Override
        public void setIsLoading(boolean isLoading) {
            isLoadingData = isLoading;
        }
    };

}
