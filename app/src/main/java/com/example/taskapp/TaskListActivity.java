package com.example.taskapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.taskapp.adapters.TaskAdapter;
import com.example.taskapp.fragments.TaskDetailFragment;
import com.example.taskapp.fragments.TaskListFragment;
import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksLoader;
import com.example.taskapp.models.TasksRepository;
import com.example.taskapp.models.db.TasksDbRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Task>>,
        TaskListFragment.OnTaskSelectedListener {

    private TasksRepository mRepository;
    private TaskListFragment mTaskListFragment;

    public static final String TASK_DETAIL_FRAGMENT_TAG = "TASK_DETAIL_FRAGMENT_TAG";
    public static boolean mTabletMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_fragments);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        mTaskListFragment = (TaskListFragment) fm.findFragmentById(R.id.task_list_fragment);

        mRepository = TasksDbRepositoryImpl.getInstance(this);

        LoaderManager lm = LoaderManager.getInstance(this);
        lm.initLoader(0, null, this);

        View container = findViewById(R.id.task_detail_container);
        if (container != null) {
            mTabletMode = true;
            Fragment taskDetailFragment = fm.findFragmentById(R.id.task_detail_container);
            if (taskDetailFragment == null) {
                FragmentTransaction t = fm.beginTransaction();
                taskDetailFragment = TaskDetailFragment.newInstance();
                t.add(R.id.task_detail_container, taskDetailFragment, TASK_DETAIL_FRAGMENT_TAG);
                t.commit();
            }
        } else {
            mTabletMode = false;
        }
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
        mTaskListFragment.setTasks(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Task>> loader) {
        mTaskListFragment.setTasks(new ArrayList<Task>());
    }

    @Override
    public void onTaskSelected(Task task) {
        if(mTabletMode){
            FragmentManager fm = getSupportFragmentManager();
            TaskDetailFragment taskDetailFragment = (TaskDetailFragment) fm.findFragmentByTag(TASK_DETAIL_FRAGMENT_TAG);
            taskDetailFragment.setSelectedTask(task);
        }else{
            Intent inIntent = new Intent(TaskDetailActivity.INTENT_EDIT_ACTION);
            inIntent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, String.valueOf(task.getId()));
            startActivity(inIntent);
        }
    }
}
