package newspoints.sfsu.com.newsp.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import newspoints.sfsu.com.newsp.R;

/**
 * Manages user profile and User details. Enables user to view and edit all the user details
 */
public class UserProfileActivity extends MainBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_edit:
                editUserDetails();
                break;
        }
        return true;
    }

    private void editUserDetails() {

    }
}
