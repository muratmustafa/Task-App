package com.example.taskapp.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class TasksLoader extends AsyncTaskLoader<List<Task>> implements TasksRepository.DataObserver{

    private TasksRepository mRepository;

    public TasksLoader(@NonNull Context context, TasksRepository repository) {
        super(context);

        mRepository = repository;
        mRepository.setDataObserver(this);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<Task> loadInBackground() {
        return mRepository.loadTasks();
    }

    @Override
    public void onDataChanged() {
        onContentChanged();
    }
}
