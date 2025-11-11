package com.duongnguyen.m_hike.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duongnguyen.m_hike.models.Hike;
import com.duongnguyen.m_hike.models.Observation;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mhike.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHelper";

    // Hikes Table
    public static final String TABLE_HIKES = "hikes";
    public static final String COLUMN_HIKE_ID = "id";
    public static final String COLUMN_HIKE_NAME = "name";
    public static final String COLUMN_HIKE_LOCATION = "location";
    public static final String COLUMN_HIKE_DATE = "hike_date";
    public static final String COLUMN_PARKING_AVAILABLE = "parking_available";
    public static final String COLUMN_HIKE_LENGTH = "length";
    public static final String COLUMN_HIKE_DIFFICULTY = "difficulty";
    public static final String COLUMN_HIKE_DESCRIPTION = "description";

    // Observations Table
    public static final String TABLE_OBSERVATIONS = "observations";
    public static final String COLUMN_OBS_ID = "id";
    public static final String COLUMN_OBS_OBSERVATION = "observation";
    public static final String COLUMN_OBS_TIME = "observation_time";
    public static final String COLUMN_OBS_COMMENTS = "comments";
    public static final String COLUMN_OBS_HIKE_ID = "hike_id"; // Foreign Key

    private static final String CREATE_TABLE_HIKES = "CREATE TABLE " + TABLE_HIKES + " ("
            + COLUMN_HIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_HIKE_NAME + " TEXT NOT NULL, "
            + COLUMN_HIKE_LOCATION + " TEXT NOT NULL, "
            + COLUMN_HIKE_DATE + " TEXT NOT NULL, "
            + COLUMN_PARKING_AVAILABLE + " TEXT NOT NULL, "
            + COLUMN_HIKE_LENGTH + " REAL NOT NULL, "
            + COLUMN_HIKE_DIFFICULTY + " TEXT NOT NULL, "
            + COLUMN_HIKE_DESCRIPTION + " TEXT);";

    private static final String CREATE_TABLE_OBSERVATIONS = "CREATE TABLE " + TABLE_OBSERVATIONS + " ("
            + COLUMN_OBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_OBS_OBSERVATION + " TEXT NOT NULL, "
            + COLUMN_OBS_TIME + " TEXT NOT NULL, "
            + COLUMN_OBS_COMMENTS + " TEXT, "
            + COLUMN_OBS_HIKE_ID + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_OBS_HIKE_ID + ") REFERENCES " + TABLE_HIKES + "(" + COLUMN_HIKE_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(TAG, "Creating tables...");
            db.execSQL(CREATE_TABLE_HIKES);
            db.execSQL(CREATE_TABLE_OBSERVATIONS);
            Log.d(TAG, "Tables created successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }

    public void addHike(Hike hike) {
        if (hike == null) {
            Log.e(TAG, "Cannot add null hike");
            return;
        }
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_HIKE_NAME, hike.getName());
            values.put(COLUMN_HIKE_LOCATION, hike.getLocation());
            values.put(COLUMN_HIKE_DATE, hike.getHikeDate());
            values.put(COLUMN_PARKING_AVAILABLE, hike.getParkingAvailable());
            values.put(COLUMN_HIKE_LENGTH, hike.getLength());
            values.put(COLUMN_HIKE_DIFFICULTY, hike.getDifficulty());
            values.put(COLUMN_HIKE_DESCRIPTION, hike.getDescription());

            db.insert(TABLE_HIKES, null, values);
            Log.d(TAG, "Successfully added hike: " + hike.getName());
        } catch (SQLException e) {
            Log.e(TAG, "Error adding hike", e);
        }
    }

    public ArrayList<Hike> getAllHikes() {
        ArrayList<Hike> hikes = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_HIKES, null, null, null, null, null, COLUMN_HIKE_NAME + " ASC")) {

            if (cursor.moveToFirst()) {
                do {
                    Hike hike = new Hike();
                    hike.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HIKE_ID)));
                    hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME)));
                    hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LOCATION)));
                    hike.setHikeDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DATE)));
                    hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARKING_AVAILABLE)));
                    hike.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LENGTH)));
                    hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DIFFICULTY)));
                    hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DESCRIPTION)));
                    hikes.add(hike);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving hikes", e);
        }
        Log.d(TAG, "Retrieved " + hikes.size() + " hikes");
        return hikes;
    }
    
    public Hike getHikeById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Hike hike = null;
        try {
            cursor = db.query(TABLE_HIKES, null, COLUMN_HIKE_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null);

            if (cursor.moveToFirst()) {
                hike = new Hike();
                hike.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HIKE_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LOCATION)));
                hike.setHikeDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DATE)));
                hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARKING_AVAILABLE)));
                hike.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LENGTH)));
                hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DIFFICULTY)));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DESCRIPTION)));
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving hike by ID", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return hike;
    }
    
    public void updateHike(Hike hike) {
        if (hike == null) return;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            int rows;
            ContentValues values = new ContentValues();
            values.put(COLUMN_HIKE_NAME, hike.getName());
            values.put(COLUMN_HIKE_LOCATION, hike.getLocation());
            values.put(COLUMN_HIKE_DATE, hike.getHikeDate());
            values.put(COLUMN_PARKING_AVAILABLE, hike.getParkingAvailable());
            values.put(COLUMN_HIKE_LENGTH, hike.getLength());
            values.put(COLUMN_HIKE_DIFFICULTY, hike.getDifficulty());
            values.put(COLUMN_HIKE_DESCRIPTION, hike.getDescription());

            rows = db.update(TABLE_HIKES, values, COLUMN_HIKE_ID + "=?",
                    new String[]{String.valueOf(hike.getId())});
            Log.d(TAG, "Updated " + rows + " rows for hike: " + hike.getName());
        } catch (SQLException e) {
            Log.e(TAG, "Error updating hike", e);
        }
    }
    
    public void deleteHike(long id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            int obsRows = db.delete(TABLE_OBSERVATIONS, COLUMN_OBS_HIKE_ID + "=?",
                    new String[]{String.valueOf(id)});
            Log.d(TAG, "Deleted " + obsRows + " observations for hike ID: " + id);

            int hikeRows = db.delete(TABLE_HIKES, COLUMN_HIKE_ID + "=?",
                    new String[]{String.valueOf(id)});
            Log.d(TAG, "Deleted " + hikeRows + " hike with ID: " + id);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting hike", e);
        }
    }

    public void deleteAllData() {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Delete all rows from both tables
            db.delete(TABLE_OBSERVATIONS, null, null);
            db.delete(TABLE_HIKES, null, null);
            Log.d(TAG, "All hikes and observations deleted.");
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting all data", e);
        }
    }

    public ArrayList<Hike> searchHikesByName(String namePattern) {
        ArrayList<Hike> hikes = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(
                TABLE_HIKES,
                null,
                COLUMN_HIKE_NAME + " LIKE ?",
                new String[]{"%" + namePattern + "%"},
                null,
                null,
                COLUMN_HIKE_NAME + " ASC"
        )) {
            if (cursor.moveToFirst()) {
                do {
                    Hike hike = new Hike();
                    hike.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HIKE_ID)));
                    hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME)));
                    hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LOCATION)));
                    hike.setHikeDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DATE)));
                    hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARKING_AVAILABLE)));
                    hike.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LENGTH)));
                    hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DIFFICULTY)));
                    hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DESCRIPTION)));
                    hikes.add(hike);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error searching hikes", e);
        }
        Log.d(TAG, "Found " + hikes.size() + " hikes from search");
        return hikes;
    }
    
    public void addObservation(Observation observation) {
        if (observation == null) {
            Log.e(TAG, "Cannot add null observation");
            return;
        }
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_OBS_OBSERVATION, observation.getObservation());
            values.put(COLUMN_OBS_TIME, observation.getObservationTime());
            values.put(COLUMN_OBS_COMMENTS, observation.getComments());
            values.put(COLUMN_OBS_HIKE_ID, observation.getHikeId());

            db.insert(TABLE_OBSERVATIONS, null, values);
            Log.d(TAG, "Successfully added observation for hike ID: " + observation.getHikeId());
        } catch (SQLException e) {
            Log.e(TAG, "Error adding observation", e);
        }
    }
    
    public ArrayList<Observation> getAllObservationsForHike(long hikeId) {
        ArrayList<Observation> observations = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(TABLE_OBSERVATIONS, null, COLUMN_OBS_HIKE_ID + "=?",
                new String[]{String.valueOf(hikeId)}, null, null, COLUMN_OBS_TIME + " DESC")) {

            if (cursor.moveToFirst()) {
                do {
                    Observation obs = new Observation();
                    obs.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_OBS_ID)));
                    obs.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_OBSERVATION)));
                    obs.setObservationTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_TIME)));
                    obs.setComments(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_COMMENTS)));
                    obs.setHikeId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_OBS_HIKE_ID)));
                    observations.add(obs);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving observations", e);
        }
        Log.d(TAG, "Retrieved " + observations.size() + " observations for hike ID: " + hikeId);
        return observations;
    }
    
    public void deleteObservation(long observationId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            int rows = db.delete(TABLE_OBSERVATIONS, COLUMN_OBS_ID + "=?",
                    new String[]{String.valueOf(observationId)});
            Log.d(TAG, "Deleted " + rows + " observation with ID: " + observationId);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting observation", e);
        }
    }

}