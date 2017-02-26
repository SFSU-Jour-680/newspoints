package newspoints.sfsu.com.newsp_cloud.dropbox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Activity to carry out all the operations related to DropBox
 */
public class DropboxActivity extends AppCompatActivity {

    private DropboxAPI<AndroidAuthSession> mDropboxAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppKeyPair appKeys = new AppKeyPair(ProjectConstants.DROPBOX_APP_KEY, ProjectConstants.DROPBOX_APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        mDropboxAPI.getSession().startOAuth2Authentication(DropboxActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mDropboxAPI.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDropboxAPI.getSession().finishAuthentication();

                String accessToken = mDropboxAPI.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private void storeKeys(String key, String secret) {
        SharedPreferences prefs = getSharedPreferences(ProjectConstants.ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(ProjectConstants.ACCESS_KEY_NAME, key);
        edit.putString(ProjectConstants.ACCESS_SECRET_NAME, secret);
        edit.apply();
    }
}