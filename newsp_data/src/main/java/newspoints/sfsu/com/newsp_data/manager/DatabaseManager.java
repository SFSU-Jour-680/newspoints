package newspoints.sfsu.com.newsp_data.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import newspoints.sfsu.com.newsp_data.dao.DaoMaster;
import newspoints.sfsu.com.newsp_data.dao.DaoSession;
import newspoints.sfsu.com.newsp_data.dao.UserDao;
import newspoints.sfsu.com.newsp_data.entities.NPAudio;
import newspoints.sfsu.com.newsp_data.entities.MyEvent;
import newspoints.sfsu.com.newsp_data.entities.NPVideo;
import newspoints.sfsu.com.newsp_data.entities.Project;
import newspoints.sfsu.com.newsp_data.entities.User;

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
    private DaoMaster.OpenHelper mHelper;
    private Database database;
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
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
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
        if (database != null) {
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
            asyncSession.deleteAll(NPAudio.class);
            asyncSession.deleteAll(NPVideo.class);
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
    public NPVideo insertVideo(NPVideo NPVideo) {
        return null;
    }

    @Override
    public void updateVideo(NPVideo NPVideo) {
    }

    @Override
    public boolean deleteVideoById(Long videoId) {
        return false;
    }

    @Override
    public NPVideo getVideoById(Long videoId) {
        return null;
    }

    @Override
    public List<NPVideo> listVideos() {
        return null;
    }

    @Override
    public NPAudio insertAudio(NPAudio audio) {
        return null;
    }

    @Override
    public void updateAudio(NPAudio audio) {
    }

    @Override
    public boolean deleteAudioById(Long audioId) {
        return false;
    }

    @Override
    public NPVideo getAudioById(Long audioId) {
        return null;
    }

    @Override
    public List<NPAudio> listAudios() {
        return null;
    }

    @Override
    public NPAudio insertProject(Project project) {
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
    public NPVideo getProjectById(Long projectId) {
        return null;
    }

    @Override
    public List<Project> listProjects() {
        return null;
    }

    @Override
    public NPAudio insertEvent(MyEvent event) {
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
    public NPVideo getEventById(Long eventId) {
        return null;
    }

    @Override
    public List<MyEvent> listEvents() {
        return null;
    }


}
