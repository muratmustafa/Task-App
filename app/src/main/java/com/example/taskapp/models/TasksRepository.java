package com.example.taskapp.models;

import java.util.List;

public interface TasksRepository {

    interface DataObserver {
        void onDataChanged();
    }

    List<Task> loadTasks();

    Task getTask(long id);

    void deleteFinishedTasks();

    void deleteTask(long id);

    void showUnfinishedTasks();

    void showAllTasks();

    void addNewTask(String shortName, String description, boolean done, String date);

    void undoTask(Task task);

    void updateTask(long id, String shortName, String description, boolean done);

    void updateDone(long id, boolean done);

    void setDataObserver(DataObserver observer);
}
