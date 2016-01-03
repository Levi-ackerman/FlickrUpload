package com.example.flickr.flickr.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.flickr.flickr.FlickrHelper;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.uploader.UploadMetaData;

import java.io.File;
import java.io.FileInputStream;

public class UploadPhotoTask extends AsyncTask<OAuth, Void, String> {
	private static final String TAG = UploadPhotoTask.class.getSimpleName();

	private OnUploadComplete onUploadComplete;
	private String filePath;

	public UploadPhotoTask(OnUploadComplete onUploadComplete, String filePath) {
		this.onUploadComplete = onUploadComplete;
		this.filePath = filePath;
	}

	@Override
	protected String doInBackground(OAuth... params) {
		OAuth oauth = params[0];
		OAuthToken token = oauth.getToken();

		try {
			Flickr f = FlickrHelper.getInstance().getFlickrAuthed(
					token.getOauthToken(), token.getOauthTokenSecret());

			File file = new File(filePath);

			UploadMetaData uploadMetaData = new UploadMetaData();
			uploadMetaData.setTitle("" + file.getName());


			return f.getUploader().upload(file.getName(),
					new FileInputStream(file), uploadMetaData);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			//e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		Log.d(TAG, "PhotoID : " + result);
		if(result != null && !result.isEmpty()){
			onUploadComplete.onSuccess();
		}else{
			onUploadComplete.onError();
		}
	}

	public interface OnUploadComplete{
		void onSuccess();
		void onError();
	}
}