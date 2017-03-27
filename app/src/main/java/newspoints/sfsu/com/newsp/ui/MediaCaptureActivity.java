package newspoints.sfsu.com.newsp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.eventbus.Subscribe;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.fragments.AudioRecorderFragment;
import newspoints.sfsu.com.newsp.ui.fragments.DynamicQuestionsFragment;
import newspoints.sfsu.com.newsp.ui.fragments.DynamicSourcesFragment;
import newspoints.sfsu.com.newsp.ui.fragments.VideoCaptureFragment;
import newspoints.sfsu.com.newsp.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.bus.BusProvider;
import newspoints.sfsu.com.newsp_lib.event.QuestionsEvent;
import newspoints.sfsu.com.newsp_lib.event.SourcesEvent;

/**
 * This is the container Activity for holding VideoCapture and AudioRecording fragments. This Activity provides the container
 * layout in order to carry out Fragment transaction and to display the View
 */
public class MediaCaptureActivity extends AppCompatActivity implements
        VideoCaptureFragment.IVideoCaptureCallbacks,
        AudioRecorderFragment.IAudioRecorderCallbacks {

    public static final String KEY_ADD_NEW_VIDEO_SOURCE = "add_new_video_source_tag";
    public static final String KEY_VIDEO_SOURCES_SELECTED = "video_sources_selected";
    public static final String KEY_ADD_NEW_AUDIO_SOURCE = "add_new_audio_source_tag";
    public static final String KEY_VIDEO_QUESTIONS_SELECTED = "video_questions_seleected";
    public static final String KEY_ADD_NEW_VIDEO_QUESTION = "add_new_video_question_tag";
    public static final String KEY_AUDIO_SOURCES_SELECTED = "audio_sources_selected";
    public static final String KEY_ADD_NEW_AUDIO_QUESTION = "add_new_audio_question_tag";
    public static final String KEY_AUDIO_QUESTION_SELECTED = "audio_question_selected";
    private static final String TAG = "~!@#$MediaCaptureAct";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_capture);

        BusProvider.bus().register(this);

        try {
            mFragmentManager = getSupportFragmentManager();


            if (findViewById(R.id.mediaCapture_fragment_container) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                // open VideoCaptureFragment
                if (getIntent().getIntExtra(ProjectActivity.KEY_CAPTURE_VIDEO, 0) == 1) {
                    VideoCaptureFragment mVideoCaptureFragment = VideoCaptureFragment.newInstance(ProjectActivity
                            .KEY_CAPTURE_VIDEO);
                    performAddFragmentTransaction(mVideoCaptureFragment);
                }

                // open AudioRecorderFragment
                else if (getIntent().getIntExtra(ProjectActivity.KEY_RECORD_AUDIO, 0) == 1) {
                    AudioRecorderFragment mAudioRecorderFragment = AudioRecorderFragment.newInstance(ProjectActivity
                            .KEY_RECORD_AUDIO);
                    performAddFragmentTransaction(mAudioRecorderFragment);
                } else // open VideoCaptureFragment
                    if (getIntent().getIntExtra(DemoActivity.KEY_OPEN_VIDEO, 0) == 1) {
                        VideoCaptureFragment mVideoCaptureFragment = new VideoCaptureFragment();
                        performAddFragmentTransaction(mVideoCaptureFragment);
                    }
            }
        } catch (Exception e) {
            Log.i(TAG, "onCreate|Catch: " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void performAddFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.mediaCapture_fragment_container, fragment);
        transaction.commit();
    }

    private void performReplaceFragmentTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.mediaCapture_fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusProvider.bus().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count == 0) {
            finish();
        } else if (count > 0) {
            mFragmentManager.popBackStack();
        }
        super.onBackPressed();
    }

    @Override
    public void addSourcesToVideoDynamically() {
        DynamicSourcesFragment mDynamicSourcesFragment = DynamicSourcesFragment.newInstance(ProjectConstants.VIDEO_RECORD_CODE);
        performReplaceFragmentTransaction(mDynamicSourcesFragment, true);
    }

    @Override
    public void addQuestionsToVideoDynamically() {
        DynamicQuestionsFragment mDynamicQuestionsFragment = DynamicQuestionsFragment.newInstance(ProjectConstants.VIDEO_RECORD_CODE);
        performReplaceFragmentTransaction(mDynamicQuestionsFragment, true);
    }

    @Override
    public void attachSourcesToAudioDynamically() {
        DynamicSourcesFragment mDynamicSourcesFragment = DynamicSourcesFragment.newInstance(ProjectConstants.AUDIO_CAPTURE_CODE);
        performReplaceFragmentTransaction(mDynamicSourcesFragment, true);
    }

    @Override
    public void attachQuestionsToAudioDynamically() {
        DynamicQuestionsFragment mDynamicQuestionsFragment = DynamicQuestionsFragment.newInstance(ProjectConstants.AUDIO_CAPTURE_CODE);
        performReplaceFragmentTransaction(mDynamicQuestionsFragment, true);
    }

    /**
     * Subscribes to the event of success in selecting Sources for Video
     *
     * @param onLoaded
     */
    @Subscribe
    public void onSourcesSelected(SourcesEvent.OnVideoSourcesLoaded onLoaded) {
        VideoCaptureFragment mVideoCaptureFragment = VideoCaptureFragment.newInstance(KEY_VIDEO_SOURCES_SELECTED,
                onLoaded.getSelectedList());
        performReplaceFragmentTransaction(mVideoCaptureFragment, false);
    }

    /**
     * Subscribes to the event of success in selecting Sources for Audio
     *
     * @param onLoaded
     */
    @Subscribe
    public void onSourcesSelected(SourcesEvent.OnAudioSourcesLoaded onLoaded) {
        AudioRecorderFragment mAudioRecorderFragment = AudioRecorderFragment.newInstance(KEY_AUDIO_SOURCES_SELECTED,
                onLoaded.getSelectedList());
        performReplaceFragmentTransaction(mAudioRecorderFragment, false);
    }

    /**
     * Subscribes to the event of success in selecting Questions for Video
     *
     * @param onLoaded
     */
    @Subscribe
    public void onQuestionsSelected(QuestionsEvent.OnVideoQuesLoaded onLoaded) {
        VideoCaptureFragment mVideoCaptureFragment = VideoCaptureFragment.newInstance(KEY_VIDEO_QUESTIONS_SELECTED,
                onLoaded.getSelectedList());
        performReplaceFragmentTransaction(mVideoCaptureFragment, false);
    }

    /**
     * Subscribes to the event of success in selecting Questions for Audio
     *
     * @param onLoaded
     */
    @Subscribe
    public void onQuestionsSelected(QuestionsEvent.OnAudioQuestionsLoaded onLoaded) {
        AudioRecorderFragment mAudioRecorderFragment = AudioRecorderFragment.newInstance(KEY_AUDIO_QUESTION_SELECTED,
                onLoaded.getSelectedList());
        performReplaceFragmentTransaction(mAudioRecorderFragment, false);
    }
}
