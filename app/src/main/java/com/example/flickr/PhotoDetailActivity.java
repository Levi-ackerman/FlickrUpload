package com.example.flickr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flickr.photodetail.PhotoDetailPresenter;
import com.example.flickr.photodetail.PhotoDetailView;
import com.example.flickr.ui.TouchImageView;
import com.googlecode.flickrjandroid.photos.Photo;

public class PhotoDetailActivity extends AppCompatActivity implements PhotoDetailView {

    private TouchImageView img;
    private ImageView btnClose;
    private TextView tvTitle;

    private PhotoDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        presenter = new PhotoDetailPresenter();

        initObject();
        initEvent();
    }

    private void initEvent() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initObject() {
        img = (TouchImageView) findViewById(R.id.img);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        tvTitle = (TextView) findViewById(R.id.title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void loadPhoto(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }

    @Override
    public Photo getPhoto() {
        Intent intent = getIntent();
        if (intent != null) {
            Photo photo = (Photo) intent.getSerializableExtra("photo");
            if (photo != null) {
                return photo;
            }
        }
        return null;
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    @Override
    public void showError(String err) {
        Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
    }

}
