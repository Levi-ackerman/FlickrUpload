package com.example.flickr.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

/**
 * Created by Khắc Vỹ on 1/1/2016.
 */
public class OAuthUtils {
    private static final String TAG = OAuthUtils.class.getSimpleName();

    public static OAuth loadOauth(Context context){
        // Restore preferences
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME,
                Context.MODE_PRIVATE);
        String oauthTokenString = settings.getString(Constant.KEY_OAUTH_TOKEN, null);
        String tokenSecret = settings.getString(Constant.KEY_TOKEN_SECRET, null);
        if (oauthTokenString == null && tokenSecret == null) {
            Log.w(TAG, "No Oauth Token retrieved !");
            return null;
        }
        OAuth oauth = new OAuth();
        String userName = settings.getString(Constant.KEY_USER_NAME, null);
        String userId = settings.getString(Constant.KEY_USER_ID, null);
        if (userId != null) {
            User user = new User();
            user.setUsername(userName);
            user.setId(userId);
            oauth.setUser(user);
        }
        OAuthToken oauthToken = new OAuthToken();
        oauth.setToken(oauthToken);
        oauthToken.setOauthToken(oauthTokenString);
        oauthToken.setOauthTokenSecret(tokenSecret);
        Log.d(TAG, String.format("Retrieved token from preference store: oauth token={%s}, " +
                "and token secret={%s}", oauthTokenString, tokenSecret));
        return oauth;
    }

    public static void saveOAuthToken(Context context, String userName, String userId, String token,
                               String tokenSecret) {
        SharedPreferences sp = context.getSharedPreferences(Constant.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.KEY_OAUTH_TOKEN, token);
        editor.putString(Constant.KEY_TOKEN_SECRET, tokenSecret);
        editor.putString(Constant.KEY_USER_NAME, userName);
        editor.putString(Constant.KEY_USER_ID, userId);
        editor.commit();
    }
}
