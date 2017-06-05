package newspoints.sfsu.com.newsp_data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.google.common.base.Preconditions;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import newspoints.sfsu.com.newsp_data.entities.Shot;

/**
 * DAO to handle all the DB operations related to {@link Shot}
 * <p>
 * Created by Pavitra on 6/16/2016.
 */
public class ShotDao extends AbstractDao<Shot, Integer> {

    public static final String TABLENAME = "NEWSP_SHOT";
    private DaoSession daoSession;
    private String selectDeep;

    public ShotDao(DaoConfig config) {
        super(config);
    }

    public ShotDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"" + TABLENAME + "\" (" +
                "\"" + Properties.Id.columnName + "\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"" + Properties.Label.columnName + "\" TEXT NOT NULL," + // 1: Label
                "\"" + Properties._Context.columnName + "\" TEXT NOT NULL ," + // 2: context
                "\"" + Properties.Value.columnName + "\" TEXT NOT NULL ," + // 3: value
                "\"" + Properties.XmlFileName.columnName + "\" TEXT NOT NULL );"); // 4 : xmlFileName
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'" + TABLENAME + "'";
        db.execSQL(sql);
    }

    @Override
    protected Shot readEntity(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected Integer readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset) ? null : cursor.getInt(offset);
    }

    @Override
    protected void readEntity(Cursor cursor, Shot entity, int offset) {
        entity.setId(cursor.isNull(offset) ? null : cursor.getLong(offset));
        entity.setLabel(cursor.getString(offset + 1));
        entity.setContext(cursor.getString(offset + 2));
        entity.setValue(cursor.getString(offset + 3));
        entity.setXmlFileName(cursor.getString(offset + 4));
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, Shot entity) {

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Shot entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        id = Preconditions.checkNotNull(id);

        stmt.bindLong(1, id);
        stmt.bindString(2, entity.getLabel());
        stmt.bindString(3, entity.getContext());
        stmt.bindString(4, entity.getValue());
        stmt.bindString(5, entity.getXmlFileName());
    }

    @Override
    protected Integer updateKeyAfterInsert(Shot entity, long rowId) {
        return null;
    }

    @Override
    protected Integer getKey(Shot entity) {
        return null;
    }

    @Override
    protected boolean hasKey(Shot entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }

    /**
     * Properties of a Shot table
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Label = new Property(1, String.class, "label", true, "LABEL");
        public final static Property _Context = new Property(2, String.class, "_context", true, "_CONTEXT");
        public final static Property Value = new Property(3, String.class, "value", true, "VALUE");
        public final static Property XmlFileName = new Property(4, String.class, "xmlFileName", true, "XMLFILENAME");

    }
}
