package com.example.taskapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.adapter.TaskAdapter;
import com.example.taskapp.model.Task;
import com.example.taskapp.model.inmemory.TaskRepositoryInMemoryImpl;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    private ArrayList<Task> tasksList;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;

    private static final String TASKS = "TASK_OBJECTS";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TASKS, tasksList);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TaskRepositoryInMemoryImpl taskRepo = TaskRepositoryInMemoryImpl.getInstance();

        if (savedInstanceState != null){
            tasksList = savedInstanceState.getParcelableArrayList(TASKS);
            Log.d("savedInstanceState", "savedInstanceState not null");
        }else{
            tasksList = taskRepo.loadTasks();
            Log.d("savedInstanceState", "savedInstanceState null");
        }

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        adapter = new TaskAdapter(tasksList);

        recyclerView = findViewById(R.id.tasks);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_list_menu, menu);
        return true;
    }
}
