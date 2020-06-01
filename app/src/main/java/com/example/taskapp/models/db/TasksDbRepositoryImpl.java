package com.example.taskapp.models.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.taskapp.models.db.TasksSQLiteOpenHelper.*;

public class TasksDbRepositoryImpl implements TasksRepository {

    private static TasksDbRepositoryImpl INSTANCE = null;

    private TasksSQLiteOpenHelper mOpenHelper = null;
    private DataObserver mDataObserver;

    private ArrayList<Task> mTasks;

    public static TasksDbRepositoryImpl getInstance(@NonNull Context context){
        if (INSTANCE == null){
            INSTANCE = new TasksDbRepositoryImpl(context);
        }
        return  INSTANCE;
    }

    private TasksDbRepositoryImpl(@NonNull Context context) {

        mOpenHelper = new TasksSQLiteOpenHelper(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);

        mTasks = new ArrayList<>();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public List<Task> loadTasks() {
        mTasks.clear();

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        String[] projection = {KEY_ID,
                               KEY_SHORT_NAME,
                               KEY_DESCRIPTION,
                               KEY_DATE,
                               KEY_DONE};

        String selection = KEY_HIDE + " = 1";

        Cursor c = db.query(DB_TASKS_TABLE,
                projection,
                selection,
                null,
                null,
                null,
                null);

        if (c != null && c.getCount() > 0){
            while(c.moveToNext()){
                long id = c.getInt(c.getColumnIndexOrThrow(KEY_ID));
                String shortName = c.getString(c.getColumnIndexOrThrow(KEY_SHORT_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(KEY_DESCRIPTION));
                String date = c.getString(c.getColumnIndexOrThrow(KEY_DATE));
                boolean done = c.getInt(c.getColumnIndexOrThrow(KEY_DONE)) == 1;

                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                Date d = new Date();
                try {
                    d = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Task task = new Task(id, shortName);
                task.setDescription(description);
                task.setCreationDate(d);
                task.setDone(done);
                mTasks.add(task);
            }
            c.close();
        }

        db.close();

        return mTasks;
    }

    @Override
    public void deleteFinishedTasks() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        db.delete(DB_TASKS_TABLE, KEY_DONE + " = 1" , null);

        if (mDataObserver != null){
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void showUnfinishedTasks() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HIDE, 0);

        db.update(DB_TASKS_TABLE, values, KEY_DONE + " = 1", null);

        if (mDataObserver != null){
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void showAllTasks() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HIDE, 1);

        db.update(DB_TASKS_TABLE, values, KEY_HIDE + " = 0", null);

        if (mDataObserver != null){
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void addNewTask(String shortName, String description, boolean done, String date) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SHORT_NAME, shortName);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DONE, done);
        values.put(KEY_DATE, date);

        db.insert(DB_TASKS_TABLE, null, values);

        db.close();

        if (mDataObserver != null){
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void updateTask(long pos, String shortName, String description, boolean done) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Log.d("taskupdate", pos + " " + shortName);

        ContentValues values = new ContentValues();
        values.put(KEY_SHORT_NAME, shortName);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DONE, done);

        db.update(DB_TASKS_TABLE, values, KEY_ID + " = ?", new String[]{Long.toString(pos + 1)});
    }

    @Override
    public void updateDone(long pos, boolean done) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DONE, done);

        db.update(DB_TASKS_TABLE, values, KEY_ID + " = ?", new String[]{Long.toString(pos)});
    }

    @Override
    public void setDataObserver(DataObserver observer) {
        mDataObserver = observer;
    }
}
