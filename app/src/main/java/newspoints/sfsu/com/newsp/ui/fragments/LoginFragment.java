package newspoints.sfsu.com.newsp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.session.SessionManager;
import newspoints.sfsu.com.newsp_lib.util.AppKeys;

/**
 * Allows user to login to the application. The login credentials entered by the user will be validated and compared to
 * corresponding entry in local DB made after registration. Upon successful match, the user will be redirected to the {@link
 * newspoints.sfsu.com.newsp.ui.MainActivity} which displays the {@link DashboardFragment}.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.button_login_login)
    Button btnLogin;
    @Bind(R.id.editText_login_email)
    TextInputEditText et_email;
    @Bind(R.id.editText_login_password)
    TextInputEditText et_password;
    @Bind(R.id.textView_login_forgotPassword)
    TextView txtView_forgotPassword;
    String email, password;
    private ILoginCallback mInterface;
    private Context mContext;
    private SessionManager mSessionManager;
    private Firebase myFirebaseRef;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(mContext);
    }

    @Override
    public void onStart() {
        super.onStart();
        myFirebaseRef = new Firebase(AppKeys.FIREBASE_APP_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        mSessionManager = new SessionManager(mContext);

        btnLogin.setOnClickListener(this);
        txtView_forgotPassword.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILoginCallback) {
            mInterface = (ILoginCallback) context;
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_login_login) {
            final ProgressDialog mProgressDialog = ProgressDialog.show(mContext, "Login", "Please wait until you are logged in!");
            mProgressDialog.show();
            email = et_email.getText().toString().trim();
            password = et_password.getText().toString().trim();

            // login the user with Firebase
            myFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // TODO: update the authToken
                    mInterface.userLoggedIn();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Toast.makeText(mContext, "User login failed", Toast.LENGTH_LONG).show();
                }
            });
        } else if (v.getId() == R.id.textView_login_forgotPassword) {
            mInterface.resetPassword();
        }
    }

    public interface ILoginCallback {
        /**
         * Callback method to handle the onclick of LoginFragment button in {@link LoginFragment} Fragment.
         */
        void userLoggedIn();

        /**
         * Callback to reset the user password for the email
         */
        void resetPassword();
    }
}
