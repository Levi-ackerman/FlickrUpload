package com.example.flickr.photodetail;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.flickr.base.BasePresenter;
import com.example.flickr.flickr.tasks.LoadPhotoTask;
import com.googlecode.flickrjandroid.photos.Photo;

/**
 * Created by Khắc Vỹ on 12/31/2015.
 */
public class PhotoDetailPresenter extends BasePresenter<Photo, PhotoDetailView>{
    @Override
    protected void updateView() {
        if(model != null){
            LoadPhotoTask task = new LoadPhotoTask(listener);
            task.execute(model.getLargeUrl());
            view().setTitle(model.getTitle());
        }
    }

    @Override
    public void bindView(@NonNull PhotoDetailView view) {
        super.bindView(view);
        if(model == null){
            setModel(view().getPhoto());
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
