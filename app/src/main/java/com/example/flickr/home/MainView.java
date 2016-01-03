package com.example.flickr.home;

import android.graphics.Bitmap;
import android.net.Uri;

import com.googlecode.flickrjandroid.oauth.OAuth;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public interface MainView {
    void showUnauthenticatedMessage();

    void hideUnauthenticatedMessage();

    void showLoading();

    void hideLoading();

    void showSelectDialog();

    void hideSelectDialog();

    void showSelectPhotoButton();

    void hideSelectPhotoButton();

    void openGallery();

    void openCamera();

    void setPhoto(Bitmap bitmap);

    void showError(String e);

    OAuth loadOAuth();

    boolean isAuthenticated();

    void saveOAuthToken(String userName, String userId, String token, String tokenSecret);

    void openBrowserView(String url);

    String getOAuthCallbackString();

    void oauthDone(OAuth oAuth);

    void setMenuVisibility(boolean isShow);

    void goToListPhotos();

    void showUploadSuccess();

    void showUploadError();

    void clearPreference();
}
