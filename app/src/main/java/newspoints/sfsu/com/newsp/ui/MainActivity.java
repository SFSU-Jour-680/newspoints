package newspoints.sfsu.com.newsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.fragments.DashboardFragment;


/**
 * Container activity for {@link DashboardFragment}. This activity will be displayed
 * when the user open the application after registration/login.
 */
public class MainActivity extends MainBaseActivity implements DashboardFragment.IDashboardCallbacks {

    public static final String KEY_CREATE_PROJECT = "create_new_project";
    private static final String TAG = "~!@#$MainAct";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        try {
            fragmentManager = getSupportFragmentManager();

            if (findViewById(R.id.main_fragment_container) != null) {
                if (savedInstanceState != null) {
                    return;
                }

                // open Dashboard when the user is successfully logged in
                if (getIntent().getIntExtra(HomeActivity.KEY_SIGNIN_SUCCESS, 0) == 1) {
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    performAddFragmentTransaction(dashboardFragment);
                }
                // open Dashboard when the user is successfully Registered
                else if (getIntent().getIntExtra(HomeActivity.KEY_REGISTRATION_SUCCESS, 0) == 1) {
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    performAddFragmentTransaction(dashboardFragment);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "onCreate|Catch: " + e.getMessage());
        }

    }


    private void performAddFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_fragment_container, fragment);
        transaction.commit();
    }

    private void performReplaceFragmentTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClickFabCaptureMedia() {
        Intent openMediaActivityIntent = new Intent(MainActivity.this, AllMediaActivity.class);
        startActivity(openMediaActivityIntent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
