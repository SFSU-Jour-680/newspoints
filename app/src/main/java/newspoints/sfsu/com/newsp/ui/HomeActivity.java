package newspoints.sfsu.com.newsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.session.SessionManager;
import newspoints.sfsu.com.newsp.ui.fragments.HomeFragment;
import newspoints.sfsu.com.newsp.ui.fragments.LoginFragment;
import newspoints.sfsu.com.newsp.ui.fragments.LogoutFragment;
import newspoints.sfsu.com.newsp.ui.fragments.PasswordResetFragment;
import newspoints.sfsu.com.newsp.ui.fragments.RegisterFragment;

/**
 * HomeActivity is the container Activity for {@link LoginFragment} and {@link RegisterFragment}. Both these Fragments are displayed when the user first opens the app for the first time. Upon
 * successful registration, login the user is redirected to the {@link newspoints.sfsu.com.newsp.ui.fragments.DashboardFragment}
 */
public class HomeActivity extends AppCompatActivity
        implements HomeFragment.IHomeCallbacks,
        LoginFragment.ILoginCallback,
        RegisterFragment.IRegisterCallbacks,
        LogoutFragment.ILogoutCallBack {

    public static final String KEY_SIGNIN_SUCCESS = "user_signin_success";
    public static final String KEY_REGISTRATION_SUCCESS = "user_registration_success";
    private static final String TAG = "~!@#$HomeAct";
    private SessionManager mSessionManager;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();

        mSessionManager = new SessionManager(this);

        // if Fragment container is present
        if (findViewById(R.id.homeAct_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // if the user clicked logout, then open LogoutFragment fragment
            if (getIntent().getIntExtra(MainBaseActivity.KEY_LOGOUT, 0) == 1) {
                LogoutFragment mLogoutFragment = new LogoutFragment();
                performReplaceFragmentTransaction(mLogoutFragment, false, false);
            } else {
                // depending on whether the Session is set for current user or not.
                if (mSessionManager.isLoggedIn()) {
                    displaySplashScreen();
                    //userLoggedIn();
                } else {
                    // use case when the session expires for current user.
                    HomeFragment mHomeFragment = new HomeFragment();
                    performAddFragmentTransaction(mHomeFragment, false);
                }
            }
        }
    }

    // TODO: change the intent
    private void displaySplashScreen() {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finish();
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }

    private void performAddFragmentTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.homeAct_fragment_container, fragment);
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    private void performReplaceFragmentTransaction(Fragment fragment, boolean addToBackStack, boolean animate) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.homeAct_fragment_container, fragment);
        if (animate)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoginClicked() {
        LoginFragment loginFragment = new LoginFragment();
        performAddFragmentTransaction(loginFragment, true);
    }

    @Override
    public void onSignUpClicked() {
        RegisterFragment registerFragment = new RegisterFragment();
        performAddFragmentTransaction(registerFragment, true);
    }

    @Override
    public void userLoggedIn() {
        Intent dashboardIntent = new Intent(HomeActivity.this, MainActivity.class);
        dashboardIntent.putExtra(KEY_SIGNIN_SUCCESS, 1);
        startActivity(dashboardIntent);
        finish();
    }

    @Override
    public void resetPassword() {
        PasswordResetFragment mPasswordResetFragment = new PasswordResetFragment();
        performReplaceFragmentTransaction(mPasswordResetFragment, true, true);
    }

    @Override
    public void onUserRegistrationSuccess() {
        Intent mainActivityIntent = new Intent(HomeActivity.this, MainActivity.class);
        mainActivityIntent.putExtra(KEY_REGISTRATION_SUCCESS, 1);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void userLoggedOut() {
        performReplaceFragmentTransaction(new HomeFragment(), false, false);
    }
}
