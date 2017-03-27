package newspoints.sfsu.com.newsp_data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import newspoints.sfsu.com.newsp_lib.util.BaseFragment;
import newspoints.sfsu.com.newsp_lib.util.ExportRecordingDetails;
import newspoints.sfsu.com.newsp_data.util.ProjectConstants;

public class RecordingDetailsDB extends SQLiteOpenHelper {

    private static final String DBNAME = "AudioVideoRecodingDetails.db";

    private static final String TABLENAME1 = "ProjectDetails";
    private static final String TABLENAME2 = "RecodingDetails";
    private static final String TABLENAME3 = "EventIdDeatails";

    private static final String TYPE = "RecordingType";
    private static final String END = "EndTime";
    private static final String EVENTNAME = "EventName";
    private static final String RECORDING_ID = "RecordingId";
    private static final String PROJECT_ID = "ProjectId";
    private static final String PROJECT_NAME = "ProjectName";
    private static final String EVENT_ID = "EventID";
    private static final String CATEGORY = "Category";
    private static final String IMAGE_URI = "ImagePath";
    private static final String DATE_CREATED = "Date";
    private static final String RECORDING_TIME = "recordingTime";
    private static final String CREATED_DATE = "DateCreated";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private static final int DBVERSION = 3;

    private final String DBCREATETABLE1 = "create table " + TABLENAME1 + "( "
            + PROJECT_ID + " text ," + PROJECT_NAME + " text ," + CATEGORY
            + " Text ," + IMAGE_URI + " text," + DATE_CREATED + " text , " + LATITUDE + " real," + LONGITUDE + " real ) ";

    private final String DBCREATETABLE2 = "create table " + TABLENAME2 + "( " + PROJECT_ID + " text ," + RECORDING_ID + " text ," + END
            + " Text ," + EVENT_ID + " Text ," + TYPE + " text ," + IMAGE_URI + " text , " + RECORDING_TIME + " text ," + CREATED_DATE
            + " text) ";

    private final String DBCREATETABLE3 = "create table " + TABLENAME3 + "( "
            + EVENTNAME + " Text ," + EVENT_ID + " Text  ) ";

    long time = 0;
    int count = 0;
    String recordingID;


    public RecordingDetailsDB(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(DBCREATETABLE1);
            db.execSQL(DBCREATETABLE2);
            db.execSQL(DBCREATETABLE3);
        } catch (SQLException s) {
            Log.d("RecordingDetails DB", "Database Error");
        }
        Log.d("RecordingDetails DB", "DB Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public boolean projectDetails(int projectId, String projectName, String category, String imagePath, String date, double latitude, double longitude) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PROJECT_ID, projectId);

        cv.put(PROJECT_NAME, projectName);

        cv.put(CATEGORY, category);

        cv.put(IMAGE_URI, imagePath);

        cv.put(DATE_CREATED, date);

        cv.put(LATITUDE, latitude);

        cv.put(LONGITUDE, longitude);
        long result = sqlLiteDatabase.insert(TABLENAME1, null, cv);
        if (result != -1) {
            return true;
        }
        return false;
    }

    // retrieve ProjectList, ProjectImage and ProjectDate
    public void getProjectDetails() {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        Cursor c = sqlLiteDatabase.query(TABLENAME1, null, null, null, null,
                null, null, null);
        try {
            c.moveToFirst();
            // MOVE: this doenst belong here. Move to UI layer
            while (!c.isAfterLast() && c.getCount() != 0) {
//                IndexActivity.projectList.add(c.getString(1));
//                IndexActivity.projectImage.add(c.getString(3));
//                IndexActivity.projectDate.add(c.getString(4));
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());

        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }

    }

    public int deleteProject(String projectName) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = {projectName};
        int result = sqlLiteDatabase.delete(TABLENAME1, PROJECT_NAME + "=?",
                whereArgs);
        return result;
    }

    public int deleteProjectRecordings(String projectName) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = {projectName};

        int result = sqlLiteDatabase.delete(TABLENAME2, PROJECT_ID + "=?",
                whereArgs);

        return result;
    }

    public String getTotalRecordingDuration(String projectName) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();

        String[] args = {projectName};

        Cursor c = sqlLiteDatabase.query(TABLENAME2,
                new String[]{RECORDING_TIME}, PROJECT_ID + "=?", args, null,
                null, null, null);
        try {
            c.moveToFirst();

            long recordingTime;
            time = 0;
            if (c.getCount() < 1) {

            } else {
                do {
                    time = time + c.getLong(0);
                } while (c.moveToNext());

                recordingTime = time;
                int timeInSeconds = (int) recordingTime;
                int hours = timeInSeconds / 3600;
                timeInSeconds = timeInSeconds - (hours * 3600);
                int minutes = timeInSeconds / 60;
                timeInSeconds = timeInSeconds - (minutes * 60);
                int seconds = timeInSeconds;
                String timeDurationString;

                if (minutes < 60) {
                    if (seconds > 10) {
                        timeDurationString = "0" + minutes + ":" + seconds;
                    } else {
                        timeDurationString = "0" + minutes + ":" + "0" + seconds;
                    }
                } else {
                    timeDurationString = "0" + hours + ":" + minutes + ":"
                            + seconds;
                }
                return timeDurationString;
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return "";
    }

    public boolean recordingDetails(String projectId, String recordingId,
                                    String end, String event, String type, String ImagePath,
                                    long recordingTime, String recordingDate) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PROJECT_ID, projectId);

        cv.put(RECORDING_ID, recordingId);

        cv.put(END, end);
        cv.put(EVENT_ID, event);
        cv.put(TYPE, type);
        cv.put(IMAGE_URI, ImagePath);
        String recordingDuration = String.valueOf(recordingTime);
        cv.put(RECORDING_TIME, recordingDuration);
        cv.put(CREATED_DATE, "" + recordingDate);

        long result = sqlLiteDatabase.insert(TABLENAME2, null, cv);
        if (result != -1) {
            return true;
        }
        return false;
    }

    public boolean storeEventId(String name, String id) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EVENTNAME, name);
        cv.put(EVENT_ID, id);
        long result = sqlLiteDatabase.insert(TABLENAME3, null, cv);
        if (result != -1) {
            return true;
        }
        return false;
    }

    public String getEventId(String eventName) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] selectionArg = {eventName,};
        String eventId = "";
        Cursor c = sqlLiteDatabase.query(TABLENAME3, new String[]{EVENT_ID},
                EVENTNAME + "=?", selectionArg, null, null, null, null);

        try {
            String[] columnName = c.getColumnNames();

            if (c == null) {
                return null;

            } else {
                c.moveToFirst();
            }
            eventId = c.getString(0);
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return eventId;
    }

    public int getRecordingCount(String projectSelected, String type) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();

        // String type = "audio";
        String[] selectionArg = {projectSelected, type};
        int recCount = 0;
        Cursor c = sqlLiteDatabase.query(TABLENAME2,
                new String[]{RECORDING_ID}, PROJECT_ID + "=?" + " and "
                        + TYPE + "=?", selectionArg, null, null, null, null);
        try {
            recCount = c.getCount();

        } catch (Exception e) {
            Log.e("===DB", e.getMessage());

        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return recCount;

    }

    public String[] getFilesList(String projectName, BaseFragment fragment) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] selectionArg2 = {projectName};

        Cursor c2 = sqlLiteDatabase.query(TABLENAME2, new String[]{
                        PROJECT_ID, RECORDING_ID, END, EVENT_ID, TYPE, IMAGE_URI,
                        CREATED_DATE}, PROJECT_ID + "=?", selectionArg2, null, null,
                null, null);

        try {
            c2.moveToFirst();
            count = 0;
            int noOfRows = c2.getCount();
            fragment.projectId = new String[noOfRows];
            fragment.recordingId = new String[noOfRows];
            fragment.endTime = new String[noOfRows];
            fragment.eventId = new String[noOfRows];
            fragment.type = new String[noOfRows];
            fragment.imageUri = new String[noOfRows];
            fragment.createdDate = new String[noOfRows];

            if (c2.getCount() == 0) {
            } else {

                c2.moveToFirst();
                while (count != noOfRows) {
                    if (!c2.getString(3).contains("404")) {
                        fragment.projectId[count] = c2.getString(0);
                        fragment.recordingId[count] = c2.getString(1);
                        fragment.endTime[count] = c2.getString(2);
                        fragment.eventId[count] = c2.getString(3);
                        fragment.type[count] = c2.getString(4);
                        fragment.imageUri[count] = c2.getString(5);
                        fragment.createdDate[count] = c2.getString(6);

                    } else {
                        fragment.projectId[count] = c2.getString(0);
                        fragment.recordingId[count] = c2.getString(1);
                        fragment.endTime[count] = c2.getString(2);
                        fragment.eventId[count] = "Untagged";
                        fragment.type[count] = c2.getString(4);
                        fragment.imageUri[count] = c2.getString(5);
                        fragment.createdDate[count] = c2.getString(6);

                    }
                    count++;
                    c2.moveToNext();
                }

                fragment.setListViewAdapterArgs(fragment.projectId,
                        fragment.recordingId, fragment.endTime, fragment.eventId,
                        fragment.type);
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {

            if (!c2.isClosed()) {
                c2.close();
            }
        }
        return null;

    }

    public int getClipCountFromRecording(String recordingID) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();

        String[] selectionArg = {recordingID};
        int clipCount = 0;

        Cursor c = sqlLiteDatabase.query(TABLENAME2,
                new String[]{EVENT_ID}, RECORDING_ID + "=?", selectionArg, null, null, null, null);
        try {
            clipCount = c.getCount();
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return clipCount;
    }

    public int getVideoRecordingDuration(String recordingID) {
        String[] selectionArg = {ProjectConstants.selectedProjectName,
                "video", recordingID};
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        Cursor c = sqlLiteDatabase.query(TABLENAME2, null, PROJECT_ID + "=?"
                        + " and " + TYPE + "=?" + " and " + RECORDING_ID + "=?",
                selectionArg, null, null, null, null);
        int videoFileRecordingDuration = 0;
        try {
            c.moveToFirst();
            do {
                String tempDuration = c.getString(6);
                videoFileRecordingDuration = videoFileRecordingDuration
                        + Integer.parseInt(tempDuration);
            } while (c.moveToNext());
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }

        return videoFileRecordingDuration;

    }

    public ArrayList<ExportRecordingDetails> getShotFilesDetails(
            List<String> arg, String projectSelected) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] selectionArg = new String[arg.size() + 1];
        for (int i = 0; i < arg.size(); i++) {
            selectionArg[i] = arg.get(i);
        }
        selectionArg[arg.size()] = projectSelected;
        String placeHolders = "(";
        for (int j = 0; j < arg.size(); j++) {
            if (j != (arg.size() - 1))
                placeHolders = placeHolders.concat("?,");
            else
                placeHolders = placeHolders.concat("?");

        }
        placeHolders = placeHolders.concat(")");
        String query = "Select * from " + TABLENAME2 + " WHERE " + EVENT_ID
                + " IN " + placeHolders + " and " + PROJECT_ID + " =?";
        Cursor c = sqlLiteDatabase.rawQuery(query, selectionArg);
        List<ExportRecordingDetails> shotName = new ArrayList<ExportRecordingDetails>();
        try {
            c.moveToFirst();
            do {
                ExportRecordingDetails reocrdingDetails = new ExportRecordingDetails();
                reocrdingDetails.recordingId = c.getString(1);
                reocrdingDetails.shotId = c.getString(3);
                reocrdingDetails.duration = Integer.parseInt(c.getString(6));
                shotName.add(reocrdingDetails);
            } while (c.moveToNext());

        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return (ArrayList<ExportRecordingDetails>) shotName;
    }

    public int getStartPointInClip(String shotId, String recordingId) {

        String[] selectionArg = {ProjectConstants.selectedProjectName, "video", recordingId};
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        Cursor c = sqlLiteDatabase.query(TABLENAME2, null, PROJECT_ID + "=?" + " and " + TYPE + "=?" + " and " + RECORDING_ID + "=?",
                selectionArg, null, null, null, null);

        int clipStartPoint = 0;
        try {
            c.moveToFirst();
            while (!c.getString(3).equals(shotId)) {
                clipStartPoint = clipStartPoint + Integer.parseInt(c.getString(6));
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }

        return clipStartPoint;
    }

    public void getFilteredFilesList(List<String> arg, String projectSelected,
                                     BaseFragment fragment) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] selectionArg = new String[arg.size() + 1];
        for (int i = 0; i < arg.size(); i++) {
            selectionArg[i] = arg.get(i);
        }
        selectionArg[arg.size()] = projectSelected;
        String placeHolders = "(";
        for (int j = 0; j < arg.size(); j++) {
            if (j != (arg.size() - 1))
                placeHolders = placeHolders.concat("?,");
            else
                placeHolders = placeHolders.concat("?");

        }
        placeHolders = placeHolders.concat(")");
        String query = "Select * from " + TABLENAME2 + " WHERE " + EVENT_ID
                + " IN " + placeHolders + " and " + PROJECT_ID + " =?";
        Cursor c = sqlLiteDatabase.rawQuery(query, selectionArg);
        try {

            c.moveToFirst();
            if (c.getCount() == 0) {
                fragment.projectId = new String[0];
                fragment.recordingId = new String[0];
                fragment.endTime = new String[0];
                fragment.eventId = new String[0];
                fragment.type = new String[0];
                fragment.imageUri = new String[0];
                fragment.createdDate = new String[0];
            } else {
                int count = 0;
                int noOfRows = c.getCount();
                if (fragment != null) {

                    fragment.projectId = new String[noOfRows];
                    fragment.recordingId = new String[noOfRows];
                    fragment.endTime = new String[noOfRows];
                    fragment.eventId = new String[noOfRows];
                    fragment.type = new String[noOfRows];
                    fragment.imageUri = new String[noOfRows];
                    fragment.createdDate = new String[noOfRows];
                    c.moveToFirst();
                    while (count != noOfRows) {
                        fragment.projectId[count] = c.getString(0);
                        fragment.recordingId[count] = c.getString(1);
                        fragment.endTime[count] = c.getString(2);
                        fragment.eventId[count] = c.getString(3);
                        fragment.type[count] = c.getString(4);
                        fragment.imageUri[count] = c.getString(5);
                        fragment.createdDate[count] = c.getString(7);
                        count++;

                        c.moveToNext();
                    }
                    fragment.setListViewAdapterArgs(fragment.projectId,
                            fragment.recordingId, fragment.endTime,
                            fragment.eventId, fragment.type);
                }
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
    }

    public String getLastRecordingId(String projectSelected, String type) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();

        // String type = "audio";
        String[] selectionArg = {projectSelected, type};

        Cursor c = sqlLiteDatabase.query(TABLENAME2,
                new String[]{RECORDING_ID}, PROJECT_ID + "=?" + " and "
                        + TYPE + "=?", selectionArg, null, null, null, null);
        try {

            if (c.getCount() == 0) {
                return "0";
            }
            c.moveToFirst();
            while (!c.isLast()) {
                c.moveToNext();
                recordingID = c.getString(0);
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }

        return recordingID;
    }

    public void deletePaticularRecording(String projectId, String itemId,
                                         String time) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();

        // String type = "audio";
        String[] selectionArg = {projectId, itemId, time};
        int i = sqlLiteDatabase.delete(TABLENAME2, PROJECT_ID + "=?" + " and "
                + EVENT_ID + "=?" + " and " + END + "=?", selectionArg);
        System.out.println("****** The no of rows delted is " + i + projectId
                + itemId + " " + time);
    }

    public String getProjectImage(String projectName) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] args = {projectName};
        String projectNameFromCursor = "";

        Cursor c = sqlLiteDatabase.query(TABLENAME1,
                new String[]{IMAGE_URI}, PROJECT_NAME + "=?", args,
                null, null, null, null);
        try {
            c.moveToFirst();
            if (c.getCount() == 0) {

                return "na";
            }
            projectNameFromCursor = c.getString(0);
            if (!c.isClosed()) {
                c.close();
            }
            return projectNameFromCursor;

        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return "null";
    }

    // MOVE: this doenst belong here. Move to UI layer
    // method to get the Location - Lat Long of the project
    /*
    public List<CreateProjectActivity.LocationDetails> getLocationPoints() {
        SQLiteDatabase sqlLiteDatabase = this.getReadableDatabase();
        List<CreateProjectActivity.LocationDetails> latLongs = new ArrayList<>();
        // build raw query
        String raw = "SELECT * FROM " + TABLENAME1;

        // Fire query on cursor
        Cursor c = sqlLiteDatabase.rawQuery(raw, null);

        try {
            if (c.moveToFirst()) {
                do {
                    String[] columns = c.getColumnNames();
                    Log.v("=====>", Arrays.toString(columns));
                    String projectName = c.getString(c.getColumnIndex(PROJECT_NAME));
                    double LAT = c.getDouble(c.getColumnIndex(LATITUDE));
                    double LONG = c.getDouble(c.getColumnIndex(LONGITUDE));
                    Log.v("=====>", projectName + ", " + LAT + " : " + LONG);
                    latLongs.add(new CreateProjectActivity.LocationDetails(projectName, LAT, LONG));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return latLongs;
    }*/


    // helper methods to get the data fromDB for playing video
    public String[] getProjectMainDetails(String projectName) {
        SQLiteDatabase sqlLiteDatabase = this.getWritableDatabase();
        String[] args = {projectName};
        String details[] = {"", ""};
        Cursor c = sqlLiteDatabase.query(TABLENAME1,
                new String[]{PROJECT_ID, CATEGORY}, PROJECT_NAME + "=?", args, null, null,
                null, null);
        try {

            if (c != null) {
                c.moveToFirst();
                if (c.getCount() == 0) {
                    return details;
                }
                // get ProjectID and Category
                details[0] = c.getString(0);
                details[1] = c.getString(1);
            }
            if (!c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
            Log.e("===DB", e.getMessage());
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return details;
    }

}



