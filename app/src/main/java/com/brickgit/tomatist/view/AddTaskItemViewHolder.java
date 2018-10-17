package com.brickgit.tomatist.view;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/16. */
public class AddTaskItemViewHolder extends RecyclerView.ViewHolder {

  public AddTaskItemViewHolder(
      View view, final TaskListAdapter.OnTaskClickListener onTaskClickListener) {
    super(view);

    itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (onTaskClickListener == null) {
              return;
            }

            onTaskClickListener.onNewTaskLick();
          }
        });
  }
}
