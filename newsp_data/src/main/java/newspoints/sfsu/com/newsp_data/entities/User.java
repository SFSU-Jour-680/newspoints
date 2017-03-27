package newspoints.sfsu.com.newsp_data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import newspoints.sfsu.com.newsp_data.dao.DaoSession;
import newspoints.sfsu.com.newsp_data.dao.UserDao;


/**
 * User of the Newspoints app.
 * <p>
 * Created by Pavitra on 2/16/2016.
 */
public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private Long id;
    private String name;
    private String email;
    private String password;
    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    private transient UserDao myDao;

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
    }

    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDBUserDao() : null;
    }
}
