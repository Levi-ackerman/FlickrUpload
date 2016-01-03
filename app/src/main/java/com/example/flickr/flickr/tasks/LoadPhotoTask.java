package com.example.flickr.flickr.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Khắc Vỹ on 1/2/2016.
 */
public class LoadPhotoTask extends AsyncTask<String, Void, Bitmap>{
    private OnLoadingPhoto listener;

    public LoadPhotoTask(OnLoadingPhoto listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            listener.onError(e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null){
            listener.onSuccess(bitmap);
        }else{
            listener.onError("Load Photo Failed. Please Try Again");
        }
    }

    public interface OnLoadingPhoto{
        void onSuccess(Bitmap bitmap);

        void onError(String err);
    }
}
