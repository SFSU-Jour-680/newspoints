package newspoints.sfsu.com.newsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.fragments.CreateProjectFragment;
import newspoints.sfsu.com.newsp.ui.fragments.EditProjectFragment;
import newspoints.sfsu.com.newsp.ui.fragments.ProjectDetailsFragment;
import newspoints.sfsu.com.newsp.ui.fragments.ProjectListFragment;
import newspoints.sfsu.com.newsp_data.entities.Project;


/**
 * ProjectActivity is the container Activity for Projects created by user. Each Project can be seen in List View as well as
 * Maps View. The project created by user contains MyVideo and NPAudio files.
 */
public class ProjectActivity extends MainBaseActivity implements
        CreateProjectFragment.ICreateProjectCallbacks,
        ProjectDetailsFragment.IProjectDetailsCallback,
        ProjectListFragment.IProjectListCallbacks,
        EditProjectFragment.IEditProjectCallbacks {

    public final static String KEY_UPLOAD_PROJECT = "upload_project_to_drive";
    public static final String KEY_CAPTURE_VIDEO = "capture_video_for_current_project";
    public static final String KEY_RECORD_AUDIO = "record_audio_for_current_project";
    private final static String TAG = "~!@#$ProjActivity";
    public Toolbar mainProjectActivityToolbar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project);

        try {

            mainProjectActivityToolbar = (Toolbar) findViewById(R.id.toolbar_top_base);

            fragmentManager = getSupportFragmentManager();
            if (findViewById(R.id.project_fragment_container) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                if (getIntent().getIntExtra(MainActivity.KEY_CREATE_PROJECT, 0) == 1) {
                    CreateProjectFragment createProjectFragment = new CreateProjectFragment();
                    performAddFragmentTransaction(createProjectFragment, 1);
                } else {

                    Project mProject = Project.createInstance();

                    ProjectListFragment projectListFragment = ProjectListFragment.newInstance(mProject);
                    performAddFragmentTransaction(projectListFragment, 0);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "onCreate: " + e.getMessage());
        }
    }

    private void performAddFragmentTransaction(Fragment fragment, int animationFlag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animationFlag == 1)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.project_fragment_container, fragment);
        transaction.commit();
    }

    private void performReplaceFragmentTransaction(Fragment fragment, int animationFlag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animationFlag == 1)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.project_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClickEditProjectDetails(String projectId) {
        EditProjectFragment editProjectFragment = EditProjectFragment.newInstance("");
        performReplaceFragmentTransaction(editProjectFragment, 1);
    }


    @Override
    public void onClickUploadProjectToCloud(Project mProject) {
        Intent uploadProjectIntent = new Intent(ProjectActivity.this, DriveUploadActivity.class);
        uploadProjectIntent.putExtra(KEY_UPLOAD_PROJECT, mProject);
        startActivity(uploadProjectIntent);
        finish();
    }

    @Override
    public void onClickCaptureAudio(long projectId) {

    }

    @Override
    public void onClickCaptureVideo(long projectId) {

    }

    @Override
    public void onClickOpenVideoLibrary(long projectId) {

    }

    @Override
    public void onClickOpenAudioLibrary(long projectId) {

    }

    @Override
    public void createProject(Project mNewProject) {
        ProjectDetailsFragment mProjectDetailsFragment = ProjectDetailsFragment.newInstance(mNewProject);
        performReplaceFragmentTransaction(mProjectDetailsFragment, 1);
    }

    @Override
    public void onProjectListItemClickListener(Project mProject) {
        ProjectDetailsFragment mProjectDetailsFragment = ProjectDetailsFragment.newInstance(mProject);
        performReplaceFragmentTransaction(mProjectDetailsFragment, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
