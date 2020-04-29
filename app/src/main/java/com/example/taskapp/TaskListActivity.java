package com.example.taskapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.adapters.TaskAdapter;
import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksLoader;
import com.example.taskapp.models.TasksRepository;
import com.example.taskapp.models.inmemory.TasksRepositoryInMemoryImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Task>> {

    private ArrayList<Task> mTasksList;
    private TaskAdapter mAdapter;
    private TasksRepository mRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRepository = TasksRepositoryInMemoryImpl.getInstance();

        LoaderManager lm = LoaderManager.getInstance(this);
        lm.initLoader(0, null, this);

        mTasksList = new ArrayList<Task>();

        FloatingActionButton addTask = findViewById(R.id.add_task);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDetailActivity.startActivity(TaskListActivity.this, -1);
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        mAdapter = new TaskAdapter(mTasksList);

        RecyclerView recyclerView = findViewById(R.id.tasks);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
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
                mRepository.showAllTasks();
                break;
            case R.id.unfinishedTasks:
                Toast.makeText(context, "Show unfinished tasks", duration).show();
                mRepository.showUnfinishedTasks();
                break;
            case  R.id.deleteFinishedTasks:
                Toast.makeText(context, "Delete finished tasks", duration).show();
                mRepository.deleteFinishedTasks();
                break;
            default:
                break;
        }

        return true;
    }

    @NonNull
    @Override
    public Loader<List<Task>> onCreateLoader(int id, @Nullable Bundle args) {
        return new TasksLoader(this, mRepository);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Task>> loader, List<Task> data) {
        setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Task>> loader) {
        setData(new ArrayList<Task>());
    }

    public void setData(List<Task> data){
        mTasksList.clear();
        mTasksList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }
}
