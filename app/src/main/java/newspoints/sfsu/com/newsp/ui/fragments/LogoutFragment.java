package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import newspoints.sfsu.com.newsp.session.SessionManager;


/**
 * Logs the user out of the application. Clears all the SharedPreferences and updates the
 * <tt>login</tt> flag in the {@link SessionManager}.
 * <p>
 * Created by Pavitra on 03/14/2016.
 */
public class LogoutFragment extends Fragment {

    private Context mContext;
    private SessionManager mSessionManager;
    private ILogoutCallBack mInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mContext = context;
            mInterface = (ILogoutCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement ILogoutCallBack interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // clear the AuthPreferences SharedPreferences
//        mSessionManager = new SessionManager(NewspointsApp.getInstance());
//
//        if (true) {
//            mSessionManager.setLogin(false);
//        }

        mInterface.userLoggedOut();

    }

    /**
     * Callback interface to logout the user and display login screen.
     */
    public interface ILogoutCallBack {
        /**
         * Callback method to logout the Account and display {@link LoginFragment} Fragment.
         */
        public void userLoggedOut();
    }

}
