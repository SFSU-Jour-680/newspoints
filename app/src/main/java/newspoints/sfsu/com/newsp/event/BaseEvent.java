package newspoints.sfsu.com.newsp.event;

import java.util.ArrayList;

/**
 * Created by Pavitra on 4/12/2016.
 */
public class BaseEvent {

    public static final String UNHANDLED_MSG = "Unexpected Error";
    public static final int UNHANDLED_CODE = -1;

    /**
     * Event which binds the user selection and publishes to the EventBus.
     *
     * @param <Rs>
     */
    protected static class OnDone<Rs> {

        private ArrayList<Rs> mSelectedList;

        public OnDone(ArrayList<Rs> responseList) {
            mSelectedList = responseList;
        }

        public ArrayList<Rs> getSelectedList() {
            return mSelectedList;
        }

        public void setSelectedList(ArrayList<Rs> mSelectedList) {
            this.mSelectedList = mSelectedList;
        }
    }

    /**
     * Displays Failed Event
     */
    protected static class OnFailed {

        private String mErrorMessage;
        private int mCode;

        public OnFailed(String errorMessage, int code) {
            mErrorMessage = errorMessage;
            mCode = code;
        }

        public String getErrorMessage() {
            return mErrorMessage;
        }

        public int getCode() {
            return mCode;
        }

    }
}
