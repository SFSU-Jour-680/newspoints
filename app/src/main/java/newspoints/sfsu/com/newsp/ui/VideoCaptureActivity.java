package newspoints.sfsu.com.newsp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.util.ProjectConstants;

public class VideoCaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback, OnClickListener, PictureCallback {

    static int pauseCount = 1;
    private static String CURRENT_EVENT = "CurrentEvent";
    int versionCode;
    String imagePath;
    Button buttonStop, sourceVideo, shotVideo, interview, wide, shot4, shot5, extraWide, establish;
    List<Button> selectedShotList, selectedSourceList;
    Button source1, source2, source3, source4, source5, source6;
    MediaRecorder mediaRecorder;
    SurfaceHolder surfaceHolder;
    Boolean recording = false, sourceVisible = false, shotVisible = false;
    Boolean lastPressedPause = false, backPressed = false;
    LinearLayout cameraTypeTray;
    TextView tv1, tv2, tv3, tv4, tv5, recordTime, tv9, tv7, tv8;
    EditText tv6;
    LinearLayout questionsRow;
    ScrollView sourceInfo;
    File newFileAfterPause;
    HorizontalScrollView hsv;
    Boolean recordingState;
    long startTime = 0;
    SurfaceView myVideoView;
    String sourceName = "SourceName", angleName = "Angle", type = "Audio", lastEndTime = null, endTime;
    Date lastEndDate, endDate;
    int hours, minutes, seconds;
    int recordingHour = 0, recordingMinute = 0, recordingSeconds;
    String ans1, ans2, ans3, ans4, ans5;
    Camera camera;
    File myStorageDir;
    // String projectRecordingCount;
    int setWidth;
    SharedPreferences prefs;
    Editor edit;
    String recordingId;
    Set source, question, shot;
    int count = 0;

    String categorySelected;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int recordingSeconds = (int) (millis / 1000);

            recordingSeconds = recordingSeconds % 60;
            if (recordingSeconds == 59) {
                recordingSeconds = 0;
                recordingMinute++;
                if (recordingMinute == 60) {
                    recordingMinute = 0;
                    recordingHour++;
                }
            }
            recordTime.setText(String.format("%02d:%02d", recordingMinute,
                    recordingSeconds));
            if (!backPressed)
                timerHandler.postDelayed(this, 1000);
        }
    };
    private LinearLayout shotSourceTray;
    private ScrollView cameraSourceScorll;
    private boolean previewRunning = false;

    /**
     * Called when the activity is first created.
     */

    @SuppressLint("ResourceAsColor")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sourceVisible = false;
        shot = new HashSet<>();
        source = new HashSet<>();
        question = new HashSet<>();
        ans1 = ans2 = ans3 = ans4 = ans5 = "";

        // mediaRecorder = new MediaRecorder();
        // initMediaRecorder();

        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        edit = prefs.edit();
        edit.putString(CURRENT_EVENT, "404");
        edit.commit();
        // projectRecordingCount = ProjectConstants.selectedProjectName;
        // pauseCount = prefs.getInt(projectRecordingCount, 1);
        // edit.putInt(projectRecordingCount, pauseCount);
        edit.commit();
        categorySelected = ProjectConstants.selectedProjectCategory;

        question = prefs.getStringSet(categorySelected + "question", null);
        shot = prefs.getStringSet(categorySelected + "shot", null);
        source = prefs.getStringSet(categorySelected + "source", null);

        setContentView(R.layout.activity_video_capture);
        initializeViews();
        registerClickListener();

        myVideoView = (SurfaceView) findViewById(R.id.videoview);
        surfaceHolder = myVideoView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // camera = Camera.open();
        // mediaRecorder.setCamera(camera);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        setWidth = width / 3;
        int height = display.getHeight();
        int setHeight = height / 2;
        questionsRow.scrollBy(setWidth / 2, 0);
        // params.leftMargin = 60;
        // params.topMargin = 100;
        count = 0;
        if (question == null) {
        } else {
            Iterator i;

            count = 0;
            i = question.iterator();
            do {

                String temp = (String) i.next();

                if (count == 0) {
                    tv1.setText(temp);
                    tv1.setVisibility(View.VISIBLE);
                }
                if (count == 1) {
                    tv2.setText(temp);
                    tv2.setVisibility(View.VISIBLE);
                }
                if (count == 2) {
                    tv3.setText(temp);
                    tv3.setVisibility(View.VISIBLE);
                }
                if (count == 3) {
                    tv4.setText(temp);
                    tv4.setVisibility(View.VISIBLE);
                }
                if (count == 4) {
                    tv5.setText(temp);
                    tv5.setVisibility(View.VISIBLE);
                }
                if (count == 5) {
                    tv7.setText(temp);
                    tv7.setVisibility(View.VISIBLE);
                }
                if (count == 6) {
                    tv8.setText(temp);
                    tv8.setVisibility(View.VISIBLE);
                }
                if (count == 7) {
                    tv9.setText(temp);
                    tv9.setVisibility(View.VISIBLE);
                }
                count++;
            } while (i.hasNext());
            count = 0;
            Iterator j = shot.iterator();
            while (j.hasNext()) {
                String temp = (String) j.next();

                if (count == 0) {
                    interview.setText(temp);
                    interview.setVisibility(View.VISIBLE);
                }
                if (count == 1) {
                    establish.setVisibility(View.VISIBLE);
                    establish.setText(temp);
                }

                if (count == 2) {
                    wide.setText(temp);
                    wide.setVisibility(View.VISIBLE);
                }
                if (count == 3) {
                    extraWide.setText(temp);
                    extraWide.setVisibility(View.VISIBLE);
                }
                if (count == 4) {
                    extraWide.setText(temp);
                    extraWide.setVisibility(View.VISIBLE);
                }
                if (count == 5) {
                    shot4.setText(temp);
                    shot4.setVisibility(View.VISIBLE);
                }
                if (count == 6) {
                    shot5.setText(temp);
                    shot5.setVisibility(View.VISIBLE);
                }
                count++;
            }
            count = 0;
            Iterator k = source.iterator();
            while (k.hasNext()) {
                String temp = (String) k.next();

                if (count == 0) {
                    source1.setText(temp);
                    source1.setVisibility(View.VISIBLE);
                }
                if (count == 1) {
                    source2.setText(temp);
                    source2.setVisibility(View.VISIBLE);
                }
                if (count == 2) {
                    source3.setText(temp);
                    source3.setVisibility(View.VISIBLE);
                }
                if (count == 3) {
                    source4.setText(temp);
                    source4.setVisibility(View.VISIBLE);
                }
                if (count == 4) {
                    source5.setText(temp);
                    source5.setVisibility(View.VISIBLE);
                }
                if (count == 5) {
                    source6.setText(temp);
                    source6.setVisibility(View.VISIBLE);
                }
                count++;
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        versionCode = android.os.Build.VERSION.SDK_INT;
        if (versionCode < 20) {
            camera.takePicture(null, null, null, VideoCaptureActivity.this);
        }

        if (previewRunning) {

            camera.stopPreview();
        }

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            previewRunning = true;
        } catch (IOException e) {
            Log.e("camera", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        // TODO Auto-generated method stub
        String dir = prefs
                .getString(ProjectConstants.PROJECT_NAME, "Anonymous");
        String subDir = ProjectConstants.selectedProjectCategory;
        File pictureFile = new File(Environment.getExternalStorageDirectory()
                + "/journalist/" + dir + "/" + subDir + "/image1.jpeg");

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (Exception error) {
            Toast.makeText(this, "Image could not be saved.", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

        // prepareMediaRecorder();
        camera = Camera.open();
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            camera.setParameters(params);
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        recording = false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        previewRunning = false;
        camera.release();
    }

    protected void onResume() {
        super.onResume();
        edit.commit();
    }

    private void initializeViews() {
        questionsRow = (LinearLayout) findViewById(R.id.innerLay);

        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        tv1 = (TextView) findViewById(R.id.text1_video);
        tv2 = (TextView) findViewById(R.id.text2_video);
        tv3 = (TextView) findViewById(R.id.text3_video);
        tv4 = (TextView) findViewById(R.id.text4_video);
        tv5 = (TextView) findViewById(R.id.text5_video);
        tv9 = (TextView) findViewById(R.id.text9_video);
        tv8 = (TextView) findViewById(R.id.text8_video);
        tv7 = (TextView) findViewById(R.id.text7_video);

        tv6 = (EditText) findViewById(R.id.text6);
        tv1.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv2.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv3.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv4.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv5.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv6.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv7.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv8.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv9.setTypeface(ProjectConstants.ROBOT_REGULAR);

        recordTime = (TextView) findViewById(R.id.time);

        shotVideo = (Button) findViewById(R.id.shot_video);
        sourceVideo = (Button) findViewById(R.id.source_video);
        shotVideo.setTypeface(ProjectConstants.ROBOT_MEDIUM);

        sourceVideo.setTypeface(ProjectConstants.ROBOT_MEDIUM);

        cameraSourceScorll = (ScrollView) findViewById(R.id.camera_type_tray_view);

        interview = (Button) findViewById(R.id.interview);
        wide = (Button) findViewById(R.id.wide);
        extraWide = (Button) findViewById(R.id.extrawide);
        establish = (Button) findViewById(R.id.establish);
        shot4 = (Button) findViewById(R.id.shot4);
        shot5 = (Button) findViewById(R.id.shot5);
        selectedShotList = new ArrayList<Button>();
        selectedShotList.add(establish);
        selectedShotList.add(extraWide);
        selectedShotList.add(wide);
        selectedShotList.add(interview);
        selectedShotList.add(shot4);
        selectedShotList.add(shot5);
        shotSourceTray = (LinearLayout) findViewById(R.id.shot_source_tray);

        source1 = (Button) findViewById(R.id.source1);
        source2 = (Button) findViewById(R.id.source2);
        source3 = (Button) findViewById(R.id.source3);

        source4 = (Button) findViewById(R.id.source4);
        source5 = (Button) findViewById(R.id.source5);
        source6 = (Button) findViewById(R.id.source6);
        selectedSourceList = new ArrayList<Button>();
        selectedSourceList.add(source1);
        selectedSourceList.add(source2);
        selectedSourceList.add(source3);
        selectedSourceList.add(source4);
        selectedSourceList.add(source5);
        selectedSourceList.add(source6);

        cameraTypeTray = (LinearLayout) findViewById(R.id.camera_type_tray);
        sourceInfo = (ScrollView) findViewById(R.id.source_info);

        buttonStop = (Button) findViewById(R.id.stop);

    }

    private void registerClickListener() {
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);

        buttonStop.setOnClickListener(this);
        sourceVideo.setOnClickListener(this);
        shotVideo.setOnClickListener(this);
        interview.setOnClickListener(this);
        establish.setOnClickListener(this);
        extraWide.setOnClickListener(this);
        wide.setOnClickListener(this);
        shot4.setOnClickListener(this);
        shot5.setOnClickListener(this);
        source1.setOnClickListener(this);

        source2.setOnClickListener(this);
        source3.setOnClickListener(this);
        source4.setOnClickListener(this);
        source5.setOnClickListener(this);
        source6.setOnClickListener(this);
    }

    private void storeDetails() {

        if (!recording) {
            endTime = recordTime.getText().toString();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            Date date1 = null;
            try {
                date1 = df.parse(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            endDate = date1;

            lastEndTime = endTime;
            lastEndDate = endDate;
        } else {
            endTime = recordTime.getText().toString();

            String eventID = prefs.getString(CURRENT_EVENT, "404");
            String selectedProject = ProjectConstants.selectedProjectName;

            long recordingDuration = recordingDuration();
            Date endDate = VideoCaptureActivity.this.endDate;
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy  hh:mm");
            String formattedDate = df.format(new Date());
            if (!eventID.contains("404")) {
                if (hours == 0) {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, minutes + ":" + seconds, eventID,
                            "video", imagePath, recordingDuration,
                            formattedDate);

                } else {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, "" + hours + ":" + minutes + ":"
                                    + seconds, eventID, "video", imagePath,
                            recordingDuration, formattedDate);

                }
            } else {
                if (hours == 0) {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, minutes + ":" + seconds, eventID,
                            "video", imagePath, recordingDuration,
                            formattedDate);

                } else {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, "" + hours + ":" + minutes + ":"
                                    + seconds, eventID, "video", imagePath,
                            recordingDuration, formattedDate);

                }
            }
            lastEndTime = endTime;
            lastEndDate = endDate;

        }
    }

    private long recordingDuration() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        Date date1 = null;
        try {
            date1 = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        endDate = date1;

        long timeDiff = endDate.getTime() - lastEndDate.getTime();
        long convertedTimeDiff = timeDiff / 1000;
        int totalTimeInSeconds = (int) convertedTimeDiff;
        int timeInSeconds = (int) (timeDiff / 1000);
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        return totalTimeInSeconds;
    }

    private void initMediaRecorderAfterPauseState(int count) {
        camera.unlock();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        CamcorderProfile camcorderProfile_HQ = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        mediaRecorder.setProfile(camcorderProfile_HQ);
        try {
            myStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/journalist");
            if (!myStorageDir.exists()) {

                myStorageDir.mkdir();
            }
            myStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/journalist/" + ProjectConstants.selectedProjectName);

            if (!myStorageDir.exists()) {
                myStorageDir.mkdir();
                myStorageDir = new File(Environment.getExternalStorageDirectory()
                        + "/journalist/" + ProjectConstants.selectedProjectName
                        + "/" + ProjectConstants.selectedProjectCategory);
                if (!myStorageDir.exists()) {
                    myStorageDir.mkdir();
                }
            } else {

                myStorageDir = new File(Environment.getExternalStorageDirectory()
                        + "/journalist/" + ProjectConstants.selectedProjectName
                        + "/" + ProjectConstants.selectedProjectCategory + "/");

            }
            newFileAfterPause = new File(myStorageDir + "/myRecording" + count
                    + ".mp4");


            mediaRecorder.setOutputFile(myStorageDir + "/myRecording" + count
                    + ".mp4");
        } catch (Exception e) {
            Log.d("=====>", e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (recording) {
            storeDetails();
            stopCalled();
            timerHandler.removeCallbacks(timerRunnable);
        }
        Intent i = new Intent(VideoCaptureActivity.this, FilesListActivity.class);
        startActivity(i);
        finish();
    }

    private void stopCalled() {
        buttonStop.setBackgroundResource(R.mipmap.ic_play_36);

        recording = false;

        String eventID = prefs.getString(CURRENT_EVENT, "404");

        if (eventID.contains("404")) {
            storeDetails();
        }
        newFileAfterPause.renameTo(new File(myStorageDir + "/"
                + "MyVideo Recording " + recordingId + ".mp4"));
        pauseCount++;

        mediaRecorder.reset();

        recording = false;
        lastPressedPause = false;
        backPressed = true;

        edit.putString(CURRENT_EVENT, "404");
        edit.commit();
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(myStorageDir + "/"
                + "MyVideo Recording " + recordingId + ".mp4", MediaStore.Images.Thumbnails.MINI_KIND);


        FileOutputStream out = null;
        try {
            out = new FileOutputStream(myStorageDir + "/image" + recordingId
                    + ".png");
            // imagePath = myStorageDir + "/image" + recordingId + ".png";
            thumb.compress(Bitmap.CompressFormat.PNG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.stop && recording) {

            storeDetails();
            stopCalled();

        } else if (v.getId() == R.id.stop && !recording) {
            recordingMinute = 0;
            buttonStop.setBackgroundResource(R.mipmap.ic_stop_white_24dp);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            Date date1 = null;
            try {
                date1 = df.parse(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            lastEndDate = date1;
            String selectedProject = ProjectConstants.selectedProjectName;

            String tempRecordingId = ProjectConstants.dbClass
                    .getLastRecordingId(selectedProject, "video");

            if (tempRecordingId == null || tempRecordingId.equals("")
                    || tempRecordingId.equals("null")) {

                tempRecordingId = String.valueOf(2);
                recordingId = tempRecordingId;

            } else {
                int temp = Integer.parseInt(tempRecordingId);
                temp = temp + 1;
                recordingId = String.valueOf(temp);
            }

            backPressed = false;
            initMediaRecorderAfterPauseState(pauseCount);

            imagePath = myStorageDir + "/image" + recordingId + ".png";

            try {
                mediaRecorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mediaRecorder.start();
            recording = true;
            startTime = System.currentTimeMillis();

            timerHandler.postDelayed(timerRunnable, 0);
            lastEndTime = null;

        }
        if (v.getId() == R.id.source_video) {
            if (!sourceVisible) {
                // sourceInfo.setVisibility(0);
                cameraSourceScorll.setVisibility(0);
                cameraTypeTray.setVisibility(0);
                sourceVisible = true;
            } else {
                sourceVisible = false;
                // sourceInfo.setVisibility(4);
                cameraSourceScorll.setVisibility(4);
                cameraTypeTray.setVisibility(4);
            }
        }
        if (v.getId() == R.id.shot_video) {
            if (!shotVisible) {
                sourceInfo.setVisibility(0);
                shotSourceTray.setVisibility(0);
                // cameraTypeTray.setVisibility(0);
                shotVisible = true;
            } else {
                shotVisible = false;
                sourceInfo.setVisibility(4);
                shotSourceTray.setVisibility(4);
                // cameraTypeTray.setVisibility(4);
            }
        }
        if (v.getId() == R.id.text1_video) {

            storeDetails();
            String eventID = prefs.getString(tv1.getText().toString(), "0000");

            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();

            tv6.setText(tv1.getText().toString());
            tv1.setBackgroundColor(0x3364FE2E);
            tv1.setTextColor(getResources().getColor(android.R.color.white));

        }
        if (v.getId() == R.id.text2_video) {

            storeDetails();
            String eventID = prefs.getString(tv2.getText().toString(), "0000");

            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv2.getText().toString());
            tv2.setBackgroundColor(0x3364FE2E);
            tv2.setTextColor(getResources().getColor(android.R.color.white));

        }
        if (v.getId() == R.id.text3_video) {

            storeDetails();
            String eventID = prefs.getString(tv3.getText().toString(), "0000");

            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv3.getText().toString());
            tv3.setBackgroundColor(0x3364FE2E);
            tv3.setTextColor(getResources().getColor(android.R.color.white));

        }
        if (v.getId() == R.id.text4_video) {

            storeDetails();
            String eventID = prefs.getString(tv4.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv4.setTextColor(getResources().getColor(android.R.color.white));

            tv6.setText(tv4.getText().toString());
            tv4.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text5_video) {
            tv5.setTextColor(getResources().getColor(android.R.color.white));

            storeDetails();
            String eventID = prefs.getString(tv5.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv5.getText().toString());
            tv5.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text8_video) {

            storeDetails();
            String eventID = prefs.getString(tv8.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv8.getText().toString());
            tv8.setTextColor(getResources().getColor(android.R.color.white));
            tv8.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text7_video) {

            storeDetails();
            String eventID = prefs.getString(tv7.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv7.getText().toString());
            tv7.setTextColor(getResources().getColor(android.R.color.white));
            tv7.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text9_video) {

            storeDetails();
            String eventID = prefs.getString(tv9.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv9.getText().toString());
            tv9.setTextColor(getResources().getColor(android.R.color.white));
            tv9.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.establish) {

            storeDetails();
            edit.putString(angleName, establish.getText().toString());
            edit.commit();
            String eventID = prefs.getString(establish.getText().toString(),
                    "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.establish);

        }
        if (v.getId() == R.id.extrawide) {

            storeDetails();
            edit.putString(angleName, extraWide.getText().toString());
            edit.commit();
            String eventID = prefs.getString(extraWide.getText().toString(),
                    "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.extrawide);
        }
        if (v.getId() == R.id.wide) {

            storeDetails();
            edit.putString(angleName, wide.getText().toString());

            edit.commit();
            String eventID = prefs.getString(wide.getText().toString(), "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.wide);

        }
        if (v.getId() == R.id.interview) {

            storeDetails();
            edit.putString(angleName, interview.getText().toString());
            edit.commit();
            String eventID = prefs.getString(interview.getText().toString(),
                    "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();

            focusSelectedShot(R.id.interview);
        }
        if (v.getId() == R.id.shot4) {

            storeDetails();
            edit.putString(angleName, shot4.getText().toString());
            edit.commit();
            String eventID = prefs
                    .getString(shot4.getText().toString(), "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.shot4);

        }
        if (v.getId() == R.id.shot5) {

            storeDetails();
            edit.putString(angleName, shot5.getText().toString());
            edit.commit();
            String eventID = prefs
                    .getString(shot5.getText().toString(), "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.shot5);

        }
        if (v.getId() == R.id.source1) { // System.out.println("source1");
            storeDetails();

            edit.putString(sourceName, source1.getText().toString());
            edit.commit();
            String eventID = prefs.getString(source1.getText().toString(),
                    "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.source1);
        }
        if (v.getId() == R.id.source2) { // System.out.println("source2");
            storeDetails();

            edit.putString(sourceName, source2.getText().toString());

            edit.commit();

            String eventID = prefs.getString(source2.getText().toString(),
                    "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();

            focusSelectedShot(R.id.source2);
        }
        if (v.getId() == R.id.source3) {
            // System.out.println("source3");
            storeDetails();

            edit.putString(sourceName, source3.getText().toString());

            edit.commit();

            String eventID = prefs.getString(source3.getText().toString(),
                    "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.source3);
        }
        if (v.getId() == R.id.source4) {
            // System.out.println("source3");
            storeDetails();

            edit.putString(sourceName, source4.getText().toString());

            edit.commit();

            String eventID = prefs.getString(source4.getText().toString(),
                    "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.source4);
        }
        if (v.getId() == R.id.source5) {
            // System.out.println("source3");

            storeDetails();
            edit.putString(sourceName, source5.getText().toString());

            edit.commit();

            String eventID = prefs.getString(source5.getText().toString(),
                    "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.source5);
        }
        if (v.getId() == R.id.source6) {
            // System.out.println("source3");
            storeDetails();

            edit.putString(sourceName, source6.getText().toString());

            edit.commit();

            String eventID = prefs.getString(source6.getText().toString(),
                    "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            focusSelectedShot(R.id.source6);
        }
    }

    private void focusSelectedShot(int id) {
        for (int i = 0; i < 6; i++) {
            if (selectedShotList.get(i).getId() == id) {
                selectedShotList.get(i).setTextColor(
                        getResources().getColor(android.R.color.white));
            } else {
                selectedShotList.get(i).setTextColor(
                        getResources().getColor(android.R.color.black));
            }
            if (selectedSourceList.get(i).getId() == id) {
                selectedSourceList.get(i).setTextColor(
                        getResources().getColor(android.R.color.white));
            } else {
                selectedSourceList.get(i).setTextColor(
                        getResources().getColor(android.R.color.black));
            }
        }

    }
}
