package newspoints.sfsu.com.newsp.ui.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp_lib.util.AppKeys;
import newspoints.sfsu.com.newsp_lib.util.PermissionUtils;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "~!@#$RegisterFrag";
    private static final int INTERNET_PERMISSION = 0x1011;
    @Bind(R.id.editText_register_name)
    TextInputEditText et_name;
    @Bind(R.id.editText_register_email)
    TextInputEditText et_email;
    @Bind(R.id.editText_register_password)
    TextInputEditText et_password;
    @Bind(R.id.editText_register_confirm_password)
    TextInputEditText et_confirmPassword;
    @Bind(R.id.button_register_register)
    Button btn_register;
    @Bind(R.id.checkbox_privacyAgreement)
    CheckBox checkBox_agreement;
    private IRegisterCallbacks mInterface;
    private Context mContext;
    private Firebase mFirebaseRef;
    private PermissionUtils mPermissionUtils;
    private ProgressDialog mProgressDialog;
    private String email, password;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize Firebase.
        Firebase.setAndroidContext(mContext);
    }

    /**
     * Helper method to clear runtime permissions for user to capture image
     */
    private void askForPermission() {
        int hasInternetPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasInternetPermission = mContext.checkSelfPermission(Manifest.permission.INTERNET);

            List<String> permissions = new ArrayList<>();

            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), INTERNET_PERMISSION);
            }
        } else {
            Log.i(TAG, "askForPermission: not working");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case INTERNET_PERMISSION: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissionUtils.setPermission(PermissionUtils.INTERNET);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseRef = new Firebase(AppKeys.FIREBASE_APP_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, rootView);

        btn_register.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askForPermission();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IRegisterCallbacks) {
            mInterface = (IRegisterCallbacks) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IRegisterCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_register_register) {
            // show progress dialog
            mProgressDialog = ProgressDialog.show(mContext, "Registering user", "Please wait while we register you!");
            email = et_email.getText().toString().trim();
            password = et_password.getText().toString().trim();
            // call Firebase and create new user
            mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    // on success, login the user and show dashboard
                    mProgressDialog.dismiss();
                    loginUser();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    mProgressDialog.dismiss();
                    Log.i(TAG, "onError: " + firebaseError.getMessage());
                }
            });
        }
    }

    /**
     * Handles the login of the user after the registration is done. Once the login is successful, redirect user to display
     * Dashboard
     */
    private void loginUser() {
        mFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // TODO: store the user id, and user details in Database
                mInterface.onUserRegistrationSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(mContext, "User login failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Callback interface to handle onClicks and events in {@link RegisterFragment}
     */
    public interface IRegisterCallbacks {
        /**
         * Callback listener when the user clicks on the RegisterFragment button in {@link RegisterFragment} fragment. This method
         * will handle the calls for Registration. The callback is used to redirect user to {@link DashboardFragment} after
         * successful Registration
         */
        void onUserRegistrationSuccess();
    }
}
