package com.example.taskapp.models.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TasksSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myTasksDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String DB_TASKS_TABLE = "myTasksTable";
    public static final String KEY_ID = "_id";
    public static final String KEY_SHORT_NAME = "shortName";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";
    public static final String KEY_DONE = "done";
    public static final String KEY_HIDE = "hide";

    private static final String SQL_CREATE_DATABASE = "create table " + DB_TASKS_TABLE + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_SHORT_NAME + " text not null,"
            + KEY_DESCRIPTION + " text,"
            + KEY_DATE + " text,"
            + KEY_DONE + " integer,"
            + KEY_HIDE + " integer DEFAULT 1);";

    public TasksSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_TASKS_TABLE + ";");
        onCreate(db);
    }
}
