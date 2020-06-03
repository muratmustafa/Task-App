package com.example.taskapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.R;
import com.example.taskapp.TaskDetailActivity;
import com.example.taskapp.TaskListActivity;
import com.example.taskapp.adapters.TaskAdapter;
import com.example.taskapp.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {

    private ArrayList<Task> mTasksList;
    private TaskAdapter mAdapter;

    private OnTaskSelectedListener onTaskSelectedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTasksList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        Button addTask = view.findViewById(R.id.addNewTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TaskListActivity.mTabletMode){
                    Intent inIntent = new Intent(TaskDetailActivity.INTENT_ADD_ACTION);
                    startActivity(inIntent);
                }else{
                    FragmentManager fragmentManager = getFragmentManager();
                    TaskDetailFragment taskDetailFragment = (TaskDetailFragment) fragmentManager.findFragmentById(R.id.task_detail_container);
                    taskDetailFragment.newTask();
                }
            }
        });

        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {
        mAdapter = new TaskAdapter(mTasksList, onTaskSelectedListener);

        RecyclerView recyclerView = view.findViewById(R.id.tasks);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    public void setTasks(List<Task> tasks){
        mTasksList.clear();
        mTasksList.addAll(tasks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onTaskSelectedListener = (OnTaskSelectedListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement onTaskSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onTaskSelectedListener = null;
    }

    public interface OnTaskSelectedListener{
        void onTaskSelected(Task task);
    }
}
