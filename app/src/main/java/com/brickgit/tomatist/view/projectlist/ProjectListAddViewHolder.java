package com.brickgit.tomatist.view.projectlist;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/16. */
public class ProjectListAddViewHolder extends RecyclerView.ViewHolder {

  public ProjectListAddViewHolder(
      View view, final ProjectListAdapter.OnProjectClickListener onProjectClickListener) {
    super(view);

    itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (onProjectClickListener == null) {
              return;
            }

            onProjectClickListener.onAddProjectClick();
          }
        });
  }
}
