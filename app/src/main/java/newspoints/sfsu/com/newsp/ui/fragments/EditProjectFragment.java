package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import newspoints.sfsu.com.newsp.R;


public class EditProjectFragment extends Fragment {


    private IEditProjectCallbacks mInterface;

    public EditProjectFragment() {
        // Required empty public constructor
    }

    public static EditProjectFragment newInstance(String projectId) {
        EditProjectFragment fragment = new EditProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_project, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IEditProjectCallbacks) {
            mInterface = (IEditProjectCallbacks) context;
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


    public interface IEditProjectCallbacks {
    }
}
