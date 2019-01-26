package com.brickgit.tomatist.view.activitylist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;

import androidx.recyclerview.widget.RecyclerView;

public class ActivityListViewHolder extends RecyclerView.ViewHolder {

  private TextView mTitleView;
  private TextView mNoteView;

  private Activity mActivity;

  public ActivityListViewHolder(
      View view, final ActivityListAdapter.OnActivityClickListener onActivityClickListener) {
    super(view);

    mTitleView = view.findViewById(R.id.activity_title);
    mNoteView = view.findViewById(R.id.activity_note);

    itemView.setOnClickListener((v) -> {
      if (onActivityClickListener == null) {
        return;
      }

      onActivityClickListener.onActivityClick(mActivity);
    });
  }

  public void bind(Activity activity) {
    mActivity = activity;
    mTitleView.setText(activity.getTitle());
    mNoteView.setText(activity.getNote());
  }
}
