package com.example.taskapp.models.inmemory;

import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksRepository;

import java.util.ArrayList;

public class TasksRepositoryInMemoryImpl implements TasksRepository {

    private static TasksRepositoryInMemoryImpl instance;

    private ArrayList<Task> mTasks;
    private ArrayList<Task> mAllTasks;
    private DataObserver mDataObserver;

    public static synchronized TasksRepositoryInMemoryImpl getInstance() {
        if (instance == null) {
            instance = new TasksRepositoryInMemoryImpl();
        }
        return instance;
    }

    private TasksRepositoryInMemoryImpl() {
        mTasks = new ArrayList<>();
        mAllTasks = new ArrayList<>();

        /*Task myTask = new Task("Empty the trash");
        myTask.setDescription("Someone has to get the dirty jobs done...");
        myTask.setDone(true);
        mTasks.add(myTask);
        mTasks.add(new Task("Groceries"));
        mTasks.add(new Task("Call parents"));
        myTask = new Task("Do Android programming");
        myTask.setDescription("Nobody said it would be easy!");
        myTask.setDone(true);
        mTasks.add(myTask);*/

        for (int i = 1; i < 6; i++)
          mTasks.add(new Task("Task - " + i));

        mAllTasks.addAll(mTasks);
    }

    @Override
    public ArrayList<Task> loadTasks() {
        return mTasks;
    }

    @Override
    public void deleteFinishedTasks() {
        for (int i=0; i<mTasks.size(); i++) {
            Task task = mTasks.get(i);
            if (task.isDone()) {
                mTasks.remove(task);
                if (mDataObserver != null) {
                    mDataObserver.onDataChanged();
                }
                i--;
            }
        }
        mAllTasks.clear();
        mAllTasks.addAll(mTasks);
    }

    @Override
    public void showUnfinishedTasks() {

        ArrayList<Task> unfinishedTasks = new ArrayList<>();

        for (int i = 0; i < mTasks.size(); i++) {
            Task task = mTasks.get(i);
            if (!task.isDone()) {
                unfinishedTasks.add(task);
            }
        }

        mTasks.clear();
        mTasks.addAll(unfinishedTasks);
        if (mDataObserver != null) {
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void showAllTasks() {
        mTasks.clear();
        mTasks.addAll(mAllTasks);
        if (mDataObserver != null) {
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void addNewTask(Task task) {
        mTasks.add(task);
        mAllTasks.add(task);
        if (mDataObserver != null) {
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void updateTask(int id, String shortName, String description, boolean done) {
        mTasks.get(id)
                .setShortName(shortName);
        mTasks.get(id)
                .setDescription(description);
        mTasks.get(id)
                .setDone(done);
        if (mDataObserver != null){
            mDataObserver.onDataChanged();
        }
    }

    @Override
    public void setDataObserver(DataObserver observer) {
        mDataObserver = observer;
    }
}
