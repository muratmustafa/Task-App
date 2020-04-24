package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taskapp.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class TaskDetailActivity extends AppCompatActivity {

    private CheckBox mDone;
    private EditText mShortName, mDescription;
    private Task mTask;
    private static final String TASK = "TASK_OBJECT";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TASK, mTask);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

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
        TextView date = findViewById(R.id.date);
        date.setText(currentDate);

        FloatingActionButton save = findViewById(R.id.save);

        mDone = findViewById(R.id.done);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    public void saveTask(){
        mShortName = findViewById(R.id.shortName);
        mDescription = findViewById(R.id.description);

        mTask = new Task(mShortName.getText().toString());
        mTask.setDescription(mDescription.getText().toString());
        mTask.setDone(mDone.isChecked());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
