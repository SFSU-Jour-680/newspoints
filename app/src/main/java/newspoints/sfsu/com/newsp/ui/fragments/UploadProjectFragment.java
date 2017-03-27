package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp_data.entities.Project;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Initiates the operations to upload the project(files,audio and video) to GoogleDrive or Dropbox depending on User's
 * selection.The {@link UploadProjectFragment} provides a gateway to upload the project using the
 */
public class UploadProjectFragment extends Fragment {

    private static final String KEY_PROJECT_TO_UPLOAD = "project_to_upload";
    @Bind(R.id.cardview_uploadProj_googleDrive)
    CardView cardView_googleDrive;
    @Bind(R.id.cardview_uploadProj_dropbox)
    CardView cardView_dropbox;
    private IUploadProjectCallbacks mInterface;
    private Context mContext;
    private Project mProject;
    private Bundle args;

    public UploadProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Static instance creation of {@link UploadProjectFragment}
     *
     * @param mProject
     * @return
     */
    public static UploadProjectFragment newInstance(Project mProject) {
        UploadProjectFragment fragment = new UploadProjectFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_PROJECT_TO_UPLOAD, mProject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkNotNull(getArguments());
        args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload_project, container, false);

        ButterKnife.bind(this, rootView);

        mProject = args.getParcelable(KEY_PROJECT_TO_UPLOAD);

        cardView_dropbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.uploadProjectToDropbox(mProject);
            }
        });

        cardView_googleDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.uploadProjectToGoogleDrive(mProject);
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IUploadProjectCallbacks) {
            mInterface = (IUploadProjectCallbacks) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }


    /**
     * Callback interface to handle onClick of UploadProjectFragment
     */
    public interface IUploadProjectCallbacks {
        void uploadProjectToGoogleDrive(Project mProject);

        void uploadProjectToDropbox(Project mProject);
    }
}
