package newspoints.sfsu.com.newsp_data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.google.common.base.Preconditions;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.SqlUtils;

import java.util.ArrayList;
import java.util.List;

import newspoints.sfsu.com.newsp_data.entities.Audio;

/**
 * Dao for {@link Audio} entity. Contains the required methods to carry out all the operations related to Audio
 * <p>
 * Created by Pavitra on 2/18/2016.
 */
public class AudioDao extends AbstractDao<Audio, Long> {

    public static final String TABLENAME = "NEWSP_AUDIO";
    private DaoSession daoSession;
    private String selectDeep;

    public AudioDao(DaoConfig config) {
        super(config);
    }


    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'" + TABLENAME + "' (" + //
                "\"" + Properties.Id.columnName + "\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"" + Properties.Name.columnName + "\" TEXT NOT NULL UNIQUE ," + // 1: name
                "\"" + Properties.StartTime.columnName + "\" INTEGER NOT NULL ," + // 2: startTime
                "\"" + Properties.EndTime.columnName + "\" INTEGER NOT NULL ," + // 3: end time
                "\"" + Properties.AudioUrl.columnName + "\" TEXT NOT NULL ," + // 4: video url
                "\"" + Properties.Timestamp.columnName + "\" LONG NOT NULL );"); // 5 : timestamp

    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'" + TABLENAME + "'";
        db.execSQL(sql);
    }

    @Override
    protected Audio readEntity(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset) ? null : cursor.getLong(offset);
    }

    @Override
    protected void readEntity(Cursor cursor, Audio entity, int offset) {
        entity.setId(cursor.isNull(offset) ? null : cursor.getLong(offset));
        entity.setName(cursor.getString(offset + 1));
        entity.setStartTime(cursor.getInt(offset + 2));
        entity.setEndTime(cursor.getInt(offset + 3));
        entity.setAudioSourceUrl(cursor.getString(offset + 4));
        entity.setTimestamp(cursor.getLong(offset + 5));
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, Audio entity) {

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Audio entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        id = Preconditions.checkNotNull(id);

        stmt.bindLong(1, id);
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getStartTime());
        stmt.bindLong(4, entity.getEndTime());
        stmt.bindString(5, entity.getAudioSourceUrl());
        stmt.bindLong(6, entity.getTimestamp());
    }

    @Override
    protected Long updateKeyAfterInsert(Audio entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    protected Long getKey(Audio entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean hasKey(Audio entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }


    protected Audio loadCurrentDeep(Cursor cursor, boolean lock) {
        Audio mAudio = loadCurrent(cursor, 0, lock);
        // offset by which the cursor builds Object
        int offset = getAllColumns().length;
        return mAudio;
    }

    /**
     * Builds MyVideo from the key using cursor
     *
     * @param key
     * @return
     */
    public Audio loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[]{key.toString()};

        try (Cursor cursor = db.rawQuery(sql, keyArray)) {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        }
    }

    /**
     * Reads all available rows from the given cursor and returns a list of MyVideo objects
     */
    private List<Audio> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Audio> list = new ArrayList<Audio>(count);

        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }

    /**
     * Returns list of MyVideos build using cursor.
     *
     * @param cursor
     * @return
     */
    private List<Audio> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    /**
     * A raw-style query where you can pass any WHERE clause and arguments to query MyVideo
     *
     * @param where
     * @param selectionArg
     * @return List of MyVideo
     */
    public List<Audio> getAll(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }


    /**
     * Returns Query string for Selecting ALL from DB
     *
     * @return
     */
    private String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(" FROM ");
            builder.append(TABLENAME);
            builder.append(" T ");
            selectDeep = builder.toString();
        }
        return selectDeep;
    }


    /**
     * Defines all the properties of the Audio Model
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property StartTime = new Property(2, Integer.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(3, Integer.class, "endTime", false, "END_TIME");
        public final static Property AudioUrl = new Property(4, String.class, "audioUrl", false, "AUDIO_URL");
        public final static Property Timestamp = new Property(5, Long.class, "timestamp", false, "TIMESTAMP");
    }
}
