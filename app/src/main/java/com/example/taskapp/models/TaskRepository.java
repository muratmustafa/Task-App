package com.example.taskapp.models;

import java.util.List;

public interface TaskRepository {

    List<Task> loadTasks();

    void deleteFinishedTasks();

    void showUnfinishedTasks();

    void showAllTasks();

    // TODO: add methods for adding new or updating existing tasks
}
