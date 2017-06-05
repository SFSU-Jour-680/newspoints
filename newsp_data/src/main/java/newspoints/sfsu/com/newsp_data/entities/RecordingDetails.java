package newspoints.sfsu.com.newsp_data.entities;

/**
 * Used to store the recording details of Video or NPAudio
 * <p>
 * Created by Pavitra on 7/26/2015.
 */
public class RecordingDetails {
    String recordingId, endTime;

    public RecordingDetails(String recordingId, String endTime) {
        this.recordingId = recordingId;
        this.endTime = endTime;
    }

    public String getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(String recordingId) {
        this.recordingId = recordingId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
