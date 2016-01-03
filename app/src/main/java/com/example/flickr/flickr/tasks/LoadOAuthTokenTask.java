package com.example.flickr.flickr.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.flickr.flickr.FlickrHelper;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

/**
 * Created by Khắc Vỹ on 1/1/2016.
 */
public class LoadOAuthTokenTask extends AsyncTask<String, Void, OAuth>{
    private static final String TAG = OAuthTokenLoaderListener.class.getSimpleName();

    private OAuthTokenLoaderListener listener;

    public LoadOAuthTokenTask(OAuthTokenLoaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected OAuth doInBackground(String... params) {
        String oauthToken = params[0];
        String oauthTokenSecret = params[1];
        String verifier = params[2];

        Flickr f = FlickrHelper.getInstance().getFlickr();
        OAuthInterface oauthApi = f.getOAuthInterface();
        try {
            return oauthApi.getAccessToken(oauthToken, oauthTokenSecret,
                    verifier);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(OAuth oAuth) {
        if(oAuth != null){
            User user = oAuth.getUser();
            OAuthToken token = oAuth.getToken();
            if (user == null || user.getId() == null || token == null
                    || token.getOauthToken() == null
                    || token.getOauthTokenSecret() == null) {
                listener.onError("Authorization failed");
            }else{
                listener.onSuccess(oAuth);
            }
        }else{
            listener.onError("Authorization failed");
        }
    }

    public interface OAuthTokenLoaderListener{
        void onSuccess(OAuth oAuth);
        void onError(String err);
    }
}
