package newspoints.sfsu.com.newsp_data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A real video captured file that contains all the meta data
 * <p/>
 * Created by Pavitra on 2/16/2016.
 */
public class MyVideo implements Parcelable {

    public static final Creator<MyVideo> CREATOR = new Creator<MyVideo>() {
        @Override
        public MyVideo createFromParcel(Parcel in) {
            return new MyVideo(in);
        }

        @Override
        public MyVideo[] newArray(int size) {
            return new MyVideo[size];
        }
    };
    private long id;
    private String name;
    private String videoUrl;
    private int startTime;
    private int endTime;
    private long timestamp;
    private LatLng mLatLng;
    // each Video has a list of Questions, Source and Shots
    private ArrayList<Question> questionList;
    private ArrayList<Source> sourceList;
    private ArrayList<Shot> shotList;

    // overloading for Demo purpose
    public MyVideo(String name, String videoUrl, int startTime, int endTime, long timestamp, LatLng mLatLng, ArrayList<Question> questionList, ArrayList<Source> sourceList, ArrayList<Shot> shotList) {
        this.name = name;
        this.videoUrl = videoUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timestamp = timestamp;
        this.mLatLng = mLatLng;
        this.questionList = questionList;
        this.sourceList = sourceList;
        this.shotList = shotList;
    }

    // main constructor
    public MyVideo(long id, String name, String videoUrl, int startTime, int endTime, long timestamp, LatLng mLatLng, ArrayList<Question> questionList, ArrayList<Source> sourceList, ArrayList<Shot> shotList) {
        this.id = id;
        this.name = name;
        this.videoUrl = videoUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timestamp = timestamp;
        this.mLatLng = mLatLng;
        this.questionList = questionList;
        this.sourceList = sourceList;
        this.shotList = shotList;
    }

    protected MyVideo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        videoUrl = in.readString();
        startTime = in.readInt();
        endTime = in.readInt();
        timestamp = in.readLong();
        in.readTypedList(questionList, Question.CREATOR);
        in.readTypedList(sourceList, Source.CREATOR);
        in.readTypedList(shotList, Shot.CREATOR);
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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
        return id + " : " + name + " : " + videoUrl + " : " + startTime + " : " + endTime + " : " + timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(videoUrl);
        dest.writeInt(startTime);
        dest.writeInt(endTime);
        dest.writeLong(timestamp);
        dest.writeTypedList(questionList);
        dest.writeTypedList(shotList);
        dest.writeTypedList(sourceList);
    }
}
