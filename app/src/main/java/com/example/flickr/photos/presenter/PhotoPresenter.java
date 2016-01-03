package com.example.flickr.photos.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.flickr.base.BasePresenter;
import com.example.flickr.flickr.tasks.LoadPhotoTask;
import com.example.flickr.photos.view.PhotoView;
import com.googlecode.flickrjandroid.photos.Photo;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public class PhotoPresenter extends BasePresenter<Photo, PhotoView>{
    @Override
    protected void updateView() {
        //view().loadPhoto(model.getLargeSquareUrl());
        LoadPhotoTask task = new LoadPhotoTask(listener);
        task.execute(model.getLargeSquareUrl());
    }

    public void onPhotoClick(){
        if(setUpDone()){
            view().gotoPhotoDetail(model);
        }
    }

    private LoadPhotoTask.OnLoadingPhoto listener = new LoadPhotoTask.OnLoadingPhoto() {
        @Override
        public void onSuccess(Bitmap bitmap) {
            view().loadPhoto(bitmap);
        }

        @Override
        public void onError(String err) {
            view().showError(err);
        }
    };
}
