package newspoints.sfsu.com.newsp_data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import newspoints.sfsu.com.newsp_data.entities.MyEvent;

/**
 *
 * Created by Pavitra on 2/19/2016.
 */
public class EventDao extends AbstractDao<MyEvent, Long> {

    public static final String TABLENAME = "EVENT";
    private DaoSession mDaoSession;
    private String selectDeep; // for deep querying

    public EventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.mDaoSession = daoSession;
    }

    public static void createTable(Database db, boolean ifNotExists) {

    }

    public static void dropTable(Database db, boolean ifExists) {
    }

    @Override
    protected MyEvent readEntity(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected void readEntity(Cursor cursor, MyEvent entity, int offset) {
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, MyEvent entity) {

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, MyEvent entity) {
    }

    @Override
    protected Long updateKeyAfterInsert(MyEvent entity, long rowId) {
        return null;
    }

    @Override
    protected Long getKey(MyEvent entity) {
        return null;
    }

    @Override
    protected boolean hasKey(MyEvent entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }

    /**
     * Table properties.
     */
    public static class Properties{
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");

    }
}
