package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;

/**
 * Holds Weak references to {@link LoginFragment} and {@link RegisterFragment}
 */
public class HomeFragment extends Fragment {

    public final String TAG = "~!@#$HomeFragment";
    // TextView
    @Bind(R.id.textView_home_login)
    TextView txtView_login;
    @Bind(R.id.textView_home_signUp)
    TextView txtView_signUp;

    private IHomeCallbacks mInterface;
    private Context mContext;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);

        txtView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onLoginClicked();
            }
        });

        txtView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onSignUpClicked();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IHomeCallbacks) {
            mInterface = (IHomeCallbacks) context;
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


    public interface IHomeCallbacks {
        /**
         * Callback method when the user clicks on the <tt>LoginFragment</tt> button in {@link HomeFragment}
         */
        public void onLoginClicked();

        /**
         * Callback method when the user clicks on the <tt>RegisterFragment</tt> button in {@link HomeFragment}
         */
        public void onSignUpClicked();
    }
}
