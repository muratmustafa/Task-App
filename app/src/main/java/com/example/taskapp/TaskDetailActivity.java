package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksRepository;
import com.example.taskapp.models.db.TasksDbRepositoryImpl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class TaskDetailActivity extends AppCompatActivity{

    public static final String EXTRA_TASK_POSITION = "EXTRA_TASK_POSITION";
    public static final String INTENT_EDIT_ACTION = "com.example.taskapp.ACTION_EDIT";
    public static final String INTENT_ADD_ACTION = "com.example.taskapp.ACTION_ADD";

    private static final String TASK = "TASK_OBJECT";

    private int flag = 0;
    private long mTaskPosition;

    private CheckBox mDone;
    private EditText mShortName, mDescription;
    private TextView mCreationDate;

    private Task mTask;
    private ArrayList<Task> mTasksList;
    private TasksRepository mRepository;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TASK, mTask);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState != null){
            mTask = savedInstanceState.getParcelable(TASK);
        }

        mRepository = TasksDbRepositoryImpl.getInstance(this);

        mTasksList = new ArrayList<Task>(mRepository.loadTasks());

        mShortName = findViewById(R.id.shortNameEditText);
        mDescription = findViewById(R.id.descriptionEditText);
        mCreationDate = findViewById(R.id.dateTextView);
        mDone = findViewById(R.id.doneCheckBox);

        Button saveButton = findViewById(R.id.addNewTask);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent outIntent = getIntent();

        if (Objects.equals(outIntent.getAction(), INTENT_EDIT_ACTION)){
            mTaskPosition = Integer.parseInt(Objects.requireNonNull(outIntent.getStringExtra(EXTRA_TASK_POSITION)));

            mTask = mTasksList.get(((int) mTaskPosition));

            mShortName.setText(mTask.getShortName());
            mDescription.setText(mTask.getDescription());
            mDone.setChecked(mTask.isDone());
            mCreationDate.setText(DateFormat.getDateInstance().format(mTask.getCreationDate()));

            flag = 1;

        }else if(Objects.equals(outIntent.getAction(), INTENT_ADD_ACTION)){
            Calendar calendar = Calendar.getInstance();
            String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
            mCreationDate.setText(currentDate);
        }
    }

    public void saveTask(){
        if (flag == 1){
            mRepository.updateTask(mTaskPosition, mShortName.getText().toString(), mDescription.getText().toString(), mDone.isChecked());
            onBackPressed();
        }else{
            mRepository.addNewTask(mShortName.getText().toString(), mDescription.getText().toString(), mDone.isChecked(), mCreationDate.getText().toString());
            onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
