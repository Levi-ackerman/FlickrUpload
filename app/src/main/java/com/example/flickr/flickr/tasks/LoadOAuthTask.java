package com.example.flickr.flickr.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.flickr.common.Constant;
import com.example.flickr.flickr.FlickrHelper;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.auth.Permission;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;

import java.net.URL;

/**
 * Created by Khắc Vỹ on 1/1/2016.
 */
public class LoadOAuthTask extends AsyncTask<OAuth, Void, String>{
    private static final String TAG = LoadOAuthTask.class.getSimpleName();

    private static final Uri OAUTH_CALLBACK_URI = Uri
            .parse(Constant.CALLBACK_SCHEME + "://oauth");

    private OAuthLoaderListener listener;

    public LoadOAuthTask(OAuthLoaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(OAuth... params) {
        try {
            Flickr f = FlickrHelper.getInstance().getFlickr();
            OAuthToken oauthToken = f.getOAuthInterface().getRequestToken(
                    OAUTH_CALLBACK_URI.toString());
            //saveTokenSecrent(oauthToken.getOauthTokenSecret());
            listener.saveTokenSecret(oauthToken.getOauthTokenSecret());
            URL oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(
                    Permission.WRITE, oauthToken);
            return oauthUrl.toString();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "error:" + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null && !result.startsWith("error")) {
            listener.onSuccess(result);
        } else {
            listener.onError(result);
        }
    }


    public interface OAuthLoaderListener{
        void onSuccess(String result);
        void onError(String err);
        void saveTokenSecret(String tokenSecret);
    }
}
