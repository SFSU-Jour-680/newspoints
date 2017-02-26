package newspoints.sfsu.com.newsp_data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.google.common.base.Preconditions;
import com.newspoints.journalist.entities.Project;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;

/**
 * Defines all the {@link Project} based DAO operations such as CREATE, GET, INSERT and UPDATE
 * Created by Pavitra on 2/18/2016.
 */
public class ProjectDao extends AbstractDao<Project, Long> {

    public static final String TABLENAME = "NEWSP_PROJECT";
    private DaoSession daoSession;
    private String selectDeep;

    public ProjectDao(DaoConfig config, DaoSession daoSession) {
        super(config);
        this.daoSession = daoSession;
    }

    public ProjectDao(DaoConfig config) {
        super(config);
    }

    /**
     * Creates <tt>Project</tt> table in DB
     *
     * @param db
     * @param ifNotExists
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"" + TABLENAME + "\" (" +
                "\"" + Properties.Id.columnName + "\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"" + Properties.Name.columnName + "\" TEXT NOT NULL UNIQUE ," + // 1: name
                "\"" + Properties.Category.columnName + "\" TEXT NOT NULL ," + // 2: category
                "\"" + Properties.ImageUrl.columnName + "\" TEXT NOT NULL ," + // 3: image url
                "\"" + Properties.ProjectDirUrl.columnName + "\" TEXT NOT NULL ," + // 4: proj dir url
                "\"" + Properties.SubDirUrl.columnName + "\" TEXT NOT NULL ," + // 5: sub dir url
                "\"" + Properties.Description.columnName + "\" TEXT NOT NULL ," + // 6: desc
                "\"" + Properties.CloudStorage.columnName + "\" LONG NULL ," + // 7: cloud storage
                "\"" + Properties.TimeStamp.columnName + "\" LONG NOT NULL );"); // 8 : timestamp
    }

    /**
     * Drop the table
     *
     * @param db
     * @param ifExists
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"" + TABLENAME + "\"";
        db.execSQL(sql);
    }

    @Override
    protected Project readEntity(Cursor cursor, int offset) {
        return new Project(
                cursor.isNull(offset) ? null : cursor.getLong(offset), // id
                cursor.getString(offset + 1),
                cursor.getString(offset + 2),
                cursor.getString(offset + 3),
                cursor.getString(offset + 4),
                cursor.getString(offset + 5),
                cursor.getString(offset + 6),
                cursor.getLong(offset + 7),
                cursor.getLong(offset + 8)
        );
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset) ? null : cursor.getLong(offset);
    }

    @Override
    protected void readEntity(Cursor cursor, Project entity, int offset) {
        entity.setId(cursor.isNull(offset) ? null : cursor.getLong(offset));
        entity.setName(cursor.getString(offset + 1));
        entity.setCategory(cursor.getString(offset + 2));
        entity.setImage_url(cursor.getString(offset + 3));
        entity.setProject_dir_url(cursor.getString(offset + 4));
        entity.setSub_dir_url(cursor.getString(offset + 5));
        entity.setDescription(cursor.getString(offset + 6));
        entity.setCloudStorage(cursor.getLong(offset + 7));
        entity.setTimestamp(cursor.getLong(offset + 8));
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Project entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        // check for NPE
        id = Preconditions.checkNotNull(id);

        stmt.bindLong(1, id);
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getCategory());
        stmt.bindString(4, entity.getImage_url());
        stmt.bindString(5, entity.getProject_dir_url());
        stmt.bindString(6, entity.getSub_dir_url());
        stmt.bindString(7, entity.getDescription());
        stmt.bindLong(8, entity.getCloudStorage());
        stmt.bindLong(9, entity.getTimestamp());
    }

    @Override
    protected Long updateKeyAfterInsert(Project entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    protected Long getKey(Project entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    /**
     * TODO : define relationship
     * Builds Project object from the Cursor containing all the relationships too.
     *
     * @param cursor
     * @param lock
     * @return
     */
    protected Project loadCurrentDeep(Cursor cursor, boolean lock) {
        Project mProject = loadCurrent(cursor, 0, lock);
        // offset by which the cursor builds Object
        int offset = getAllColumns().length;


        return mProject;
    }

    /**
     * Returns the {@link Project} from the key
     *
     * @param key
     * @return
     */
    public Project loadDeep(Long key) {
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
     * Reads all available rows from the given cursor and returns a list of Project objects
     */
    public List<Project> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Project> list = new ArrayList<Project>(count);

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
     * Returnes list of Projects build using cursor.
     *
     * @param cursor
     * @return
     */
    protected List<Project> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    /**
     * A raw-style query where you can pass any WHERE clause and arguments to query Project
     *
     * @param where
     * @param selectionArg
     * @return List of Projects
     */
    public List<Project> getAll(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }


    /**
     * Returns Query string for selecting all from DB
     *
     * @return
     */
    public String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(" FROM PROJECT T ");
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    /**
     * Properties of entity Project.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Category = new Property(2, String.class, "Category", false, "CATEGORY");
        public final static Property ImageUrl = new Property(3, String.class, "image_url", false, "IMAGE_URL");
        public final static Property ProjectDirUrl = new Property(4, String.class, "dir_url", false, "DIR_URL");
        public final static Property SubDirUrl = new Property(5, String.class, "sub_dir_url", false, "SUB_DIR_URL");
        public final static Property Description = new Property(6, String.class, "description", false, "DESCRIPTION");
        public final static Property CloudStorage = new Property(7, long.class, "cloud_storage", false, "CLOUD_STORAGE");
        public final static Property TimeStamp = new Property(8, long.class, "timestamp", false, "TIMESTAMP");
    }
}
