package com.brickgit.tomatist.view;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.Task;

import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/14. */
public class CheckableTaskItemViewHolder extends RecyclerView.ViewHolder {

  private CheckBox mIsCheckedView;
  private TextView mTitleView;

  private Task mTask;

  public CheckableTaskItemViewHolder(
      View view, final TaskListAdapter.OnTaskClickListener onTaskClickListener) {
    super(view);

    mIsCheckedView = view.findViewById(R.id.task_is_checked);
    mTitleView = view.findViewById(R.id.task_title);

    itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (onTaskClickListener == null) {
              return;
            }

            onTaskClickListener.onTaskClick(mTask);
          }
        });

    mIsCheckedView.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (onTaskClickListener == null) {
              return;
            }
            mTask.setFinished(isChecked);
            onTaskClickListener.onTaskCheck(mTask);
          }
        });
  }

  public void bind(Task task) {
    mTask = task;
    mIsCheckedView.setChecked(task.isFinished());
    mTitleView.setText(task.getTitle());
    mTitleView.setPaintFlags(
        task.isFinished() ? Paint.STRIKE_THRU_TEXT_FLAG : Paint.ANTI_ALIAS_FLAG);
  }
}
