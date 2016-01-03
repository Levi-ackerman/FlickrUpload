package com.example.flickr.photos.adapter;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flickr.PhotoDetailActivity;
import com.example.flickr.R;
import com.example.flickr.base.MvpRecyclerListAdapter;
import com.example.flickr.photos.presenter.PhotoPresenter;
import com.example.flickr.photos.view.PhotoViewHolder;
import com.googlecode.flickrjandroid.photos.Photo;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public class CustomPhotoAdapter extends MvpRecyclerListAdapter<Photo, PhotoPresenter, PhotoViewHolder>{
    @NonNull
    @Override
    protected PhotoPresenter createPresenter(@NonNull Photo model) {
        PhotoPresenter photoPresenter = new PhotoPresenter();
        photoPresenter.setModel(model);
        return photoPresenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull Photo model) {
        return model.getId();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_photo_item, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        holder.setListener(new PhotoViewHolder.OnPhotoClickListener() {
            @Override
            public void onPhotoClick(Photo photo) {
                Intent intent = new Intent(parent.getContext(), PhotoDetailActivity.class);
                intent.putExtra("photo", photo);
                parent.getContext().startActivity(intent);
            }
        });

        return holder;
    }
}
