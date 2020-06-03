package com.example.taskapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskapp.R;
import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksRepository;
import com.example.taskapp.models.db.TasksDbRepositoryImpl;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class TaskDetailFragment extends Fragment {

    private int flag = 0;

    private Task mTask;
    private TasksRepository mRepository;

    private CheckBox mDone;
    private EditText mShortName, mDescription;
    private TextView mCreationDate;

    public static TaskDetailFragment newInstance(){
        return new TaskDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);

        mRepository = TasksDbRepositoryImpl.getInstance(Objects.requireNonNull(getContext()));

        mShortName = view.findViewById(R.id.shortNameEditText);
        mDescription = view.findViewById(R.id.descriptionEditText);
        mCreationDate = view.findViewById(R.id.dateTextView);
        mDone = view.findViewById(R.id.doneCheckBox);
        Button save = view.findViewById(R.id.addNewTask);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        return view;
    }

    private void saveTask(){
        if (flag == 1){
            mRepository.updateTask(mTask.getId(), mShortName.getText().toString(), mDescription.getText().toString(), mDone.isChecked());
        }else{
            mRepository.addNewTask(mShortName.getText().toString(), mDescription.getText().toString(), mDone.isChecked(), mCreationDate.getText().toString());
        }
    }

    public void setSelectedTask(Task task){
        mTask = task;

        mShortName.setText(task.getShortName());
        mDescription.setText(task.getDescription());
        mCreationDate.setText(DateFormat.getDateInstance().format(task.getCreationDate()));
        mDone.setChecked(task.isDone());

        flag = 1;
    }

    public void newTask(){
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        mShortName.setText("");
        mDescription.setText("");
        mCreationDate.setText(currentDate);
        mDone.setChecked(false);

        flag = 0;
    }
}
