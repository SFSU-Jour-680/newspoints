package newspoints.sfsu.com.newsp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.adapters.FilesListAdapter;
import newspoints.sfsu.com.newsp.ui.AudioRecorderActivity;
import newspoints.sfsu.com.newsp.ui.VideoCaptureActivity;
import newspoints.sfsu.com.newsp_data.dao.RecordingDetailsDB;
import newspoints.sfsu.com.newsp_data.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.util.BaseFragment;

public class ShotFilesFragment extends BaseFragment {
    private static Context activityContext;
    final String eventType = "shot";
    SharedPreferences prefs;
    String categoryName, projectName;
    Boolean confirmDelete;
    List<String> selectionArg = new ArrayList<>();
    // Media control and VideoView
    RelativeLayout Animationlayout;
    ImageButton imageButton_close;
    Animation animSlideUp, animSlideDown;
    VideoView video_player;
    DisplayMetrics dm;
    MediaController media_Controller;
    int height, width;
    int clipStartTime, clipEndTime;
    TextView txtviewHelp;
    private Intent addProjectFiles;
    private ListView filesList;
    private FilesListAdapter adapter;
    private Set<String> temp;
    private Iterator<String> j;

    public static ShotFilesFragment newInstance(String text, Context context) {
        activityContext = context;
        ShotFilesFragment f = new ShotFilesFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.project_file_list_fragment,
                container, false);
        filesList = (ListView) v.findViewById(R.id.content);
        txtviewHelp = (TextView) v.findViewById(R.id.txtView_help);
        prefs = activityContext.getSharedPreferences(activityContext.getPackageName(), Context.MODE_PRIVATE);
        File categoryNamePath = new File(Environment.getExternalStorageDirectory() + "/journalist/"
                + ProjectConstants.selectedProjectName);
        String[] categoryListTemp = categoryNamePath.list();

        categoryName = categoryListTemp[0];
        temp = prefs.getStringSet(categoryName + "shot", null);

        try {
            j = temp.iterator();

            for (int i = 0; i < temp.size(); i++) {
                String tt = j.next();
                String yy = prefs.getString(tt, "000");
                selectionArg.add(yy);
            }

            ProjectConstants.dbClass.getFilteredFilesList(selectionArg, ProjectConstants.selectedProjectName,
                    ShotFilesFragment.this);
        } catch (NullPointerException e) {
            Log.d("=====>", "Null in Iterator in SHotFilesFragment");
        }
        if (projectId == null) {
            filesList.setVisibility(View.GONE);
        } else {
            filesList.setVisibility(View.VISIBLE);
            adapter = new FilesListAdapter(activityContext, projectId,
                    recordingId, endTime, eventId, type, imageUri, createdDate, eventType);

            adapter.notifyDataSetChanged();
            filesList.setAdapter(adapter);
        }

        // define the LongClickable
        filesList.setLongClickable(true);

        // set up the media player, videoview and Animations
        // initialize the DB class in RecordingDetailsDB
        ProjectConstants.dbClass = new RecordingDetailsDB(activityContext);

        if (getActivity().getIntent().getExtras() != null) {
            // get the project name from Intent Extras
            projectName = getActivity().getIntent().getExtras().getString("foldername");
        }

        // get the layout and media player identify the layout
        Animationlayout = (RelativeLayout) v.findViewById(R.id.videoLayout);
        video_player = (VideoView) v.findViewById(R.id.videoView_allFilesVideo);
        imageButton_close = (ImageButton) v.findViewById(R.id.imageButton_close);

        // load the animations
        animSlideUp = AnimationUtils.loadAnimation(activityContext, R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(activityContext, R.anim.slide_down);


        media_Controller = new MediaController(activityContext);
        dm = new DisplayMetrics();
        // since we already have the VideoView added, simply remove the view and add it again.
        Animationlayout.removeView(video_player);
        // add video to layout
        Animationlayout.addView(video_player);
        // finally set the visibility of the layout and button to Invisible
        Animationlayout.setVisibility(View.GONE);
        imageButton_close.setVisibility(View.GONE);


        // initialize FAB
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) v.findViewById(R.id.multiple_actions);

        // initializing the FAB and FAB Menu
        final FloatingActionButton actionB = (FloatingActionButton) v.findViewById(R.id.action_video);
        actionB.setTitle("Capture MyVideo");
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProjectFiles = new Intent(activityContext,
                        VideoCaptureActivity.class);

                startActivity(addProjectFiles);
                ((Activity) activityContext).finish();
            }
        });

        final FloatingActionButton actionA = (FloatingActionButton) v.findViewById(R.id.action_audio);
        actionA.setTitle("Capture NPAudio");
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProjectFiles = new Intent(activityContext,
                        AudioRecorderActivity.class);
                addProjectFiles.putExtra("foldername",
                        ProjectConstants.selectedProjectName);
                startActivity(addProjectFiles);
                ((Activity) activityContext).finish();
            }
        });

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // get the length of recordingId array
        ProjectConstants.dbClass.getFilesList(ProjectConstants.selectedProjectName, ShotFilesFragment.this);
        if (recordingId.length == 0) {
            filesList.setVisibility(View.GONE);
            txtviewHelp.setVisibility(View.VISIBLE);
        } else {
            filesList.setVisibility(View.VISIBLE);
            txtviewHelp.setVisibility(View.GONE);
        }

        // item click listener
        // setOnClickListener for the ListView item click.
        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get the absolute path for the Selected MyVideo
                String absolutePathToVideo = ProjectConstants.getAbsolutePathToVideo(projectName);

//                ProjectConstants.dbClass.getFilesList(ProjectConstants.selectedProjectName, ShotFilesFragment.this);
                String selectedVideoId = recordingId[position];
                String selectedVideoDuration = endTime[position];

                // once you have the path to video, you need to append the name of MyVideo to the path
                // in order to build the complete path to video to feed in to VideoView
                final String fullVideoPath = absolutePathToVideo + ProjectConstants.VIDEO_RECORDING_ID + selectedVideoId + ".mp4";

                //Toast.makeText(activityContext, fullVideoPath, Toast.LENGTH_LONG).show();

                // finally render the video in the Videoview
                ((ActionBarActivity) activityContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

                // set the Visibility of the View to be Visible
                Animationlayout.setVisibility(View.VISIBLE);
                imageButton_close.setVisibility(View.VISIBLE);

                // Animate the View by X and Y
                Animationlayout.setAnimation(animSlideDown);

                //initialize the details to feed into VideoView
                height = dm.heightPixels;
                width = dm.widthPixels;
                video_player.setMinimumWidth(width);
                video_player.setMinimumHeight(height);

                video_player.setMediaController(media_Controller);

                // Most IMP step -> set the full video path to feed in to the VideoView
                video_player.setVideoPath(fullVideoPath);

                // get the List of durations from the endTime
                int videoDurations[] = getVideoDurations(endTime);
                int finalClipDuration = 0;
                int listSize = videoDurations.length;
                clipStartTime = 0;
                int totalStartTime = 0;

                // now since we have the position and the clipCpunt for each recording, we need to develop logic
                // for each clip's position in the Listview. For that, we will build an int array.
                int startPos = getStartingPosition(selectedVideoId);
                //Toast.makeText(activityContext, startPos + " : " + position, Toast.LENGTH_LONG).show();

                // this parameter is used to get the index of each clip in list separated by RecordingId
                // so for all the clips in a specific recording, index will remain same.
                // offset will specify the relative position of each clip in recordings
                int offset = position - startPos;
                // we need counter to loop through offset
                int counter = 1;
                int selectedPosition = position;

                if (listSize != 0 && videoDurations != null) {
                    if (position == 0) {
                        finalClipDuration = videoDurations[0];
                        clipStartTime = 0;
                        clipEndTime = finalClipDuration;
                    } else {
                        /**
                         * This logic is self explanatory. So when user selects lets say 2nd video in MyVideo Recording 2
                         * and if MyVideo recording 2 contains only the 2 videos, then the counter will loop 2 times
                         * starting from the position selected in order to get the starting time of the video.
                         * So in this case, 2 parameters are taken into account: total number of clips of each recording
                         * and the selected position of the clip in the ListView.
                         */

                        if (offset == 0) {
                            clipStartTime = 0;
                        }
                        if (offset > 0) {
                            while (counter < offset) {
                                totalStartTime = totalStartTime + videoDurations[selectedPosition - 1];
                                counter++;
                                selectedPosition--;
                            }
                            clipStartTime = totalStartTime + 1;
                        }
                        finalClipDuration = videoDurations[position];
                        // we don't need end time, its just for future references
                        clipEndTime = clipStartTime + finalClipDuration - 1;
                    }
                }

                //Toast.makeText(activityContext, clipStartTime + " : " + finalClipDuration, Toast.LENGTH_LONG).show();
                //Toast.makeText(activityContext, finalClipDuration + " sec.", Toast.LENGTH_LONG).show();

                // final duration of clip in ms
                finalClipDuration = finalClipDuration * 1000;

                video_player.requestFocus();

                // seekTo the specified position
                video_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        video_player.seekTo(clipStartTime * 1000);
                    }
                });

                Toast.makeText(activityContext, ProjectConstants.VIDEO_RECORDING_ID + selectedVideoId + "; " + clipStartTime + " - " + clipEndTime + " sec.", Toast.LENGTH_LONG).show();


                // finally start the playback.
                CountDownTimer video_Playback_Counter = new CountDownTimer(finalClipDuration, 1000) {
                    // stop the playback of the video player.
                    public void onTick(long millisUntilFinished) {
                        video_player.start();
                    }

                    // stop the playback of the video player.
                    public void onFinish() {
                        video_player.stopPlayback();
                    }
                };
                video_Playback_Counter.start();

            }
        });


        // item long click listener
        filesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            private String timeText;
            private String recordingID;
            private String temprecordingID;
            private String itemIdText;
            private TextView recordingIdView;

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int pos, long id) { // TODO Auto-generated method
                // stub TextView
                recordingIdView = (TextView) v
                        .findViewById(R.id.recording_id);
                TextView item_id = (TextView) v
                        .findViewById(R.id.item_id);
                TextView time = (TextView) v.findViewById(R.id.time);
                String recordingIdText = recordingIdView.getText()
                        .toString();
                itemIdText = item_id.getText().toString();
                timeText = time.getText().toString();
                int i = recordingIdText.indexOf(":", 0);
                i++;
                recordingID = recordingIdText.substring(i);
                temprecordingID = recordingID.trim();
                new AlertDialog.Builder(activityContext)
                        .setTitle("Delete entry")
                        .setMessage(
                                "Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        ProjectConstants.dbClass
                                                .deletePaticularRecording(
                                                        ProjectConstants.selectedProjectName,
                                                        itemIdText,
                                                        timeText);
                                        ProjectConstants.dbClass
                                                .getFilteredFilesList(
                                                        selectionArg,
                                                        ProjectConstants.selectedProjectName,
                                                        ShotFilesFragment.this);
                                        adapter = new FilesListAdapter(
                                                activityContext,
                                                projectId, recordingId,
                                                endTime, eventId, type,
                                                imageUri, createdDate, projectName);

                                        filesList.setAdapter(adapter); // continue
                                        // with
                                        // delete
                                        confirmDelete = true;
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        // donothing
                                        confirmDelete = false;
                                    }
                                })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });

        // on click of ImageButton
        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_player.stopPlayback();
                Animationlayout.setVisibility(View.GONE);
                imageButton_close.setVisibility(View.GONE);

                Animationlayout.setAnimation(animSlideUp);
            }
        });

    }

    // get all values in  int
    private int[] getVideoDurations(String[] endTime) {
        if (endTime.length != 0) {
            int[] durations = new int[endTime.length];
            for (int i = 0; i < endTime.length; i++) {
                String[] actualValue = endTime[i].split(":");
                int duration = Integer.valueOf(actualValue[1]).intValue();
                durations[i] = duration;
            }
            return durations;
        }

        return null;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        this.getView().invalidate();
    }

    @Override
    public void setListViewAdapterArgs(String[] projectID, String[] recordingID, String[] endTime, String[] eventID,
                                       String[] type) {
        projectId = projectID;
        recordingId = recordingID;
        this.endTime = endTime;
        eventId = eventID;
        this.type = type;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            projectId = null;
            recordingId = null;
            this.endTime = null;
            eventId = null;
            this.type = null;

            ProjectConstants.dbClass.getFilteredFilesList(selectionArg, ProjectConstants.selectedProjectName,
                    ShotFilesFragment.this);

            adapter = new FilesListAdapter(activityContext, projectId,
                    recordingId, endTime, eventId, type, imageUri, createdDate, projectName);

            filesList.setAdapter(adapter);

        } catch (NullPointerException e) {
            Log.d("=====>", e.getMessage());
        }
    }


    /**
     * method to get the index of each video recording
     *
     * @param selectedVideoId
     * @return
     */
    public int getStartingPosition(String selectedVideoId) {
        //Toast.makeText(activityContext, "1: " + selectedVideoId, Toast.LENGTH_SHORT).show();

        int mainVideoId = Integer.valueOf(selectedVideoId).intValue();
        //Toast.makeText(activityContext, "2: " + mainVideoId, Toast.LENGTH_SHORT).show();
        int startingPosition = 0;
        int counter = 1, tempVideoId = 1;

        if (mainVideoId == 1) {
            startingPosition = 0;
            //Toast.makeText(activityContext, "3: sp: " + startingPosition, Toast.LENGTH_SHORT).show();
        } else {
            int count = 0;
            while (counter < mainVideoId) {
                String paramVideoId = String.valueOf(tempVideoId).toString();
                //Toast.makeText(activityContext, "3A: pvid " + paramVideoId, Toast.LENGTH_SHORT).show();
                count = ProjectConstants.dbClass.getClipCountFromRecording(paramVideoId);
                //Toast.makeText(activityContext, "3B: count " + count, Toast.LENGTH_SHORT).show();
                startingPosition = startingPosition + count;
                tempVideoId++;
                counter++;
            }
        }
        //Toast.makeText(activityContext, "4: Fsp: " + startingPosition, Toast.LENGTH_SHORT).show();
        return startingPosition;
    }
}
