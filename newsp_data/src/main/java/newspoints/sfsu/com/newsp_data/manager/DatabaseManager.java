package newspoints.sfsu.com.newsp_data.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.newspoints.journalist.database.DaoMaster;
import com.newspoints.journalist.database.DaoSession;
import com.newspoints.journalist.database.UserDao;
import com.newspoints.journalist.entities.Audio;
import com.newspoints.journalist.entities.MyEvent;
import com.newspoints.journalist.entities.MyVideo;
import com.newspoints.journalist.entities.Project;
import com.newspoints.journalist.entities.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Business layer for handling all the Database operations that contains all the operations, methods andrelated business logic
 * for the
 */
public class DatabaseManager implements IDatabaseManager, AsyncOperationListener {

    /**
     * Class tag. Used for debug.
     */
    private static final String TAG = "~!@#$" + DatabaseManager.class.getCanonicalName();
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager mInstance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context context;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link Context}.
     */
    public DatabaseManager(final Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(this.context, "sample-database", null);
        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
    }

    /**
     * @param context The Android {@link Context}.
     * @return this.mInstance
     */
    public static DatabaseManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseManager(context);
        }
        return mInstance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (mInstance != null) {
            mInstance = null;
        }
    }

    @Override
    public void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true);  // drops all tables
            mHelper.onCreate(database);
            asyncSession.deleteAll(User.class);
            asyncSession.deleteAll(Audio.class);
            asyncSession.deleteAll(MyVideo.class);
            asyncSession.deleteAll(Project.class);
            asyncSession.deleteAll(MyEvent.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized User insertUser(User user) {
        try {
            if (user != null) {
                openWritableDb();
                UserDao userDao = daoSession.getDBUserDao();
                userDao.insert(user);
                Log.d(TAG, "Inserted user: " + user.getEmail() + " to the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        try {
            if (user != null) {
                openWritableDb();
                daoSession.update(user);
                Log.d(TAG, "Updated user: " + user.getEmail() + " from the schema.");
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserByEmail(String email) {
        try {
            openWritableDb();
            UserDao userDao = daoSession.getDBUserDao();
            QueryBuilder<User> queryBuilder = userDao.queryBuilder().where(UserDao.Properties.Email.eq(email));
            List<User> userToDelete = queryBuilder.list();
            for (User user : userToDelete) {
                userDao.delete(user);
            }
            daoSession.clear();
            Log.d(TAG, userToDelete.size() + " entry. " + "Deleted user: " + email + " from the schema.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteUserById(Long userId) {
        try {
            openWritableDb();
            UserDao userDao = daoSession.getDBUserDao();
            userDao.deleteByKey(userId);
            daoSession.clear();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(Long userId) {
        User user = null;
        try {
            openReadableDb();
            UserDao userDao = daoSession.getDBUserDao();
            user = userDao.loadDeep(userId);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public MyVideo insertVideo(MyVideo myVideo) {
        return null;
    }

    @Override
    public void updateVideo(MyVideo myVideo) {

    }

    @Override
    public boolean deleteVideoById(Long videoId) {
        return false;
    }

    @Override
    public MyVideo getVideoById(Long videoId) {
        return null;
    }

    @Override
    public List<MyVideo> listVideos() {
        return null;
    }

    @Override
    public Audio insertAudio(Audio audio) {
        return null;
    }

    @Override
    public void updateAudio(Audio audio) {

    }

    @Override
    public boolean deleteAudioById(Long audioId) {
        return false;
    }

    @Override
    public MyVideo getAudioById(Long audioId) {
        return null;
    }

    @Override
    public List<Audio> listAudios() {
        return null;
    }

    @Override
    public Audio insertProject(Project project) {
        return null;
    }

    @Override
    public void updateProject(Project project) {

    }

    @Override
    public boolean deleteProjectById(Project projectId) {
        return false;
    }

    @Override
    public MyVideo getProjectById(Long projectId) {
        return null;
    }

    @Override
    public List<Project> listProjects() {
        return null;
    }

    @Override
    public Audio insertEvent(MyEvent event) {
        return null;
    }

    @Override
    public void updateEvent(MyEvent event) {

    }

    @Override
    public boolean deleteEventById(MyEvent eventId) {
        return false;
    }

    @Override
    public MyVideo getEventById(Long eventId) {
        return null;
    }

    @Override
    public List<MyEvent> listEvents() {
        return null;
    }

}
