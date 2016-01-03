package com.example.flickr.photos.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flickr.R;
import com.example.flickr.base.MvpViewHolder;
import com.example.flickr.photos.presenter.PhotoPresenter;
import com.googlecode.flickrjandroid.photos.Photo;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public class PhotoViewHolder extends MvpViewHolder<PhotoPresenter> implements PhotoView{
    private ImageView imgItem;
    private Context context;
    @Nullable
    private OnPhotoClickListener listener;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        imgItem = (ImageView)itemView.findViewById(R.id.imgItem);
        context = itemView.getContext();

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPhotoClick();
            }
        });
    }

    public void setListener(@Nullable OnPhotoClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void loadPhoto(Bitmap bitmap) {
        imgItem.setImageBitmap(bitmap);
    }

    @Override
    public void gotoPhotoDetail(Photo photo) {
        if(listener != null){
            listener.onPhotoClick(photo);
        }
    }

    @Override
    public void showError(String err) {
        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
    }

    public interface OnPhotoClickListener {
        void onPhotoClick(Photo photo);
    }

}
