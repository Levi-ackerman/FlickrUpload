package com.example.flickr;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.flickr.common.OAuthUtils;
import com.example.flickr.photos.adapter.CustomPhotoAdapter;
import com.example.flickr.photos.presenter.ListPhotoPresenter;
import com.example.flickr.photos.view.ListPhotoView;
import com.example.flickr.ui.SpacesItemDecoration;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.photos.PhotoList;

public class ListPhotoActivity extends AppCompatActivity implements ListPhotoView{
    private static final String TAG = ListPhotoActivity.class.getSimpleName();

    private ProgressDialog mDialog;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private CustomPhotoAdapter customAdapter;

    private ListPhotoPresenter listPhotoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listPhotoPresenter = new ListPhotoPresenter();

        initObject();
        initEvent();
    }

    private void initObject(){
        mDialog = new ProgressDialog(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.listPhotos);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mGridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        }
        else{
            mGridLayoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        }
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        customAdapter = new CustomPhotoAdapter();
        mRecyclerView.setAdapter(customAdapter);
    }

    private void initEvent(){
        
    }

    @Override
    public void showPhotos(PhotoList photos) {
        customAdapter.clearAndAddAll(photos);
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        if(mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    public void showError(String err) {
        Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public OAuth loadOAuth() {
        return OAuthUtils.loadOauth(getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        listPhotoPresenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        listPhotoPresenter.unbindView();
    }


}
