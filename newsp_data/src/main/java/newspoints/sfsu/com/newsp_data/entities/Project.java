package newspoints.sfsu.com.newsp_data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.greendao.DaoException;

import java.util.ArrayList;

import newspoints.sfsu.com.newsp_data.dao.DaoSession;
import newspoints.sfsu.com.newsp_data.dao.ProjectDao;

/**
 * <p>
 * Represents a Project entity that contains meta data, {@link NPEvent} and media content such as image, videos and/or audio.
 * </p>
 * A Project is a parent entity to hold multiple {@link NPEvent}s.
 * <p>
 * Created by Pavitra on 2/18/2016.
 * </p>
 */
public class Project implements Parcelable {

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    private long id;
    private String name;
    private String category;
    private String image_url;
    private String project_dir_url;
    private String sub_dir_url;
    private String description;
    // should be either of 0x47, 0x0x44 or 0xFFFF
    private long cloudStorage;
    private LatLng mLatLng;
    private long mTimestamp;
    private ArrayList<NPAudio> mAudioList;
    private ArrayList<NPVideo> mVideoList;


    /**
     * Used to resolve relations
     */
    private transient DaoSession mDaoSession;

    /**
     * Used for active entity operations.
     */
    private transient ProjectDao mProjectDao;

    public Project(long id) {
        this.id = id;
    }

    // DEMO purpose
    public Project(String name, String category, String image_url, String project_dir_url,
                   String sub_dir_url, String description, int cloudStorage,
                   double longitude, double latitude, long timestamp) {
        this.name = name;
        this.category = category;
        this.image_url = image_url;
        this.description = description;
        this.cloudStorage = cloudStorage;
        this.mLatLng = new LatLng(latitude, longitude);
        this.mTimestamp = timestamp;
    }

    /**
     * Constructor overloading when the Project is crated for the first time and contains only basic details
     */
    public Project(Long id, String name, String category, String image_url, String project_dir_url, String sub_dir_url,
                   String description, long cloudStorage, long timestamp) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.image_url = image_url;
        this.project_dir_url = project_dir_url;
        this.sub_dir_url = sub_dir_url;
        this.description = description;
        this.cloudStorage = cloudStorage;
        this.mTimestamp = timestamp;
    }


    protected Project(Parcel in) {
        id = in.readLong();
        name = in.readString();
        category = in.readString();
        image_url = in.readString();
        project_dir_url = in.readString();
        sub_dir_url = in.readString();
        description = in.readString();
        cloudStorage = in.readInt();
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mTimestamp = in.readLong();
        mAudioList = in.createTypedArrayList(NPAudio.CREATOR);
        mVideoList = in.createTypedArrayList(NPVideo.CREATOR);
    }

    /**
     * create a temp instance
     * @return
     */
    public static Project createInstance() {

//        return new Project("Leonardo wins OSCAR", "News", "drawable://" + R.drawable.placeholder_android_n, "", "", "First" +
//                " time in the history, Leo wins an Oscar. People are going crazy", ProjectConstants.CLOUD_GOOGLE_DRIVE,
//                2.5, 1.6, AppUtil.getCurrentTimeStamp());
        return null;
    }

    /**
     * called by internal mechanisms
     */
    public void _setDaoSession(DaoSession daoSession) {
        this.mDaoSession = daoSession;
        mProjectDao = mDaoSession != null ? mDaoSession.getProjectDao() : null;
    }

    public String getProject_dir_url() {
        return project_dir_url;
    }

    public void setProject_dir_url(String project_dir_url) {
        this.project_dir_url = project_dir_url;
    }

    public String getSub_dir_url() {
        return sub_dir_url;
    }

    public void setSub_dir_url(String sub_dir_url) {
        this.sub_dir_url = sub_dir_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public long getmTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    @Override
    public String toString() {
        return id +
                " : " + name +
                " : " + category +
                " : " + image_url +
                " : " + project_dir_url +
                " : " + sub_dir_url +
                " : " + description +
                " : " + cloudStorage +
                " : " + mLatLng.latitude +
                " : " + mLatLng.longitude +
                " : " + mTimestamp;
    }


    public ArrayList getmAudioList() {
        return mAudioList;
    }

    public void setmAudioList(ArrayList<NPAudio> mAudioList) {
        this.mAudioList = mAudioList;
    }

    public ArrayList<NPVideo> getmVideoList() {
        return mVideoList;
    }

    public void setmVideoList(ArrayList<NPVideo> mVideoList) {
        this.mVideoList = mVideoList;
    }

    public long getCloudStorage() {
        return cloudStorage;
    }

    public void setCloudStorage(long cloudStorage) {
        this.cloudStorage = cloudStorage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(image_url);
        dest.writeString(project_dir_url);
        dest.writeString(sub_dir_url);
        dest.writeString(description);
        dest.writeLong(cloudStorage);
        dest.writeParcelable(mLatLng, flags);
        dest.writeLong(mTimestamp);
        dest.writeTypedList(mAudioList);
        dest.writeTypedList(mVideoList);
    }

    /**
     * Delete the DAO
     */
    public void delete() {
        if (mProjectDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mProjectDao.delete(this);
    }

    /**
     * Updates the DAO
     */
    public void update() {
        if (mProjectDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mProjectDao.update(this);
    }

    /**
     * Reloads the value for this DAO from DB
     */
    public void refresh() {
        if (mProjectDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mProjectDao.refresh(this);
    }
}
