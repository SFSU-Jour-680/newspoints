package newspoints.sfsu.com.newsp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.VideoView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.adapters.FilesListAdapter;
import newspoints.sfsu.com.newsp.ui.AudioRecorderActivity;
import newspoints.sfsu.com.newsp.ui.VideoCaptureActivity;
import newspoints.sfsu.com.newsp_data.dao.RecordingDetailsDB;
import newspoints.sfsu.com.newsp_data.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.util.BaseFragment;


public class AllFilesFragment extends BaseFragment implements MediaController.MediaPlayerControl {
    private static Context activityContext;
    Boolean confirmDelete;
    VideoView video_player;
    DisplayMetrics dm;
    MediaController media_Controller;
    int height, width;
    int clipStartTime, clipEndTime;
    String projectName;
    RelativeLayout mRelativeLayoutVideoMain;
    ImageButton imageButton_close;
    Animation animSlideUp, animSlideDown;
    TextView txtviewHelp;
    private MediaPlayer mMediaPlayer;
    private Intent addProjectFiles;
    private ListView filesList;
    private FilesListAdapter adapter;

    public static AllFilesFragment newInstance(String text, Context context) {
        activityContext = context;
        AllFilesFragment f = new AllFilesFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            // assign the context to the activity
            activityContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " No parent Activity found");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.project_file_list_fragment, null);
        filesList = (ListView) v.findViewById(R.id.content);
        filesList.setLongClickable(true);
        txtviewHelp = (TextView) v.findViewById(R.id.txtView_help);

        // initialize the DB class in RecordingDetailsDB
        ProjectConstants.dbClass = new RecordingDetailsDB(activityContext);

        if (getActivity().getIntent().getExtras() != null) {
            // get the project name from Intent Extras
            projectName = getActivity().getIntent().getExtras().getString("foldername");
        }

        // get the layout and media player identify the layout
        mRelativeLayoutVideoMain = (RelativeLayout) v.findViewById(R.id.videoLayout);
        video_player = (VideoView) v.findViewById(R.id.videoView_allFilesVideo);
        imageButton_close = (ImageButton) v.findViewById(R.id.imageButton_close);

        // load the animations
        animSlideUp = AnimationUtils.loadAnimation(activityContext, R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(activityContext, R.anim.slide_down);

        media_Controller = new MediaController(activityContext);
        dm = new DisplayMetrics();
        // since we already have the VideoView added, simply remove the view and add it again.
        mRelativeLayoutVideoMain.removeView(video_player);
        // add video to layout
        mRelativeLayoutVideoMain.addView(video_player);

        //initialize the details to feed into VideoView
        height = dm.heightPixels;
        width = dm.widthPixels;
        video_player.setMinimumWidth(width);
        video_player.setMinimumHeight(height);

        video_player.setMediaController(media_Controller);

        // finally set the visibility of the layout and button to Invisible
        mRelativeLayoutVideoMain.setVisibility(View.GONE);
        imageButton_close.setVisibility(View.GONE);


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
        ProjectConstants.dbClass.getFilesList(ProjectConstants.selectedProjectName, AllFilesFragment.this);
        if (recordingId.length == 0) {
            filesList.setVisibility(View.GONE);
            txtviewHelp.setVisibility(View.VISIBLE);
        } else {
            filesList.setVisibility(View.VISIBLE);
            txtviewHelp.setVisibility(View.GONE);
        }


        // setOnClickListener for the ListView item click.
        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get the absolute path for the Selected MyVideo
                String absolutePathToVideo = ProjectConstants.getAbsolutePathToVideo(projectName);

                // call db to get the data inorder to inflate the ListView
                ProjectConstants.dbClass.getFilesList(ProjectConstants.selectedProjectName, AllFilesFragment.this);

                String selectedVideoId = recordingId[position];
                //int clipCount = ProjectConstants.dbClass.getClipCountFromRecording(selectedVideoId);

                // once you have the path to video, you need to append the name of MyVideo to the path
                // in order to build the complete path to video to feed in to VideoView
                final String fullVideoPath = absolutePathToVideo + ProjectConstants.VIDEO_RECORDING_ID + selectedVideoId + ".mp4";

                //Toast.makeText(activityContext, fullVideoPath, Toast.LENGTH_LONG).show();

                // finally render the video in the Videoview
                ((AppCompatActivity) activityContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

                // set the Visibility of the View to be Visible
                mRelativeLayoutVideoMain.setVisibility(View.VISIBLE);
                imageButton_close.setVisibility(View.VISIBLE);

                // Animate the View by X and Y
                mRelativeLayoutVideoMain.setAnimation(animSlideDown);

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
                int counter = 0;
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

                //Toast.makeText(activityContext, startPos + " : " + selectedPosition, Toast.LENGTH_SHORT).show();

//                Toast.makeText(activityContext, selectedPosition + " :" + offset + " -> " + clipStartTime + " - " + clipEndTime + " : " + finalClipDuration, Toast.LENGTH_SHORT).show();
                // final duration of clip in ms
                finalClipDuration = finalClipDuration * 1000;

                video_player.requestFocus();

                // seekTo the specified position
                video_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayer = mp;
                        video_player.seekTo(clipStartTime * 1000);
                    }
                });

//                Toast.makeText(activityContext, ProjectConstants.VIDEO_RECORDING_ID + selectedVideoId + "; " + clipStartTime + " - " + clipEndTime + " sec.", Toast.LENGTH_LONG).show();


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

        // Item on long click listener
        filesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            private String timeText;
            private String itemIdText;
            private TextView recordingIdView;

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int pos, long id) {
                recordingIdView = (TextView) v.findViewById(R.id.recording_id);
                TextView item_id = (TextView) v.findViewById(R.id.item_id);
                TextView time = (TextView) v.findViewById(R.id.time);
                String recordingIdText = recordingIdView.getText().toString();
                itemIdText = item_id.getText().toString();
                timeText = time.getText().toString();
                int i = recordingIdText.indexOf(":", 0);
                i++;

                // alert dialog
                new AlertDialog.Builder(activityContext)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ProjectConstants.dbClass.deletePaticularRecording(ProjectConstants.selectedProjectName, itemIdText, timeText);
                                        ProjectConstants.dbClass.getFilesList(ProjectConstants.selectedProjectName, AllFilesFragment.this);
                                        adapter = new FilesListAdapter(activityContext, projectId, recordingId, endTime, eventId, type, imageUri, createdDate, projectName);

                                        filesList.setAdapter(adapter);
                                        confirmDelete = true;
                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDelete = false;
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                return true;
            }
        });

        // close image button
        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_player.stopPlayback();
                video_player.clearFocus();
                mRelativeLayoutVideoMain.setVisibility(View.GONE);
                imageButton_close.setVisibility(View.GONE);
                // provide anim slide up
                mRelativeLayoutVideoMain.setAnimation(animSlideUp);
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
    public void setListViewAdapterArgs(String[] projectID, String[] recordingID, String[]
            endTime, String[] eventID, String[] type) {

        projectId = projectID;
        recordingId = recordingID;
        this.endTime = endTime;
        eventId = eventID;
        this.type = type;

    }

    @Override
    public void onPause() {
        super.onPause();
        this.getView().invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();

        // initialize the DB class in RecordingDetailsDB
        ProjectConstants.dbClass = new RecordingDetailsDB(activityContext);
        projectName = ProjectConstants.selectedProjectName;

        try {
            projectId = null;
            recordingId = null;
            this.endTime = null;
            eventId = null;
            this.type = null;

            ProjectConstants.dbClass.getFilesList(ProjectConstants.selectedProjectName, AllFilesFragment.this);
            adapter = new FilesListAdapter(activityContext, projectId, recordingId, endTime, eventId, type, imageUri, createdDate, projectName);

            filesList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
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

    @Override
    public void start() {

    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
