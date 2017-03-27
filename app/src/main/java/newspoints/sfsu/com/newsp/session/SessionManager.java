package newspoints.sfsu.com.newsp.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import newspoints.sfsu.com.newsp.R;


/**
 * <p>
 * Manages the {@link newspoints.sfsu.com.newsp_data.entities.User} session in the application. When the use logs in the application, the LoginFragment
 * <p>The SessionManager will store the boolean value depending on whether the Account is logged in or not. When the Account
 * opens the application, the HomeActivity will check for the boolean value in SessionManager and will redirect the user to
 * the {@link newspoints.sfsu.com.newsp.ui.fragments.DashboardFragment}
 * or {@link newspoints.sfsu.com.newsp.ui.fragments.LoginFragment} screen respectively
 * </p>
 * <p>
 * Created by Pavitra on 03/11/2016.
 */
public class SessionManager {
    // Shared preferences file name
    private static final String KEY_PREF_NAME = "InvesTICKationsSession";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_LOGGEDOUT = "isLoggedOut";
    // LogCat tag
    private static final String TAG = "~!@#$SessionMgr";
    // Shared Preferences
    private SharedPreferences prefSession;
    private SharedPreferences.Editor editor;
    private Context mContext;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.mContext = context;
        prefSession = mContext.getSharedPreferences(mContext.getResources().getString(R.string.session_pref_file_name), Context.MODE_PRIVATE);
        editor = prefSession.edit();
    }

    /**
     * Sets a boolean value to the preference and commits.
     *
     * @param isLoggedIn
     */
    public void setLogin(boolean isLoggedIn) {
        // open the Session preference file.
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.apply();
        Log.d(TAG, "Account LoginFragment session modified!");
    }

    /**
     * Returns whether the user is logged in or not.
     *
     * @return
     */
    public boolean isLoggedIn() {
        return prefSession.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
