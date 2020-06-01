package com.example.taskapp.models;

import java.util.List;

public interface TasksRepository {

    interface DataObserver {
        void onDataChanged();
    }

    List<Task> loadTasks();

    void deleteFinishedTasks();

    void showUnfinishedTasks();

    void showAllTasks();

    void addNewTask(String shortName, String description, boolean done, String date);

    void updateTask(long pos, String shortName, String description, boolean done);

    void updateDone(long pos, boolean done);

    void setDataObserver(DataObserver observer);
}
