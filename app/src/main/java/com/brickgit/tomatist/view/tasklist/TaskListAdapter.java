package com.brickgit.tomatist.view.tasklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListAdapter
    extends ExpandableRecyclerViewAdapter<TaskListCategoryViewHolder, TaskListTaskViewHolder> {

  public interface OnTaskClickListener {
    void onTaskClick(Long taskId);
  }

  private OnTaskClickListener mOnTaskClickListener;

  public TaskListAdapter(List<? extends ExpandableGroup> groups) {
    super(groups);
  }

  public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
    mOnTaskClickListener = onTaskClickListener;
  }

  public void expandAll() {
    for (int i = 0; i < getItemCount(); i++) {
      if (!isGroupExpanded(i)) {
        toggleGroup(i);
      }
    }
  }

  @Override
  public TaskListCategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_holder_category, parent, false);
    return new TaskListCategoryViewHolder(view);
  }

  @Override
  public TaskListTaskViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_task, parent, false);
    return new TaskListTaskViewHolder(view, mOnTaskClickListener);
  }

  @Override
  public void onBindGroupViewHolder(
      TaskListCategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
    holder.setGroupTitle(group);
  }

  @Override
  public void onBindChildViewHolder(
      TaskListTaskViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
    TaskListCategoryViewData taskListCategoryViewData = (TaskListCategoryViewData) group;
    TaskListTaskViewData taskListTaskViewData = taskListCategoryViewData.getItems().get(childIndex);
    holder.bind(taskListTaskViewData);
  }
}
