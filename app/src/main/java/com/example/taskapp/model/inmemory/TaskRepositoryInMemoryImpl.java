package com.example.taskapp.model.inmemory;

import com.example.taskapp.model.Task;
import com.example.taskapp.model.TaskRepository;

import java.util.ArrayList;

public class TaskRepositoryInMemoryImpl implements TaskRepository {

    private static TaskRepositoryInMemoryImpl instance;

    private ArrayList<Task> mTasks;
    private ArrayList<Task> mAllTasks;

    public static synchronized TaskRepositoryInMemoryImpl getInstance() {
        if (instance == null) {
            instance = new TaskRepositoryInMemoryImpl();
        }
        return instance;
    }

    private TaskRepositoryInMemoryImpl() {
        mTasks = new ArrayList<>();
        mAllTasks = new ArrayList<>();

        Task myTask = new Task("Empty the trash");
        myTask.setDescription("Someone has to get the dirty jobs done...");
        myTask.setDone(true);
        mTasks.add(myTask);
        mTasks.add(new Task("Groceries"));
        mTasks.add(new Task("Call parents"));
        myTask = new Task("Do Android programming");
        myTask.setDescription("Nobody said it would be easy!");
        myTask.setDone(true);
        mTasks.add(myTask);

        for (int i = 1; i < 4; i++)
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
                i--;
            }
        }

        mAllTasks.clear();
        mAllTasks.addAll(mTasks);
    }

    @Override
    public void showUnfinishedTasks() {

        ArrayList<Task> unfinishedTasks = new ArrayList<Task>();

        for (int i = 0; i < mTasks.size(); i++) {
            Task task = mTasks.get(i);
            if (!task.isDone()) {
                unfinishedTasks.add(task);
            }
        }

        mTasks.clear();
        mTasks.addAll(unfinishedTasks);
    }

    @Override
    public void showAllTasks() {
        mTasks.clear();
        mTasks.addAll(mAllTasks);
    }
}
