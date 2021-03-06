package com.kiwi.auready.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiwi.auready.data.source.local.PersistenceContract.FriendEntry;
import com.kiwi.auready.data.source.local.PersistenceContract.MemberEntry;
import com.kiwi.auready.data.source.local.PersistenceContract.NotificationEntry;
import com.kiwi.auready.data.source.local.PersistenceContract.TaskEntry;
import com.kiwi.auready.data.source.local.PersistenceContract.TaskHeadEntry;

import java.util.List;

import static com.kiwi.auready.data.source.local.PersistenceContract.DBExceptionTag.INSERT_ERROR;
import static com.kiwi.auready.data.source.local.PersistenceContract.DBExceptionTag.TAG_SQLITE;
import static com.kiwi.auready.data.source.local.PersistenceContract.SQL_CREATE_TABLE.DATABASE_NAME;
import static com.kiwi.auready.data.source.local.PersistenceContract.SQL_CREATE_TABLE.DATABASE_VERSION;
import static com.kiwi.auready.data.source.local.PersistenceContract.SQL_CREATE_TABLE.SQL_CREATE_FRIEND_TABLE;
import static com.kiwi.auready.data.source.local.PersistenceContract.SQL_CREATE_TABLE.SQL_CREATE_MEMBER_TABLE;
import static com.kiwi.auready.data.source.local.PersistenceContract.SQL_CREATE_TABLE.SQL_CREATE_NOTIFICATION_TABLE;
import static com.kiwi.auready.data.source.local.PersistenceContract.SQL_CREATE_TABLE.SQL_CREATE_TASKHEAD_TABLE;
import static com.kiwi.auready.data.source.local.PersistenceContract.SQL_CREATE_TABLE.SQL_CREATE_TASK_TABLE;

/*
* Local Database helper
* */
public class SQLiteDBHelper extends SQLiteOpenHelper {

    // Object mapper for serializing and deserializing JSON strings
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static SQLiteDBHelper sDbHelper = null;
    private static SQLiteDatabase sDb = null;

    /*
    * Constructor
    * */
    private SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    * Create new SQLiteDBHelper instance
    * */
    public static SQLiteDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // dont accidentally leak an Activity's context
        if (sDbHelper == null) {
            sDbHelper = new SQLiteDBHelper(context.getApplicationContext());

            try {
//                sDb = sDbHelper.getWritableDatabase();
//                Log.d(TAG, "db path is " + sDb.getPath());
            } catch (SQLException e) {
                Log.e(TAG_SQLITE, "Could not create and/or open the database ( " + DATABASE_NAME + " ) that will be used for reading and writing.", e);
            }
        }
        return sDbHelper;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FRIEND_TABLE);
        db.execSQL(SQL_CREATE_TASKHEAD_TABLE);
        db.execSQL(SQL_CREATE_MEMBER_TABLE);
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FriendEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TaskHeadEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MemberEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + NotificationEntry.TABLE_NAME);

            onCreate(db);
        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        sDb = sDbHelper.getWritableDatabase();

        long isSuccess = INSERT_ERROR;
        sDb.beginTransaction();
        try {
            isSuccess = sDb.insertOrThrow(table, nullColumnHack, values);
            sDb.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG_SQLITE, "Error insert new one to ( " + table + " ). ", e);
        } finally {
            sDb.endTransaction();
        }
        return isSuccess;
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        sDb = sDbHelper.getReadableDatabase();

        return sDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    /*
    * return value; isSuccess - is not invoked any exception
    * */
    public boolean delete(String table, String whereClause, String[] whereArgs) {
        sDb = sDbHelper.getWritableDatabase();

        boolean isSuccess = true;
        sDb.beginTransaction();
        try {
            sDb.delete(table, whereClause, whereArgs);
            sDb.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG_SQLITE, "Could not delete the column in ( " + DATABASE_NAME + "). ", e);
            isSuccess = false;
        } finally {
            sDb.endTransaction();
        }
        return isSuccess;
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        sDb = sDbHelper.getReadableDatabase();
        return sDb.rawQuery(sql, selectionArgs);
    }

    boolean execSQL(String sql) {
        sDb = sDbHelper.getWritableDatabase();
        sDb.beginTransaction();
        try {
            sDb.execSQL(sql);
            sDb.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG_SQLITE, "Could not delete taskheads in ( " + DATABASE_NAME + "). ", e);
            return false;
        } finally {
            sDb.endTransaction();
        }
        return true;
    }

    public long insertTaskHeadAndMembers(ContentValues taskHeadValues, List<ContentValues> memberValuesList) {
        sDb = sDbHelper.getWritableDatabase();

        long isSuccessAll = INSERT_ERROR;
        sDb.beginTransaction();
        try {
            // Save TaskHead
            long isSuccessOfTaskHead = sDb.insertOrThrow(TaskHeadEntry.TABLE_NAME, null, taskHeadValues);

            // Save members
            long isSuccessOfMember = INSERT_ERROR;
            for (ContentValues values : memberValuesList) {
                isSuccessOfMember = sDb.insertOrThrow(MemberEntry.TABLE_NAME, null, values);
            }
            if (isSuccessOfTaskHead != INSERT_ERROR && isSuccessOfMember != INSERT_ERROR) {
                isSuccessAll = isSuccessOfTaskHead; // any value is ok, if not INSERT_ERROR
                sDb.setTransactionSuccessful();
            }
        } catch (SQLException e) {
            Log.e(TAG_SQLITE, "Error insert new one to ( " + TaskHeadEntry.TABLE_NAME + " and " +
                    MemberEntry.TABLE_NAME + " ). ", e);
        } finally {
            sDb.endTransaction();
        }
        return isSuccessAll;
    }

    public boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {

        sDb = sDbHelper.getWritableDatabase();

        int numOfRows = 0;
        sDb.beginTransaction();
        try {
            numOfRows = sDb.update(table, values, whereClause, whereArgs);
            sDb.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG_SQLITE, "Error update to ( " + table + " ). ", e);
        } finally {
            sDb.endTransaction();
        }
        return numOfRows > 0;
    }

    public long replace(String table, String nullColumnHack, ContentValues values) {
        sDb = sDbHelper.getWritableDatabase();

        sDb.beginTransaction();
        try {
            sDb.setTransactionSuccessful();
            return sDb.replace(table, nullColumnHack, values);
        } catch (SQLException e) {
            Log.e(TAG_SQLITE, "Error inserting " + values, e);
            return PersistenceContract.DBExceptionTag.REPLACE_ERROR;
        } finally {
            sDb.endTransaction();
        }
    }

}

