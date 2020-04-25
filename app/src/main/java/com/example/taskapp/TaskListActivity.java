package com.example.taskapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.adapters.TaskAdapter;
import com.example.taskapp.models.Task;
import com.example.taskapp.models.inmemory.TaskRepositoryInMemoryImpl;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    private ArrayList<Task> tasksList;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private TaskRepositoryInMemoryImpl taskRepo;

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

        taskRepo = TaskRepositoryInMemoryImpl.getInstance();

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        switch (item.getItemId()){
            case R.id.allTasks:
                Toast.makeText(context, "Show all tasks", duration).show();
                taskRepo.showAllTasks();
                adapter.notifyDataSetChanged();
                break;
            case R.id.unfinishedTasks:
                Toast.makeText(context, "Show unfinished tasks", duration).show();
                taskRepo.showUnfinishedTasks();
                adapter.notifyDataSetChanged();
                break;
            case  R.id.deleteFinishedTasks:
                Toast.makeText(context, "Delete finished tasks", duration).show();
                taskRepo.deleteFinishedTasks();
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

        return true;
    }
}
