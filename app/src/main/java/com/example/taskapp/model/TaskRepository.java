package com.example.taskapp.model;

import java.util.List;

public interface TaskRepository {

    List<Task> loadTasks();

    void deleteFinishedTasks();

    void addTask(String shortName);

    void updateTask(long id);

    // TODO: add methods for adding new or updating existing tasks
}
