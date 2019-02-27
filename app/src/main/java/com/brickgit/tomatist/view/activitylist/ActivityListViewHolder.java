package com.brickgit.tomatist.view.activitylist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Activity;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

public class ActivityListViewHolder extends RecyclerView.ViewHolder {

  private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

  private TextView mStartDateTime;
  private TextView mEndDateTime;

  private TextView mTitleView;
  private TextView mNoteView;

  private TextView mCategoryGroupView;
  private TextView mCategoryView;

  private Activity mActivity;

  public ActivityListViewHolder(
      View view, final ActivityListAdapter.OnActivityClickListener onActivityClickListener) {
    super(view);

    mStartDateTime = view.findViewById(R.id.start_time);
    mEndDateTime = view.findViewById(R.id.end_time);

    mTitleView = view.findViewById(R.id.activity_title);
    mNoteView = view.findViewById(R.id.activity_note);

    mCategoryGroupView = view.findViewById(R.id.activity_category_group);
    mCategoryView = view.findViewById(R.id.activity_category);

    itemView.setOnClickListener(
        (v) -> {
          if (onActivityClickListener == null) {
            return;
          }

          onActivityClickListener.onActivityClick(mActivity);
        });
  }

  public void bind(
      Activity activity, Map<Long, CategoryGroup> categoryGroups, Map<Long, Category> categories) {
    mActivity = activity;

    if (activity.getStartTime() != null) {
      mStartDateTime.setText(dateFormat.format(activity.getStartTime()));
      if (activity.getMinutes() != 0) {
        mEndDateTime.setText(dateFormat.format(activity.getEndTime()));
      } else {
        mEndDateTime.setText("");
      }
    } else {
      mStartDateTime.setText("");
      mEndDateTime.setText("");
    }

    mTitleView.setText(activity.getTitle());
    mNoteView.setText(activity.getNote());

    Category category = categories.get(activity.getCategoryId());
    if (category != null) {
      mCategoryView.setText(category.getTitle());
      CategoryGroup group = categoryGroups.get(category.getCategoryGroupId());
      mCategoryGroupView.setText(group != null ? group.getTitle() : "");
    } else {
      mCategoryView.setText("");
      mCategoryGroupView.setText("");
    }
  }
}
