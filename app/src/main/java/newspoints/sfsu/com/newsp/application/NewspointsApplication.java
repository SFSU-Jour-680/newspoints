package newspoints.sfsu.com.newsp.application;

import android.app.Application;

public class NewspointsApplication extends Application {
    // single object of application
    private static NewspointsApplication mInstance;

    /**
     * Returns the singleton instance of the Application context.
     *
     * @return
     */
    public static synchronized NewspointsApplication getInstance() {
        return mInstance;
    }

    /**
     * Returns the height of the status bar depending on each device
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
