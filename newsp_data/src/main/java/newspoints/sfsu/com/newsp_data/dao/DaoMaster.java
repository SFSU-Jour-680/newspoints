package newspoints.sfsu.com.newsp_data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


/**
 * Master of DAO (schema version 1): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(UserDao.class);
        registerDaoClass(VideoDao.class);
        registerDaoClass(AudioDao.class);
        registerDaoClass(ProjectDao.class);
        registerDaoClass(EventDao.class);
    }


    /**
     * Creates underlying database table using DAOs.
     */
    public static void createAllTables(Database db, boolean ifNotExists) {
        UserDao.createTable(db, ifNotExists);
        VideoDao.createTable(db, ifNotExists);
        AudioDao.createTable(db, ifNotExists);
        ProjectDao.createTable(db, ifNotExists);
        EventDao.createTable(db, ifNotExists);
    }

    /**
     * Drops underlying database table using DAOs.
     */
    public static void dropAllTables(Database db, boolean ifExists) {
        UserDao.dropTable(db, ifExists);
        VideoDao.dropTable(db, ifExists);
        AudioDao.dropTable(db, ifExists);
        ProjectDao.dropTable(db, ifExists);
        EventDao.dropTable(db, ifExists);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    public static abstract class OpenHelper extends DatabaseOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("Newspoints", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     */
    public static class DevOpenHelper extends OpenHelper {

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("Newspoints", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
