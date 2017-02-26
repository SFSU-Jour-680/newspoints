package newspoints.sfsu.com.newsp_data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.newspoints.journalist.entities.Question;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * DAO to handle all the DB operations related to {@link Question}
 * <p>
 * Created by Pavitra on 6/16/2016.
 */
public class QuestionDao extends AbstractDao<Question, Integer> {

    public static final String TABLENAME = "NEWSP_QUESTION";
    private DaoSession daoSession;
    private String selectDeep;

    public QuestionDao(DaoConfig config) {
        super(config);
    }

    public QuestionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    @Override
    protected Question readEntity(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected Integer readKey(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected void readEntity(Cursor cursor, Question entity, int offset) {

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Question entity) {

    }

    @Override
    protected Integer updateKeyAfterInsert(Question entity, long rowId) {
        return null;
    }

    @Override
    protected Integer getKey(Question entity) {
        return null;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }
}
