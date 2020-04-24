package com.example.taskapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.adapter.TaskAdapter;
import com.example.taskapp.model.Task;
import com.example.taskapp.model.inmemory.TaskRepositoryInMemoryImpl;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private List<Task> tasksList;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TaskRepositoryInMemoryImpl taskRepo = TaskRepositoryInMemoryImpl.getInstance();

        tasksList = taskRepo.loadTasks();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        adapter = new TaskAdapter(tasksList);

        recyclerView = findViewById(R.id.tasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}
