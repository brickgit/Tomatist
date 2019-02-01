package com.brickgit.tomatist.view.activitylist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class ActivityListViewHolder extends RecyclerView.ViewHolder {

  private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

  private TextView mStartDateTime;
  private TextView mEndDateTime;

  private TextView mTitleView;
  private TextView mNoteView;

  private Activity mActivity;

  public ActivityListViewHolder(
      View view, final ActivityListAdapter.OnActivityClickListener onActivityClickListener) {
    super(view);

    mStartDateTime = view.findViewById(R.id.start_time);
    mEndDateTime = view.findViewById(R.id.end_time);

    mTitleView = view.findViewById(R.id.activity_title);
    mNoteView = view.findViewById(R.id.activity_note);

    itemView.setOnClickListener(
        (v) -> {
          if (onActivityClickListener == null) {
            return;
          }

          onActivityClickListener.onActivityClick(mActivity);
        });
  }

  public void bind(Activity activity) {
    mActivity = activity;

    mStartDateTime.setText(dateFormat.format(activity.getStartTime()));
    if (activity.getMinutes() != 0) {
      mEndDateTime.setText(dateFormat.format(activity.getEndTime()));
    } else {
      mEndDateTime.setText("");
    }

    mTitleView.setText(activity.getTitle());
    mNoteView.setText(activity.getNote());
  }
}
