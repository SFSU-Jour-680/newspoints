package newspoints.sfsu.com.newsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.fragments.UploadProjectFragment;
import newspoints.sfsu.com.newsp_cloud.dropbox.DropboxActivity;
import newspoints.sfsu.com.newsp_cloud.googledrive.GoogleDriveActivity;
import newspoints.sfsu.com.newsp_data.entities.Project;


/**
 * DriveUploadActivity makes API calls to Google Drive to upload the Data(Projects and media content).
 * <a href="https://developers.google.com/drive/android/create-file">Google Doc official</a>
 * <br/>
 * <a href="http://stackoverflow.com/questions/33517043/how-to-create-multiple-files-programmatically-using-drive-api-for
 * -android">Upload multiple files folder</a>
 * and
 * <a href="http://stackoverflow.com/questions/27534929/how-to-handle-internet-connectivity-lost-after-onconnected-in-google-drive
 * -andro?rq=1">Handle connection</a>
 */
public class DriveUploadActivity extends MainBaseActivity implements UploadProjectFragment.IUploadProjectCallbacks {

    public static final String KEY_UPLOAD_GDRIVE = "upload_to_google_drive";
    public static final String KEY_UPLOAD_DROPBOX = "upload_to_dropbox";
    private static final String TAG = "~!@#$DriveUploadAct";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_upload);

        try {
            fragmentManager = getSupportFragmentManager();
            if (findViewById(R.id.driveUpload_fragment_container) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                UploadProjectFragment uploadProjectFragment = new UploadProjectFragment();
                performAddFragmentTransaction(uploadProjectFragment);

            }

        } catch (Exception e) {
            Log.i(TAG, "onCreate: " + e.getMessage());
        }

    }

    private void performAddFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.driveUpload_fragment_container, fragment);
        transaction.commit();
    }

    private void performReplaceFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.driveUpload_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void uploadProjectToGoogleDrive(Project mProject) {
        Intent googleDriveIntent = new Intent(DriveUploadActivity.this, GoogleDriveActivity.class);
        googleDriveIntent.putExtra(KEY_UPLOAD_GDRIVE, mProject);
        startActivity(googleDriveIntent);
        finish();
    }

    @Override
    public void uploadProjectToDropbox(Project mProject) {
        Intent dropboxIntent = new Intent(DriveUploadActivity.this, DropboxActivity.class);
        dropboxIntent.putExtra(KEY_UPLOAD_DROPBOX, mProject);
        startActivity(dropboxIntent);
        finish();
    }
}
