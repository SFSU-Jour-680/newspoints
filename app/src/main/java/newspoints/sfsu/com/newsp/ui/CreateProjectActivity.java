package newspoints.sfsu.com.newsp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.util.ProjectConstants;

public class CreateProjectActivity extends Activity implements View.OnClickListener, LocationListener {
    static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    // ExceptionHandlerActivity exceptionHandle;
    public static Context context;
    public UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

        public UncaughtExceptionHandler uncaughtException = Thread
                .getDefaultUncaughtExceptionHandler();

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            System.exit(2);
            uncaughtException.uncaughtException(thread, ex);
        }
    };
    protected LocationManager locationManager;
    File myStorageDir;
    SharedPreferences prefs;
    EditText authorName, organizationName;
    Spinner category;
    Button submit;
    Editor editor;
    int projectId = 0;
    List<String> categoryList;
    String picturePath = null;
    String defaultName;
    Location location;
    double latitude, longitude;
    private ImageView image;
    private TextView change_image;
    private int RESULT_LOAD_IMAGE = 666;
    private boolean imageSelected = false;

    private Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        return location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        context = this;


        // identify and initialize each control
        image = (ImageView) findViewById(R.id.imageView_createProj_main);

        authorName = (EditText) findViewById(R.id.editText_createProj_name);
        SimpleDateFormat dateTime = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        defaultName = dateTime.format(new Date());
        authorName.setHint("Project " + defaultName);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        category = (Spinner) findViewById(R.id.spinner_createProj_category);

        if (prefs == null) {
            prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            editor = prefs.edit();
        }

        projectId = prefs.getInt(ProjectConstants.PROJECT_ID, 0);
        categoryList = new ArrayList<String>();
        Set<String> categorySet = prefs.getStringSet("category", null);
        categorySet.add("None");
        Iterator i = categorySet.iterator();
        while (i.hasNext()) {
            categoryList.add((String) i.next());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                CreateProjectActivity.this, android.R.layout.simple_spinner_item,
                categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        //check location
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.d("=====>", ex.getMessage());
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(CreateProjectActivity.this);
            dialog.setMessage("Location service is disabled. Please enable location.");
            dialog.setPositiveButton(CreateProjectActivity.this.getResources().getString(R.string.enable_gps), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(CreateProjectActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = getLocation();
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = getLocation();
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

//            Toast.makeText(context, latitude + " : " + longitude, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateProjectActivity.this, IndexActivity.class);
        startActivity(i);
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch (v.getId()) {
            case R.id.imageView_createProj_main:
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.submit:
                String author = authorName.getText().toString();
                ProjectConstants.selectedProjectName = author;

                String categoryName = category.getSelectedItem().toString();
                ProjectConstants.selectedProjectCategory = categoryName;

                if (author == null || author.equals("")) {
                    ProjectConstants.selectedProjectName = defaultName;
                    editor.putString(ProjectConstants.PROJECT_NAME, "Project " + ProjectConstants.selectedProjectName);
                    editor.commit();
                    editor.putString(ProjectConstants.SELECTED_CATEGORY, categoryName);
                    editor.commit();
                } else {
                    editor.putString(ProjectConstants.PROJECT_NAME, ProjectConstants.selectedProjectName);
                    editor.commit();
                    editor.putString(ProjectConstants.SELECTED_CATEGORY, categoryName);
                    editor.commit();
                }
                projectId++;

                editor.putInt(ProjectConstants.PROJECT_ID, projectId);
                editor.commit();
                String dir = ProjectConstants.selectedProjectName;
                String subDir = ProjectConstants.selectedProjectCategory;

                myStorageDir = new File(Environment.getExternalStorageDirectory() + "/journalist");
                if (!myStorageDir.exists()) {
                    myStorageDir.mkdir();
                }

                myStorageDir = new File(Environment.getExternalStorageDirectory() + "/journalist/" + dir);

                if (!myStorageDir.exists()) {
                    myStorageDir.mkdir();
                    myStorageDir = new File(Environment.getExternalStorageDirectory() + "/journalist/" + dir + "/" + subDir);
                    if (!myStorageDir.exists()) {
                        myStorageDir.mkdir();
                    }
                } else {
                    String[] temp = myStorageDir.list();
                    subDir = temp[0];
                    myStorageDir = new File(Environment.getExternalStorageDirectory() + "/journalist/" + dir + "/" + subDir + "/");
                }
                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                String dateString = date.format(new Date());
                if (picturePath == null) {
                    ProjectConstants.dbClass.projectDetails(projectId, dir, subDir, null, dateString, latitude, longitude);
                } else {
                    ProjectConstants.dbClass.projectDetails(projectId, dir, subDir, picturePath, dateString, latitude, longitude);
                }

                Intent intent = new Intent(CreateProjectActivity.this,
                        FilesListActivity.class);
                intent.putExtra("filesList", subDir);
                intent.putExtra("foldername", dir);
                editor.putString("SelectedProject", dir);
                editor.commit();

                startActivity(intent);
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageSelected = true;
            image.setBackground(null);
            image.setImageBitmap(getScaledBitmap(picturePath, 800, 800));
        }
    }

    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    @Override
    public void onLocationChanged(Location arg0) {

    }

    @Override
    public void onProviderDisabled(String arg0) {

    }

    @Override
    public void onProviderEnabled(String arg0) {

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    // simple POJO for accessing the LocationDetails of the Project
    public static class LocationDetails {
        String projectName;
        double latitude;
        double longitude;

        public LocationDetails(String projectName, double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.projectName = projectName;
        }

        public String getProjectName() {
            return projectName;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return projectName + " -> " + latitude + " : " + longitude;
        }
    }
}
