package com.brickgit.tomatist.view;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.Project;

import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/14. */
public class ProjectItemViewHolder extends RecyclerView.ViewHolder {

  private TextView mTitleView;

  private Project mProject;

  public ProjectItemViewHolder(
      View view, final ProjectListAdapter.OnProjectClickListener onProjectClickListener) {
    super(view);

    mTitleView = view.findViewById(R.id.project_title);

    itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (onProjectClickListener == null) {
              return;
            }

            onProjectClickListener.onProjectClick(mProject);
          }
        });
  }

  public void bind(Project project) {
    mProject = project;
    mTitleView.setText(project.getTitle());
  }
}
