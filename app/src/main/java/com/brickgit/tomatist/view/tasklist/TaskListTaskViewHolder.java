package com.brickgit.tomatist.view.tasklist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListTaskViewHolder extends ChildViewHolder {

  private TextView mTaskTitleView;

  public TaskListTaskViewHolder(View view) {
    super(view);
    mTaskTitleView = view.findViewById(R.id.task_title);
  }

  public void bind(TaskListTaskViewData taskListTaskViewData) {
    mTaskTitleView.setText(taskListTaskViewData.getTaskTitle());
  }
}
