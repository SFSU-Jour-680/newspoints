package newspoints.sfsu.com.newsp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import newspoints.sfsu.com.newsp.R;


/**
 * This activity is used to specify the NavigationDrawer in all the activities. The business logic for the Navigation Drawer
 * is specified in this activity and all the Activities having NavDrawer will inherit the NavDrawer from this Activity.
 * <p/>
 * Created by Pavitra on 11/20/2015.
 */
public class MainBaseActivity extends AppCompatActivity {

    public static final String KEY_LOGOUT = "key_user_logout";
    private final String TAG = "~!@#$MainBaseAct";
    // InjectView is used to inject the UI controls using ButterKnife library.
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbarMain;
    private NavigationView mNavigationView;
    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbarMain = (Toolbar) findViewById(R.id.toolbar_top_base);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (toolbarMain != null) {
            setSupportActionBar(toolbarMain);

            // get the ActionBar and set the Menu icon.
            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            // set the onClick Listener for the Drawer click event
            toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // initialize the NavigationView and also setup OnClickListener for each of the Items in list
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectDrawerItem(menuItem);
                    return true;
                }
            });
        }

    }

    /**
     * Helper method to select the drawer item and open the Activity based on Account's selection.
     *
     * @param menuItem
     */
    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent;

        try {
            switch (menuItem.getItemId()) {
                case R.id.nav_dashboard:
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 0;
                    break;

                case R.id.nav_project:
                    intent = new Intent(this, ProjectActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 1;
                    break;

                case R.id.nav_cloud:
                    intent = new Intent(this, DriveUploadActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 2;
                    break;

                case R.id.nav_tutorial:
                    intent = new Intent(this, TutorialActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 3;
                    break;

                case R.id.nav_profile:
                    intent = new Intent(this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 4;
                    break;

                case R.id.nav_settings:
                    intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 5;
                    break;

                case R.id.nav_logout:
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(KEY_LOGOUT, 1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 6;
                    break;
            }
        } catch (Exception e) {

        }

        // Highlight the selected item and close the drawer
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
