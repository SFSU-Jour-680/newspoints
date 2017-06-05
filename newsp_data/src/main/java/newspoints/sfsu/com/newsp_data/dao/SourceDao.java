package newspoints.sfsu.com.newsp_data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import newspoints.sfsu.com.newsp_data.entities.Source;

/**
 * DAO to handle all the DB operations related to {@link Source}
 * <p>
 * Created by Pavitra on 6/16/2016.
 */
public class SourceDao extends AbstractDao<Source, Integer> {
    public static final String TABLENAME = "NEWSP_SOURCE";
    private DaoSession daoSession;
    private String selectDeep;

    public SourceDao(DaoConfig config) {
        super(config);
    }

    public SourceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    @Override
    protected Source readEntity(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected Integer readKey(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected void readEntity(Cursor cursor, Source entity, int offset) {

    }

    @Override
    protected void bindValues(DatabaseStatement stmt, Source entity) {

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Source entity) {

    }

    @Override
    protected Integer updateKeyAfterInsert(Source entity, long rowId) {
        return null;
    }

    @Override
    protected Integer getKey(Source entity) {
        return null;
    }

    @Override
    protected boolean hasKey(Source entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }
}
