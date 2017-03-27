package newspoints.sfsu.com.newsp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import java.io.File;
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

public class AudioRecorderActivity extends Activity implements OnClickListener {
    static int pauseCount = 1;
    private static String CURRENT_EVENT = "CurrentEvent";
    public MediaRecorder audioRecorder;
    Button stop, pause, sourceAudio, shotAudio, interview, wide, extraWide, shot4, shot5, establish;
    ScrollView cameraTypeHorizontalScorll;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, recordTime, tv9, tv7, tv8;
    Boolean backPressed = false;
    Button source1, source2, source3, source4, source5, source6;
    List<Button> selectedSourceList;
    String picturePath;
    Boolean recording = false;
    File myStorageDir, newFileAfterPause;
    String sourceName = "SourceName", angleName = "Angle", type = "Audio", lastEndTime = null, endTime;
    Date lastEndDate, endDate;
    long startTime = 0;
    Editor edit;
    SharedPreferences prefs;
    int lastFocusedTextView = 0;
    int changeTextViewColorNumber = 0;
    Set source, question, shot;
    int recordingHour = 0, recordingMinute = 0, recordingSeconds;

    int count = 0;
    int hours, minutes, seconds;

    String recordingId = "1";
    String categorySelected;
    Button questionsList;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            recordingSeconds = (int) (millis / 1000);
            recordingSeconds = recordingSeconds % 60;
            if (recordingSeconds == 59) {
                recordingSeconds = 0;
                recordingMinute++;
                if (recordingMinute == 60) {
                    recordingHour++;
                }
            }
            recordTime.setText(String.format("%02d:%02d", recordingMinute,
                    recordingSeconds));
            if (!backPressed)
                timerHandler.postDelayed(this, 1000);
        }
    };
    private ScrollView sourceInfo;
    private ImageView selectedImage;
    private LinearLayout shotSourceTray;
    private String projectName;
    private boolean sourceVisible = false;

    private void findAllViewsToClass() {
        stop = (Button) findViewById(R.id.stop);
        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);
        tv3 = (TextView) findViewById(R.id.text3);
        tv4 = (TextView) findViewById(R.id.text4);
        tv5 = (TextView) findViewById(R.id.text5);
        tv6 = (EditText) findViewById(R.id.text6);
        tv7 = (TextView) findViewById(R.id.text7);
        tv9 = (TextView) findViewById(R.id.text9);
        tv8 = (TextView) findViewById(R.id.text8);

        tv1.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv2.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv3.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv4.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv5.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv6.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv9.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv8.setTypeface(ProjectConstants.ROBOT_REGULAR);
        tv7.setTypeface(ProjectConstants.ROBOT_REGULAR);

        questionsList = (Button) findViewById(R.id.drag_handle);
        shotAudio = (Button) findViewById(R.id.shot_audio);
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

        sourceInfo = (ScrollView) findViewById(R.id.source_info);
        shotSourceTray = (LinearLayout) findViewById(R.id.shot_source_tray);
        interview = (Button) findViewById(R.id.interview_audio);
        wide = (Button) findViewById(R.id.wide_audio);
        extraWide = (Button) findViewById(R.id.extrawide_audio);
        establish = (Button) findViewById(R.id.establish_audio);
        shot4 = (Button) findViewById(R.id.shot4);
        shot5 = (Button) findViewById(R.id.shot5);
        shotAudio.setTypeface(ProjectConstants.ROBOT_MEDIUM);

        recordTime = (TextView) findViewById(R.id.time);

        selectedImage = (ImageView) findViewById(R.id.selected_image);
    }

    private void registerClickListener() {
        stop.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        shotAudio.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        interview.setOnClickListener(this);
        establish.setOnClickListener(this);
        extraWide.setOnClickListener(this);
        wide.setOnClickListener(this);

        shot4.setOnClickListener(this);
        shot5.setOnClickListener(this);
        questionsList.setOnClickListener(this);

        source1.setOnClickListener(this);

        source2.setOnClickListener(this);
        source3.setOnClickListener(this);
        source4.setOnClickListener(this);
        source5.setOnClickListener(this);
        source6.setOnClickListener(this);

    }

    private void storeDetails() {

        if (!recording) {
            // String eventID = prefs.getString(CURRENT_EVENT, "");

            endTime = recordTime.getText().toString();
            long recordingDuration = recordingMinute * 60 + recordingSeconds;
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
            System.out.println("Event id is " + eventID);

            long recordingDuration = recordingDuration();
            Date endDate = AudioRecorderActivity.this.endDate;

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy  hh:mm");
            String formattedDate = df.format(new Date());

            if (!eventID.contains("404")) {
                if (hours == 0) {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, minutes + ":" + seconds, eventID,
                            "audio", picturePath, recordingDuration,
                            formattedDate);
                } else {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, "" + hours + ":" + minutes + ":"
                                    + seconds, eventID, "audio", picturePath,
                            recordingDuration, formattedDate);
                }
            } else {
                if (hours == 0) {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, minutes + ":" + seconds, eventID,
                            "audio", picturePath, recordingDuration,
                            formattedDate);
                } else {
                    ProjectConstants.dbClass.recordingDetails(selectedProject,
                            "" + recordingId, "" + hours + ":" + minutes + ":"
                                    + seconds, eventID, "audio", picturePath,
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        endDate = date1;

        long timeDiff = endDate.getTime() - lastEndDate.getTime();
        long convertedTimeDiff = timeDiff / 1000;

        String timeDiffDb = String.valueOf(convertedTimeDiff);
        int timeInSeconds = (int) (timeDiff / 1000);
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        return timeInSeconds;
    }

    public String calculateTimeDifference(String endTime, String lastEndTime) {

        String minutes = (String) endTime.subSequence(0, 2);
        String seconds = (String) endTime.subSequence(3, 5);

        String lastEndTimeMinutes = (String) lastEndTime.subSequence(0, 2);
        String lastEndTimeSeconds = (String) lastEndTime.subSequence(3, 5);

        return null;

    }

    private void initMediarecorder(int count) {

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
        newFileAfterPause = new File(myStorageDir + "/Audio Recording" + count
                + ".mp3");

        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioRecorder.setOutputFile(myStorageDir + "/Audio Recording" + count
                + ".mp3");

    }

    private void prepareMediaRecorder() {
        try {
            audioRecorder.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed = true;
        if (recording) {
            storeDetails();
            audioRecorder.stop();
            audioRecorder.release();
            timerHandler.removeCallbacks(timerRunnable);
        }

        Intent i = new Intent(AudioRecorderActivity.this, FilesListActivity.class);
        startActivity(i);
        finish();
    }

    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        shot = new HashSet<String>();
        source = new HashSet<String>();
        question = new HashSet<String>();

        findAllViewsToClass();
        registerClickListener();
        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        edit = prefs.edit();
        edit.putString(CURRENT_EVENT, "404");
        edit.commit();
        categorySelected = ProjectConstants.selectedProjectCategory;
        Intent intent = getIntent();
        projectName = intent.getStringExtra("foldername");
        picturePath = ProjectConstants.dbClass.getProjectImage(projectName);
        try {
            if (!picturePath.equalsIgnoreCase("False")) {
                selectedImage.setBackground(null);
                // selectedImage.setImageBitmap(getScaledBitmap(picturePath,
                // 800,
                // 800));// selectedImage.setBa
                Bitmap b = BitmapFactory.decodeFile(picturePath);
                selectedImage.setImageBitmap(b);
            }
        } catch (NullPointerException ignored) {

        }

        question = prefs.getStringSet(categorySelected + "question", null);
        shot = prefs.getStringSet(categorySelected + "shot", null);
        source = prefs.getStringSet(categorySelected + "source", null);
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
        audioRecorder = new MediaRecorder();
        prefs = getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
    }

    private String getEventId(String eventName) {
        String eventId = ProjectConstants.dbClass.getEventId(eventName);
        return eventId;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stop && recording) {
            storeDetails();
            shotAudio.setClickable(false);
            questionsList.setClickable(false);
            stop.setBackgroundResource(R.mipmap.ic_play_36);
            recording = false;
            pauseCount++;
            newFileAfterPause.renameTo(new File(myStorageDir + "/" + "Audio Recording " + recordingId + ".mp3"));
            String eventID = prefs.getString(CURRENT_EVENT, "404");

            audioRecorder.reset();
            backPressed = true;
            edit.putString(CURRENT_EVENT, "404");
            edit.commit();
        } else if (v.getId() == R.id.stop && !recording) {
            stop.setBackgroundResource(R.mipmap.ic_shot_white_24dp);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            Date date1 = null;
            try {
                date1 = df.parse(formattedDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            lastEndDate = date1;

            String selectedProject = ProjectConstants.selectedProjectName;

            String tempRecordingId = ProjectConstants.dbClass.getLastRecordingId(selectedProject, "audio");

            if (tempRecordingId == null || tempRecordingId.equals("") || tempRecordingId.equals("null")) {
                tempRecordingId = String.valueOf(2);
                recordingId = tempRecordingId;
            } else {
                int temp = Integer.parseInt(tempRecordingId);
                temp = temp + 1;
                recordingId = String.valueOf(temp);
            }
            backPressed = false;

            initMediarecorder(pauseCount);
            prepareMediaRecorder();
            audioRecorder.start();
            recording = true;
            startTime = System.currentTimeMillis();

            timerHandler.postDelayed(timerRunnable, 0);
            lastEndTime = null;

        }
        if (v.getId() == R.id.shot_audio) {
            if (!sourceVisible) {
                // sourceInfo.setVisibility(0);
                shotSourceTray.setVisibility(View.INVISIBLE);
                sourceInfo.setVisibility(View.INVISIBLE);
                sourceVisible = true;
            } else {
                sourceVisible = false;
                // sourceInfo.setVisibility(4);
                shotSourceTray.setVisibility(View.VISIBLE);

                sourceInfo.setVisibility(View.VISIBLE);
            }
        }

        if (v.getId() == R.id.text1) {
            storeDetails();

            String eventID = prefs.getString(tv1.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv1.setTextColor(getResources().getColor(android.R.color.white));

            tv6.setText(tv1.getText().toString());
            tv1.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text2) {
            storeDetails();

            String eventID = prefs.getString(tv2.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv2.setTextColor(getResources().getColor(android.R.color.white));

            tv6.setText(tv2.getText().toString());
            tv2.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text3) {
            storeDetails();

            String eventID = prefs.getString(tv3.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv3.setTextColor(getResources().getColor(android.R.color.white));

            tv6.setText(tv3.getText().toString());
            tv3.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text4) {
            storeDetails();

            String eventID = prefs.getString(tv4.getText().toString(), "0000");

            edit.putString(CURRENT_EVENT, eventID);
            tv4.setTextColor(getResources().getColor(android.R.color.white));
            tv6.setText(tv4.getText().toString());
            tv4.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text5) {
            storeDetails();

            String eventID = prefs.getString(tv5.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv5.setTextColor(getResources().getColor(android.R.color.white));

            tv6.setText(tv5.getText().toString());
            tv5.setBackgroundColor(0x3364FE2E);

        }
        if (v.getId() == R.id.text8) {

            storeDetails();
            String eventID = prefs.getString(tv8.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv8.setTextColor(getResources().getColor(android.R.color.white));
            tv6.setText(tv8.getText().toString());
            tv8.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text7) {

            storeDetails();
            String eventID = prefs.getString(tv7.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv7.getText().toString());
            tv7.setTextColor(getResources().getColor(android.R.color.white));
            tv7.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.text9) {

            storeDetails();
            String eventID = prefs.getString(tv9.getText().toString(), "0000");
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
            tv6.setText(tv9.getText().toString());
            tv9.setTextColor(getResources().getColor(android.R.color.white));
            tv9.setBackgroundColor(0x3364FE2E);
        }
        if (v.getId() == R.id.establish_audio) {
            storeDetails();

            edit.putString(angleName, establish.getText().toString());
            edit.commit();
            String eventID = prefs.getString(establish.getText().toString(),
                    "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();

        }
        if (v.getId() == R.id.extrawide_audio) {
            storeDetails();
            edit.putString(angleName, extraWide.getText().toString());
            edit.commit();
            String eventID = prefs.getString(extraWide.getText().toString(),
                    "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
        }
        if (v.getId() == R.id.wide_audio) {
            storeDetails();
            edit.putString(angleName, wide.getText().toString());
            edit.commit();

            String eventID = prefs.getString(wide.getText().toString(), "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
        }
        if (v.getId() == R.id.interview_audio) {
            storeDetails();
            edit.putString(angleName, interview.getText().toString());
            edit.commit();

            String eventID = prefs.getString(interview.getText().toString(),
                    "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();
        }
        if (v.getId() == R.id.shot4) {

            storeDetails();
            edit.putString(angleName, shot4.getText().toString());
            edit.commit();
            String eventID = prefs
                    .getString(shot4.getText().toString(), "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();

        }
        if (v.getId() == R.id.shot5) {

            storeDetails();
            edit.putString(angleName, shot5.getText().toString());
            edit.commit();
            String eventID = prefs.getString(shot5.getText().toString(), "" + 0);
            edit.putString(CURRENT_EVENT, eventID);
            edit.commit();

        }
        if (v.getId() == R.id.source1) { // System.out.println("source1");
            storeDetails();

            edit.putString(sourceName, source1.getText().toString());
            edit.commit();
            String eventID = prefs.getString(source1.getText().toString(), "0000");
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
