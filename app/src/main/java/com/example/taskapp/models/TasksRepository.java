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

    void setDataObserver(DataObserver observer);

    // TODO: add methods for adding new or updating existing tasks
}
