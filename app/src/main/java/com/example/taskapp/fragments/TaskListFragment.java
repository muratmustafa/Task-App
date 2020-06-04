package com.example.taskapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.R;
import com.example.taskapp.TaskDetailActivity;
import com.example.taskapp.TaskListActivity;
import com.example.taskapp.adapters.TaskAdapter;
import com.example.taskapp.models.Task;
import com.example.taskapp.models.TasksRepository;
import com.example.taskapp.models.db.TasksDbRepositoryImpl;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TaskListFragment extends Fragment {

    private ArrayList<Task> mTasksList;
    private TaskAdapter mAdapter;
    private TasksRepository mRepository;

    private RecyclerView recyclerView;

    private OnTaskSelectedListener onTaskSelectedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksList = new ArrayList<>();
        mRepository = TasksDbRepositoryImpl.getInstance(getContext());
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void setUpRecyclerView(View view) {
        mAdapter = new TaskAdapter(mTasksList, onTaskSelectedListener);

        recyclerView = view.findViewById(R.id.tasks);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Task task = mTasksList.get(position);
                    mRepository.deleteTask(mTasksList.get(position).getId());
                    Snackbar.make(recyclerView, mTasksList.get(position).getShortName() + " deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mRepository.undoTask(task);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    mRepository.updateDone(mTasksList.get(position).getId(), !mTasksList.get(position).isDone());
                    break;
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX/6, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green))
                    .addSwipeRightActionIcon(R.drawable.ic_check_white_24dp)
                    .setActionIconTint(ContextCompat.getColor(recyclerView.getContext(), android.R.color.white))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX/6, dY, actionState, isCurrentlyActive);
        }
    };

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
