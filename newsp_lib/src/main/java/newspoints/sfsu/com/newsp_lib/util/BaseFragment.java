package newspoints.sfsu.com.newsp_lib.util;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    public String[] projectId, recordingId, endTime, eventId, type, Id, imageUri, createdDate;

    public void setListViewAdapterArgs(String[] projectID,
                                       String[] recordingID, String[] endTime, String[] eventID,
                                       String[] type) {
    }
}
