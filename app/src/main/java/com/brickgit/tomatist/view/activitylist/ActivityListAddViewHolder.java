package com.brickgit.tomatist.view.activitylist;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ActivityListAddViewHolder extends RecyclerView.ViewHolder {

  public ActivityListAddViewHolder(
      View view, final ActivityListAdapter.OnActivityClickListener onActivityClickListener) {
    super(view);

    itemView.setOnClickListener((v) -> {
      if (onActivityClickListener == null) {
        return;
      }

      onActivityClickListener.onAddActivityClick();
    });
  }
}
