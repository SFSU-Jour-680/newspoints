package newspoints.sfsu.com.newsp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.fragments.DynamicQuestionsFragment;
import newspoints.sfsu.com.newsp.ui.fragments.DynamicSourcesFragment;


public class DynamicTagsActivity extends MainBaseActivity {

    public static final String KEY_SELECTED_VIDEO_SOURCES = "user_selected_video_sources";
    public static final String KEY_SELECTED_VIDEO_QUESTIONS = "user_selected_video_questions";
    public static final String KEY_SELECTED_AUDIO_SOURCES = "user_selected_audio_sources";
    public static final String KEY_SELECTED_AUDIO_QUESTIONS = "user_selected_audio_questions";
    private static final String TAG = "~!@#$DynTagsAct";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_tags);

        this.reportFullyDrawn();

        try {
            mFragmentManager = getSupportFragmentManager();

            if (findViewById(R.id.dynamicTags_fragment_container) != null) {
                if (savedInstanceState != null) {
                    return;
                }

                // Add Sources Tag to Video
                if (getIntent().getIntExtra(MediaCaptureActivity.KEY_ADD_NEW_VIDEO_SOURCE, 0) == 1) {
                    DynamicSourcesFragment dynamicSourcesFragment = new DynamicSourcesFragment();
                    performAddFragmentTransaction(dynamicSourcesFragment);
                }
                // add Questions to Video
                else if (getIntent().getIntExtra(MediaCaptureActivity.KEY_ADD_NEW_VIDEO_QUESTION, 0) == 1) {
                    DynamicQuestionsFragment dynamicQuestionsFragment = new DynamicQuestionsFragment();
                    performAddFragmentTransaction(dynamicQuestionsFragment);
                }
                // add Sources to NPAudio
                else if (getIntent().getIntExtra(MediaCaptureActivity.KEY_ADD_NEW_AUDIO_SOURCE, 0) == 1) {
                    DynamicSourcesFragment dynamicSourcesFragment = new DynamicSourcesFragment();
                    performAddFragmentTransaction(dynamicSourcesFragment);
                }
                // add Questions to NPAudio
                else if (getIntent().getIntExtra(MediaCaptureActivity.KEY_ADD_NEW_AUDIO_QUESTION, 0) == 1) {
                    DynamicQuestionsFragment dynamicQuestionsFragment = new DynamicQuestionsFragment();
                    performAddFragmentTransaction(dynamicQuestionsFragment);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "onCreate|Catch: " + e.getMessage());
        }
    }

    private void performAddFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.dynamicTags_fragment_container, fragment);
        transaction.commit();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
//
//    @Subscribe
//    public void onSourcesSelectedSuccess(SourcesEvent.OnLoaded onLoaded) {
//        Log.i(TAG, "size :" + onLoaded.getSelectedList().size());
//        Intent captureVideoIntent = new Intent(DynamicTagsActivity.this, MediaCaptureActivity.class);
//        Bundle args = new Bundle();
//        args.putStringArrayList(KEY_SELECTED_VIDEO_SOURCES, onLoaded.getSelectedList());
//        captureVideoIntent.putExtra(KEY_SELECTED_VIDEO_SOURCES, args);
//        startActivity(captureVideoIntent);
//        finish();
//    }

}
