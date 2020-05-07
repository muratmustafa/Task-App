package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksRepository;
import com.example.taskapp.models.inmemory.TasksRepositoryInMemoryImpl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class TaskDetailActivity extends AppCompatActivity{

    private static final String TASK = "TASK_OBJECT";
    public static final String EXTRA_TASK_POSITION = "EXTRA_TASK_POSITION";

    private int flag = 0;

    private CheckBox mDone;
    private EditText mShortName, mDescription;
    private TextView mCreationDate;

    private Task mTask;
    private ArrayList<Task> mTasksList;
    private TasksRepository mRepository;

    public static void startActivity(Context context, int taskPosition) {

        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra(EXTRA_TASK_POSITION, taskPosition);

        context.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TASK, mTask);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState != null){
            mTask = savedInstanceState.getParcelable(TASK);
            Log.d("savedInstanceState", "not null");
        }else{
            Log.d("savedInstanceState", "null");
        }

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        mRepository = TasksRepositoryInMemoryImpl.getInstance();
        mTasksList = new ArrayList<Task>(mRepository.loadTasks());

        mShortName = findViewById(R.id.shortNameEditText);
        mDescription = findViewById(R.id.descriptionEditText);
        mCreationDate = findViewById(R.id.dateTextView);
        mDone = findViewById(R.id.doneCheckBox);

        mCreationDate.setText(currentDate);

        Button saveFAB = findViewById(R.id.addNewTask);
        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

        int taskPosition = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra(EXTRA_TASK_POSITION)));

        if (taskPosition != -1){

            mTask = mTasksList.get(taskPosition);

            mShortName.setText(mTask.getShortName());
            mDescription.setText(mTask.getDescription());
            mDone.setChecked(mTask.isDone());
            mCreationDate.setText(DateFormat.getDateInstance().format(mTask.getCreationDate()));

            flag = 1;
        }
    }

    public void saveTask(){

        if (flag == 1){
            mRepository.updateTask(mTask.getId(), mShortName.getText().toString(), mDescription.getText().toString(), mDone.isChecked());
            onBackPressed();
        }else{
            Task task = new Task(mShortName.getText().toString());
            task.setDescription(mDescription.getText().toString());
            task.setDone(mDone.isChecked());
            mRepository.addNewTask(task);
            onBackPressed();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
