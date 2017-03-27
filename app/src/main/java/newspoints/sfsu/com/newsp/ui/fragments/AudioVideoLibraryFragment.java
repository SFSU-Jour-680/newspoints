package newspoints.sfsu.com.newsp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import newspoints.sfsu.com.newsp.R;

public class AudioVideoLibraryFragment extends Fragment {

    public AudioVideoLibraryFragment() {
        // Required empty public constructor
    }

    public static AudioVideoLibraryFragment newInstance(String param1, String param2) {
        AudioVideoLibraryFragment fragment = new AudioVideoLibraryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_library, container, false);
    }

}
