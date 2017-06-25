package newspoints.sfsu.com.newsp_data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * NPAudio specifies the recorded sound captured by the user in a specific project.
 * <p>
 * Created by Pavitra on 2/16/2016.
 */
public class NPAudio implements Parcelable {
    public static final Creator<NPAudio> CREATOR = new Creator<NPAudio>() {
        @Override
        public NPAudio createFromParcel(Parcel in) {
            return new NPAudio(in);
        }

        @Override
        public NPAudio[] newArray(int size) {
            return new NPAudio[size];
        }
    };

    private long id;
    private String name;
    private String audioSourceUrl;
    private int startTime;
    private int endTime;
    private long timestamp;
    private LatLng mLatLng;
    private ArrayList<Question> questionList;
    private ArrayList<Source> sourceList;
    private ArrayList<Shot> shotList;

    // overloading for Demo purpose
    public NPAudio(String name, String audioSourceUrl, int startTime, int endTime, long timestamp,
                   LatLng mLatLng, ArrayList<Question> questionList, ArrayList<Source> sourceList,
                   ArrayList<Shot> shotList) {
        this.name = name;
        this.audioSourceUrl = audioSourceUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timestamp = timestamp;
        this.mLatLng = mLatLng;
        this.questionList = questionList;
        this.sourceList = sourceList;
        this.shotList = shotList;
    }

    // main constructor
    public NPAudio(long id, String name, String audioSourceUrl, int startTime, int endTime,
                   long timestamp, LatLng mLatLng, ArrayList<Question> questionList,
                   ArrayList<Source> sourceList, ArrayList<Shot> shotList) {
        this.id = id;
        this.name = name;
        this.audioSourceUrl = audioSourceUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timestamp = timestamp;
        this.mLatLng = mLatLng;
        this.questionList = questionList;
        this.sourceList = sourceList;
        this.shotList = shotList;
    }

    protected NPAudio(Parcel in) {
        id = in.readLong();
        name = in.readString();
        audioSourceUrl = in.readString();
        startTime = in.readInt();
        endTime = in.readInt();
        timestamp = in.readLong();
        in.readTypedList(questionList, Question.CREATOR);
        in.readTypedList(sourceList, Source.CREATOR);
        in.readTypedList(shotList, Shot.CREATOR);

    }

    public NPAudio(long id, String name, String audioSourceUrl, int startTime, int endTime, long timestamp) {
        this.id = id;
        this.name = name;
        this.audioSourceUrl = audioSourceUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timestamp = timestamp;
    }

    public LatLng getmLatLng() {
        return mLatLng;
    }

    public void setmLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    public ArrayList<Source> getSourceList() {
        return sourceList;
    }

    public void setSourceList(ArrayList<Source> sourceList) {
        this.sourceList = sourceList;
    }

    public ArrayList<Shot> getShotList() {
        return shotList;
    }

    public void setShotList(ArrayList<Shot> shotList) {
        this.shotList = shotList;
    }

    public String getAudioSourceUrl() {
        return audioSourceUrl;
    }

    public void setAudioSourceUrl(String audioSourceUrl) {
        this.audioSourceUrl = audioSourceUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return id + " : " + name + " : " + audioSourceUrl + " : " + startTime + " : " + endTime + " : " + timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(audioSourceUrl);
        dest.writeInt(startTime);
        dest.writeInt(endTime);
        dest.writeLong(timestamp);
        dest.writeTypedList(questionList);
        dest.writeTypedList(shotList);
        dest.writeTypedList(sourceList);
    }
}
