package newspoints.sfsu.com.newsp.ui.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.storagedir.image.AlbumStorageDirFactory;
import newspoints.sfsu.com.newsp.storagedir.project.BaseProjectDirFactory;
import newspoints.sfsu.com.newsp.storagedir.project.ProjectStorageDirFactory;
import newspoints.sfsu.com.newsp_data.entities.Project;
import newspoints.sfsu.com.newsp_data.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.log.Logger;
import newspoints.sfsu.com.newsp_lib.util.AppUtil;
import newspoints.sfsu.com.newsp_lib.util.PermissionUtils;

/**
 * Allows {@link newspoints.sfsu.com.newsp_data.entities.User} to create the {@link Project}. Each project contains image, name ,
 * description and category that are entered by user. In addition to the basic details, the project also contains the location;
 * latitude and longitude where it is created.
 */
public class CreateProjectFragment extends Fragment implements View.OnClickListener {
    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private static final String JPEG_FILE_PREFIX = "NP_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int GALLERY_CAMERA_PERMISSION = 24;
    private static final String TAG = "~!@#$CreateProjFrag";
    @Bind(R.id.imageView_createProj_main)
    ImageView imageView_main;
    @Bind(R.id.editText_createProj_name)
    EditText et_name;
    @Bind(R.id.editText_createProj_description)
    EditText et_description;
    @Bind(R.id.spinner_createProj_category)
    Spinner spinner_category;
    @Bind(R.id.button_createProj_createProj)
    Button btn_createProject;
    @Bind(R.id.fab_createProj_addImage)
    FloatingActionButton fab_addImage;
    private String selectedImagePath;
    private ICreateProjectCallbacks mInterface;
    private Context mContext;
    private PermissionUtils mPermissionUtils;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private ProjectStorageDirFactory mProjectStorageDirFactory;
    private BaseProjectDirFactory mBaseProjectDirFactory;
    private Project mProject;
    private File myProjectDirectory;

    public CreateProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_project, container, false);
        ButterKnife.bind(this, rootView);

        btn_createProject.setOnClickListener(this);
        fab_addImage.setOnClickListener(this);
        mBaseProjectDirFactory = new BaseProjectDirFactory();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ICreateProjectCallbacks) {
            mInterface = (ICreateProjectCallbacks) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ICreateProjectCallbacks interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_createProj_createProj:
                createProject();
                break;
            case R.id.fab_createProj_addImage:
                askForPermission();
                break;
        }
    }

    /**
     * Helper method to clear runtime permissions for user to capture image
     */
    private void askForPermission() {
        int hasCameraPermission = 0;
        int hasWritePermission = 0;
        int hasReadPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasCameraPermission = mContext.checkSelfPermission(Manifest.permission.CAMERA);
            hasWritePermission = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            hasReadPermission = mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<>();
            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), GALLERY_CAMERA_PERMISSION);
            }
        } else {
            Log.i(TAG, "askForPermission: not working");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: reached");
        switch (requestCode) {
            case GALLERY_CAMERA_PERMISSION: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissionUtils.setPermission(PermissionUtils.CAMERA);
                        mPermissionUtils.setPermission(PermissionUtils.READ);
                        mPermissionUtils.setPermission(PermissionUtils.WRITE);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
                startDialogForChoosingImage();
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /**
     * Loads image into ImageView
     */
    private void loadImage() {
        boolean isCameraApproved = mPermissionUtils.isCameraPermissionApproved();
        boolean isWriteStorageApproved = mPermissionUtils.isWritePermissionApproved();
        boolean isReadStorageApproved = mPermissionUtils.isReadPermissionApproved();

        if (isCameraApproved && isWriteStorageApproved && isReadStorageApproved) {
            startDialogForChoosingImage();
        } else {
            askForPermission();
        }
    }


    /**
     * Helper method for starting a dialog box to prompt user to choose image from Gallery or Camera.
     */
    private void startDialogForChoosingImage() {
        AlertDialog.Builder chooseImageAlertDialog = new AlertDialog.Builder(mContext);
        chooseImageAlertDialog.setTitle(R.string.alertDialog_projectImage_title);
        chooseImageAlertDialog.setMessage(R.string.alertDialog_projectImage_message);

        // Choose from Gallery.
        chooseImageAlertDialog.setPositiveButton(R.string.alertDialog_projectImage_gallery,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent;
                        // Select the Intent depending on Camera
                        pictureActionIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // finally call intent for Result.
                        pictureActionIntent.setType("image/*");
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                    }
                });

        // Choose from Camera.
        chooseImageAlertDialog.setNegativeButton(R.string.alertDialog_projectImage_camera,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = null;
                        try {
                            f = setUpPhotoFile();
                            selectedImagePath = f.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        } catch (IOException e) {
                            e.printStackTrace();
                            f = null;
                            selectedImagePath = null;
                        }
                        startActivityForResult(takePictureIntent, CAMERA_PICTURE);
                    }
                });
        chooseImageAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the Image was selected from the Camera
        if (requestCode == CAMERA_PICTURE) {
            handleCameraPicture();
        }
        // if the image is chosen from Gallery
        else if (requestCode == GALLERY_PICTURE) {
            try {
                if (data != null) {
                    InputStream inputStream = mContext.getContentResolver().openInputStream(data.getData());
                    Drawable testExternalStorage = BitmapDrawable.createFromStream(inputStream, "tick");
                    imageView_main.setImageDrawable(testExternalStorage);

                    selectedImagePath = data.getData().getPath();
                    Log.i(TAG, selectedImagePath);
                }
            } catch (Exception e) {
                Log.i(TAG, "->G :" + e.getMessage());
            }
        }
    }

    /**
     * Handles the captured camera image
     */
    private void handleCameraPicture() {
        if (selectedImagePath != null) {
            setPic();
            galleryAddPic();
            selectedImagePath = null;
        } else {
            Log.i(TAG, "handleCameraPicture: selectedPath null");
        }
    }


    /**
     * Returns the Photo album for this application
     */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    /**
     * Returns the album directory for the application in external storage
     *
     * @return
     */
    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }


    /**
     * Method to create the temp file by specifying the name using prefix, suffix and timestamp
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss", Locale.US).format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp;
        File albumF = getAlbumDir();
        return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
    }

    /**
     * Intermediate method to create File for the captured Image
     *
     * @return
     * @throws IOException
     */
    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        selectedImagePath = f.getAbsolutePath();
        return f;
    }

    /**
     * Previews the captured image into ImageView
     */
    private void setPic() {
        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imageView_main.getWidth();
        int targetH = imageView_main.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        imageView_main.setImageBitmap(bitmap);

        Log.i("~!@#$", selectedImagePath);
    }

    /**
     * Finally saves the image to the gallery in external storage
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(selectedImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }


    /**
     * Finally create project by taking in all the information and pass the newly created project to Listener
     */
    private void createProject() {

        String projectName = et_name.getText().toString();
        String description = et_description.getText().toString();
        String category = spinner_category.getSelectedItem().toString();
        String project_dir_url = "", sub_dir_url = "";


        myProjectDirectory = mBaseProjectDirFactory.createAppRootDirectory();
        myProjectDirectory = mBaseProjectDirFactory.getProjectStorageDirectory(projectName);

        // use the same File object to create Project Directory
        if (!myProjectDirectory.exists()) {
            if (myProjectDirectory.mkdir()) {
                project_dir_url = myProjectDirectory.getAbsolutePath();
                // finally create sub directory for Category
                // use the same File object to create Project Directory
                myProjectDirectory = mBaseProjectDirFactory.getProjectCategoryStorageDirectory(projectName, category);
                if (!myProjectDirectory.exists()) {
                    if (myProjectDirectory.mkdir()) {
                        sub_dir_url = myProjectDirectory.getAbsolutePath();
                    }

                }
            }
        }

        // create the Project
        mProject = new Project(projectName, category, selectedImagePath, project_dir_url, sub_dir_url, description,
                ProjectConstants.CLOUD_GOOGLE_DRIVE, 1.5, 2.8, AppUtil.getCurrentTimeStamp());

        // pass the project to ProjectActivity
        mInterface.createProject(mProject);
    }

    /**
     * Callback interface to handle the onClick Listeners in {@link CreateProjectFragment}.
     */
    public static interface ICreateProjectCallbacks {
        /**
         * Callback method to handle onclick event of CreateProjectFragment
         *
         * @param mNewProject
         */
        void createProject(Project mNewProject);
    }
}
