package com.brickgit.tomatist.view.tasklist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListTaskViewHolder extends ChildViewHolder {

  private TextView mTaskTitleView;

  private TaskListTaskViewData mViewData;

  public TaskListTaskViewHolder(
      View view, TaskListAdapter.OnTaskClickListener onTaskClickListener) {
    super(view);
    mTaskTitleView = view.findViewById(R.id.task_title);

    itemView.setOnClickListener((v) -> onTaskClickListener.onTaskClick(mViewData.getTaskId()));
  }

  public void bind(TaskListTaskViewData taskListTaskViewData) {
    mViewData = taskListTaskViewData;
    mTaskTitleView.setText(taskListTaskViewData.getTaskTitle());
  }
}
