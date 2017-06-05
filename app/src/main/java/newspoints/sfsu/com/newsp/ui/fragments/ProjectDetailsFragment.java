package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.ProjectActivity;
import newspoints.sfsu.com.newsp_data.entities.Project;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Displays the project details and allows user to add audio/video to the project.
 */
public class ProjectDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_NEW_PROJECT = "newly_created_project";
    @Bind(R.id.imageView_projDet_mainImage)
    ImageView imageView_mainImage;
    @Bind(R.id.textView_projDet_category)
    TextView txtView_category;
    @Bind(R.id.textView_projDet_projName)
    TextView txtView_projName;
    @Bind(R.id.textView_projDet_description)
    TextView txtView_description;
    @Bind(R.id.textView_projDet_videoCount)
    TextView txtView_videoCount;
    @Bind(R.id.textView_projDet_audioCount)
    TextView txtView_audioCount;
    @Bind(R.id.fab_projDet_captureVideo)
    FloatingActionButton fab_captureVideo;
    @Bind(R.id.fab_projDet_captureAudio)
    FloatingActionButton fab_captureAudio;
    @Bind(R.id.toolbar_projectDetail)
    Toolbar mProjDetToolbar;
    private IProjectDetailsCallback mInterface;
    private Project mProject;
    private Bundle bundle_proj;
    private Context mContext;


    public ProjectDetailsFragment() {
        // Required empty public constructor
    }

    // static method creation for displaying
    public static ProjectDetailsFragment newInstance(Project mNewlyCreatedProject) {
        ProjectDetailsFragment fragment = new ProjectDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_NEW_PROJECT, mNewlyCreatedProject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.bundle_proj = checkNotNull(getArguments());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_project_details, container, false);

        ButterKnife.bind(this, rootView);

        assert bundle_proj != null;

        mProject = bundle_proj.getParcelable(KEY_NEW_PROJECT);

        if (mProject != null) {
            //populate all the views
//            Picasso.with(mContext).load(mProject.getImage_url()).into(imageView_mainImage);
            txtView_category.setText(mProject.getCategory());
            txtView_projName.setText(mProject.getName());
            txtView_description.setText(mProject.getDescription());
            txtView_videoCount.setText("View 4 videos");
            txtView_audioCount.setText("View 6 audios");
        }
        fab_captureAudio.setOnClickListener(this);
        fab_captureVideo.setOnClickListener(this);
        txtView_audioCount.setOnClickListener(this);
        txtView_videoCount.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // hide the toolbar in ProjectActivity
        final Toolbar toolbar = ((ProjectActivity) getActivity()).mainProjectActivityToolbar;
        ProjectActivity mProjectActivity = (ProjectActivity) getActivity();
        mProjectActivity.getSupportActionBar().hide();

        // display the toolbar in this Fragment
        mProjectActivity.setSupportActionBar(mProjDetToolbar);
        mProjectActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //display title
        getActivity().setTitle(R.string.title_fragment_project_details);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IProjectDetailsCallback) {
            mInterface = (IProjectDetailsCallback) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
        inflater.inflate(R.menu.menu_project_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editProject();
                return true;
            case R.id.action_upload:
                uploadProject();
                return true;
        }
        return false;
    }

    /**
     * Handles the user click of uploading the project to GoogleDrive or Dropbox.
     */
    private void uploadProject() {
        mInterface.onClickUploadProjectToCloud(null);
    }

    /**
     * Opens the EditProjectFragment
     */
    private void editProject() {
        mInterface.onClickEditProjectDetails("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_projDet_captureAudio:
                mInterface.onClickCaptureAudio(mProject.getId());
                break;
            case R.id.fab_projDet_captureVideo:
                mInterface.onClickCaptureVideo(mProject.getId());
                break;
            case R.id.textView_projDet_audioCount:
                mInterface.onClickOpenAudioLibrary(mProject.getId());
                break;
            case R.id.textView_projDet_videoCount:
                mInterface.onClickOpenVideoLibrary(mProject.getId());
                break;
        }
    }

    /**
     * Callback interface to handle the onClick listeners in {@link ProjectDetailsFragment}
     */
    public interface IProjectDetailsCallback {

        /**
         * Callback to open {@link EditProjectFragment} in order to make some edits to the Project
         *
         * @param projectId
         */
        void onClickEditProjectDetails(String projectId);

        /**
         * Callback to handle the onClick of upload button. Opens {@link UploadProjectFragment}
         *
         * @param mProject
         */
        void onClickUploadProjectToCloud(Project mProject);

        /**
         * Callback to handle the onclick of the MyVideo FAB in {@link ProjectDetailsFragment}
         *
         * @param projectId
         */
        void onClickCaptureAudio(long projectId);

        /**
         * Callback to handle the onclic of the NPAudio FAB in {@link ProjectDetailsFragment}
         *
         * @param projectId
         */
        void onClickCaptureVideo(long projectId);

        /**
         * Callback to handle the onclick of the MyVideo library TextView in {@link ProjectDetailsFragment}
         *
         * @param projectId
         */
        void onClickOpenVideoLibrary(long projectId);

        /**
         * Callback to handle the onclick of the NPAudio library TextView in {@link ProjectDetailsFragment}
         *
         * @param projectId
         */
        void onClickOpenAudioLibrary(long projectId);


    }
}
