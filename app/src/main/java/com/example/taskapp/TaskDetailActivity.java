package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.taskapp.fragments.TaskDetailFragment;
import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksRepository;
import com.example.taskapp.models.db.TasksDbRepositoryImpl;

import java.util.Objects;

public class TaskDetailActivity extends AppCompatActivity{

    public static final String EXTRA_TASK_ID = "EXTRA_TASK_ID";
    public static final String INTENT_EDIT_ACTION = "com.example.taskapp.ACTION_EDIT";
    public static final String INTENT_ADD_ACTION = "com.example.taskapp.ACTION_ADD";

    private static final String TASK = "TASK_OBJECT";

    private Task mTask;
    private TasksRepository mRepository;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TASK, mTask);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRepository = TasksDbRepositoryImpl.getInstance(this);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction t = fm.beginTransaction();
            TaskDetailFragment taskDetailFragment = TaskDetailFragment.newInstance();
            t.add(R.id.task_detail_container, taskDetailFragment);
            t.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent outIntent = getIntent();

        FragmentManager fragmentManager = getSupportFragmentManager();
        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) fragmentManager.findFragmentById(R.id.task_detail_container);

        if (Objects.equals(outIntent.getAction(), INTENT_EDIT_ACTION)){
            long mTaskID = Integer.parseInt(Objects.requireNonNull(outIntent.getStringExtra(EXTRA_TASK_ID)));
            mTask = mRepository.getTask(mTaskID);
            taskDetailFragment.setSelectedTask(mTask);

        }else if(Objects.equals(outIntent.getAction(), INTENT_ADD_ACTION)){
            taskDetailFragment.newTask();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
