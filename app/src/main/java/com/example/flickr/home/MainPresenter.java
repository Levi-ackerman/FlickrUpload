package com.example.flickr.home;


import com.example.flickr.flickr.tasks.LoadOAuthTask;
import com.example.flickr.flickr.tasks.LoadOAuthTokenTask;
import com.example.flickr.flickr.tasks.UploadPhotoTask;
import com.googlecode.flickrjandroid.oauth.OAuth;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public class MainPresenter {
    private MainView mainView;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    public void loadContent() {
        if (!mainView.isAuthenticated()) {
            mainView.showUnauthenticatedMessage();
            mainView.setMenuVisibility(false);
        } else {
            mainView.hideUnauthenticatedMessage();
            mainView.setMenuVisibility(true);
        }
    }

    public void onSignInButtonClick() {
        mainView.showLoading();
        LoadOAuthTask loadOAuthTask = new LoadOAuthTask(oAuthLoaderListener);
        loadOAuthTask.execute(mainView.loadOAuth());
    }

    public void onSelectButtonClick() {
        mainView.showSelectDialog();
    }

    public void onUploadMenuClick(String fileName) {
        UploadPhotoTask task = new UploadPhotoTask(onUploadComplete, fileName);
        mainView.showLoading();
        task.execute(mainView.loadOAuth());
    }

    public void onLogoutMenuClick(){
        mainView.clearPreference();
        loadContent();
    }

    public void onListPhotoMenuClick(){
        mainView.goToListPhotos();
    }

    public void onPhotoSelected(){
        mainView.hideSelectPhotoButton();
    }

    public void onStop(){
        mainView.setPhoto(null);
        mainView.showSelectPhotoButton();
    }

    public void onSelectCamera() {
        mainView.hideSelectDialog();
        mainView.openCamera();
    }

    public void onSelectGallery() {
        mainView.hideSelectDialog();
        mainView.openGallery();
    }

    public void onAuthenticationCallback() {
        String callbackString = mainView.getOAuthCallbackString();
        if (callbackString != null) {
            String[] data = callbackString.split("&");
            if (data != null && data.length == 2) {
                String oauthToken = data[0].substring(data[0].indexOf("=") + 1);
                String oauthVerifier = data[1]
                        .substring(data[1].indexOf("=") + 1);
                if (mainView.loadOAuth() != null && mainView.loadOAuth().getToken() != null
                        && mainView.loadOAuth().getToken().getOauthTokenSecret() != null) {
                    LoadOAuthTokenTask task = new LoadOAuthTokenTask(oAuthTokenLoaderListener);
                    task.execute(oauthToken, mainView.loadOAuth().getToken()
                            .getOauthTokenSecret(), oauthVerifier);
                }
            }
        }
    }

    private LoadOAuthTask.OAuthLoaderListener oAuthLoaderListener = new LoadOAuthTask.OAuthLoaderListener() {
        @Override
        public void onSuccess(String result) {
            mainView.hideLoading();
            mainView.openBrowserView(result);
        }

        @Override
        public void onError(String err) {
            mainView.hideLoading();
            mainView.showError(err);
        }

        @Override
        public void saveTokenSecret(String tokenSecret) {
            mainView.saveOAuthToken(null, null, null, tokenSecret);
        }
    };

    private LoadOAuthTokenTask.OAuthTokenLoaderListener oAuthTokenLoaderListener
            = new LoadOAuthTokenTask.OAuthTokenLoaderListener() {
        @Override
        public void onSuccess(OAuth oAuth) {
            mainView.oauthDone(oAuth);
            mainView.hideUnauthenticatedMessage();
            mainView.setMenuVisibility(true);
            mainView.hideLoading();
        }

        @Override
        public void onError(String err) {
            mainView.hideLoading();
            mainView.showError(err);
        }
    };

    private UploadPhotoTask.OnUploadComplete onUploadComplete = new UploadPhotoTask.OnUploadComplete() {
        @Override
        public void onSuccess() {
            mainView.hideLoading();
            mainView.showUploadSuccess();
            mainView.goToListPhotos();
        }

        @Override
        public void onError() {
            mainView.hideLoading();
            mainView.showUploadError();
        }
    };

}
