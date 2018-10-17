package com.brickgit.tomatist.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/14. */
public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int VIEW_TYPE_ADD_TASK = 0;
  private static final int VIEW_TYPE_CHECKABLE_TASK = 1;

  private static final int ADD_TASK_BUTTON_POSITION = 0;

  public interface OnTaskClickListener {
    void onNewTaskLick();

    void onTaskClick(Task task);

    void onTaskCheck(Task task);
  }

  private List<Task> mTasks = new ArrayList<>();
  private OnTaskClickListener mOnTaskClickListener;

  public TaskListAdapter() {}

  public void updateTasks(List<Task> tasks) {
    mTasks.clear();
    mTasks.addAll(tasks);
    notifyDataSetChanged();
  }

  public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
    mOnTaskClickListener = onTaskClickListener;
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ADD_TASK) {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.add_task_item_view_holder, parent, false);
      return new AddTaskItemViewHolder(view, mOnTaskClickListener);
    } else {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.checkable_task_item_view_holder, parent, false);
      return new CheckableTaskItemViewHolder(view, mOnTaskClickListener);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == VIEW_TYPE_CHECKABLE_TASK) {
      CheckableTaskItemViewHolder viewHolder = (CheckableTaskItemViewHolder) holder;
      viewHolder.bind(mTasks.get(position - 1));
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == ADD_TASK_BUTTON_POSITION) {
      return VIEW_TYPE_ADD_TASK;
    } else {
      return VIEW_TYPE_CHECKABLE_TASK;
    }
  }

  @Override
  public int getItemCount() {
    return mTasks.size() + 1;
  }
}
