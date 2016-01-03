package com.example.flickr.flickr.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.flickr.flickr.FlickrHelper;
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
public class LoadPhotoStreamTask extends AsyncTask<OAuth, Void, PhotoList> {
    private static final String TAG = LoadPhotoStreamTask.class.getSimpleName();

    private OnLoadPhotoStream onLoadPhotoStream;

    public LoadPhotoStreamTask(OnLoadPhotoStream onLoadPhotoStream) {
        this.onLoadPhotoStream = onLoadPhotoStream;
    }

    @Override
    protected void onPreExecute() {
        onLoadPhotoStream.setIsLoading(true);
    }

    @Override
    protected PhotoList doInBackground(OAuth... params) {
        OAuthToken token = params[0].getToken();
        Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getOauthToken(),
                token.getOauthTokenSecret());
        Set<String> extras = new HashSet<String>();
        extras.add("url_sq");
        extras.add("url_l");
        extras.add("views");
        User user = params[0].getUser();
        try {
            return f.getPeopleInterface().getPhotos(user.getId(), extras, 50, 1);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(PhotoList photos) {
        if(photos != null){
            onLoadPhotoStream.onSuccess(photos);
        }else{
            onLoadPhotoStream.onError("Load Photo Failed. Please Try Again");
        }
        onLoadPhotoStream.setIsLoading(false);
    }

    public interface OnLoadPhotoStream{
        void onSuccess(PhotoList photos);
        void onError(String err);
        void setIsLoading(boolean isLoading);
    }
}
