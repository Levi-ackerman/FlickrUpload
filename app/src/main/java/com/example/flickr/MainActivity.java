package com.example.flickr;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flickr.common.Constant;
import com.example.flickr.common.OAuthUtils;
import com.example.flickr.home.MainPresenter;
import com.example.flickr.home.MainView;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MainView {
    //TAG Logger
    private static final String TAG = MainActivity.class.getSimpleName();
    //Button
    private Button btnSignIn;
    //Dialog
    private Dialog selectDialog;
    private ProgressDialog progressDialog;
    //ImageView
    private ImageView img;
    //Presenter
    private MainPresenter mainPresenter;
    //File Image
    private File fileImage;
    //Constant
    private static final int REQUEST_CODE_OPEN_CAMERA = 100;
    private static final int REQUEST_CODE_OPEN_GALLERY = 200;
    private boolean MENU_IS_SHOW = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObject();
        initEvent();

        mainPresenter.loadContent();
    }

    private void initObject() {
        mainPresenter = new MainPresenter(this);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        img = (ImageView) findViewById(R.id.img);

        progressDialog = new ProgressDialog(this);
        selectDialog = new Dialog(MainActivity.this);
        selectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDialog.setContentView(R.layout.dialog_select_photo);
        View vOpenCamera = selectDialog.findViewById(R.id.btnCamera);
        vOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onSelectCamera();
            }
        });

        View vOpenGallery = selectDialog.findViewById(R.id.btnGallery);
        vOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onSelectGallery();
            }
        });
    }

    private void initEvent() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onSelectButtonClick();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onSignInButtonClick();
            }
        });

    }

    @Override
    public void showUnauthenticatedMessage() {
        btnSignIn.setVisibility(View.VISIBLE);
        findViewById(R.id.vAuthenticated).setVisibility(View.GONE);
    }

    @Override
    public void hideUnauthenticatedMessage() {
        btnSignIn.setVisibility(View.GONE);
        findViewById(R.id.vAuthenticated).setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showSelectDialog() {
        selectDialog.show();
    }

    @Override
    public void hideSelectDialog() {
        if (selectDialog.isShowing()) {
            selectDialog.dismiss();
        }
    }

    @Override
    public void showSelectPhotoButton() {
        findViewById(R.id.select_photo_area).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSelectPhotoButton() {
        findViewById(R.id.select_photo_area).setVisibility(View.GONE);
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                REQUEST_CODE_OPEN_GALLERY);
    }

    @Override
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_OPEN_CAMERA);
    }

    @Override
    public void setPhoto(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }


    @Override
    public void showError(String e) {
        Toast.makeText(getBaseContext(), e, Toast.LENGTH_LONG).show();
    }

    @Override
    public OAuth loadOAuth() {
        return OAuthUtils.loadOauth(getBaseContext());
    }

    @Override
    public boolean isAuthenticated() {
        OAuth oauth = loadOAuth();
        return (oauth != null && oauth.getUser() != null);
    }

    @Override
    public void saveOAuthToken(String userName, String userId, String token, String tokenSecret) {
        OAuthUtils.saveOAuthToken(getBaseContext(), userName, userId, token, tokenSecret);
    }

    @Override
    public void openBrowserView(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                .parse(url)));
    }

    @Override
    public String getOAuthCallbackString() {
        Intent intent = getIntent();
        String scheme = intent.getScheme();
        OAuth savedToken = loadOAuth();

        if (Constant.CALLBACK_SCHEME.equals(scheme)
                && (savedToken == null || savedToken.getUser() == null)) {
            Uri uri = intent.getData();
            String query = uri.getQuery();
            return query;
        }
        return null;
    }

    @Override
    public void oauthDone(OAuth oAuth) {
        User user = oAuth.getUser();
        OAuthToken token = oAuth.getToken();
        Toast.makeText(this, R.string.authen_success_message, Toast.LENGTH_LONG).show();
        saveOAuthToken(user.getUsername(), user.getId(),
                token.getOauthToken(), token.getOauthTokenSecret());
    }

    @Override
    public void setMenuVisibility(boolean isShow) {
        MENU_IS_SHOW = isShow;
        invalidateOptionsMenu();
    }

    @Override
    public void goToListPhotos() {
        Intent intent = new Intent(MainActivity.this, ListPhotoActivity.class);
        startActivity(intent);
    }

    @Override
    public void showUploadSuccess() {
        Toast.makeText(getBaseContext(), R.string.message_upload_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUploadError() {
        Toast.makeText(getBaseContext(), R.string.message_upload_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void clearPreference() {
        SharedPreferences pref = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
        pref.edit().clear().commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mainPresenter.onPhotoSelected();
            Bitmap thumbnail;
            if (requestCode == REQUEST_CODE_OPEN_CAMERA) {
                /**
                 * Get Photo after taken
                 */
                thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                 fileImage = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    fileImage.createNewFile();
                    fo = new FileOutputStream(fileImage);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setPhoto(thumbnail);
            } else if (requestCode == REQUEST_CODE_OPEN_GALLERY) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
                setPhoto(thumbnail);
                //set image url
                Log.d(TAG, "ImagePath : " + selectedImagePath);
                fileImage = new File(selectedImagePath);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.onAuthenticationCallback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fileImage = null;
        mainPresenter.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // this is very important, otherwise you would get a null Scheme in the onResume later on.
        setIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        for(int i = 0; i < menu.size(); i++){
            menu.getItem(i).setVisible(MENU_IS_SHOW);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user) {
            mainPresenter.onLogoutMenuClick();
        }else if(id == R.id.action_upload){
            if(fileImage == null){
                Toast.makeText(getBaseContext(), R.string.message_select_photo, Toast.LENGTH_LONG).show();
            }else{
                mainPresenter.onUploadMenuClick(fileImage.getAbsolutePath());
            }
        }else if(id == R.id.action_list_photo){
            mainPresenter.onListPhotoMenuClick();
        }

        return super.onOptionsItemSelected(item);
    }
}
