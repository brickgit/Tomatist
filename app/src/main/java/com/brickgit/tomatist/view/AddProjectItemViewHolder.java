package com.brickgit.tomatist.view;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/16. */
public class AddProjectItemViewHolder extends RecyclerView.ViewHolder {

  public AddProjectItemViewHolder(
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
