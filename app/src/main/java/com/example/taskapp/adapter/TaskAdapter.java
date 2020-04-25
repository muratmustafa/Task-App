package com.example.taskapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.R;
import com.example.taskapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    private final List<Task> mTaskList;

    public TaskAdapter(List<Task> mTaskList) {
        this.mTaskList = mTaskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent,false);

        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.mShortName.setText(mTaskList.get(position).getShortName());
        holder.mDone.setChecked(mTaskList.get(position).isDone());

        holder.mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskList.get(position).setDone(holder.mDone.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        CheckBox mDone;
        TextView mShortName;

        public TaskViewHolder(View itemView){
            super(itemView);

            mDone = itemView.findViewById(R.id.done);
            mShortName = itemView.findViewById(R.id.shortName);
        }
    }
}
