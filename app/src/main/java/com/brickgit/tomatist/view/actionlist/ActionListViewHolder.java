package com.brickgit.tomatist.view.actionlist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

public class ActionListViewHolder extends RecyclerView.ViewHolder {

  private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

  private TextView mStartDateTime;
  private TextView mEndDateTime;

  private TextView mTitleView;
  private TextView mNoteView;

  private TextView mCategoryGroupView;
  private TextView mCategoryView;

  private Action mAction;

  public ActionListViewHolder(
      View view, final ActionListAdapter.OnActionClickListener onActionClickListener) {
    super(view);

    mStartDateTime = view.findViewById(R.id.start_time);
    mEndDateTime = view.findViewById(R.id.end_time);

    mTitleView = view.findViewById(R.id.action_title);
    mNoteView = view.findViewById(R.id.action_note);

    mCategoryGroupView = view.findViewById(R.id.action_category_group);
    mCategoryView = view.findViewById(R.id.action_category);

    itemView.setOnClickListener(
        (v) -> {
          if (onActionClickListener == null) {
            return;
          }

          onActionClickListener.onActionClick(mAction);
        });
  }

  public void bind(
      Action action, Map<Long, CategoryGroup> categoryGroups, Map<Long, Category> categories) {
    mAction = action;

    if (action.getStartTime() != null) {
      mStartDateTime.setText(dateFormat.format(action.getStartTime()));
      if (action.getMinutes() != 0) {
        mEndDateTime.setText(dateFormat.format(action.getEndTime()));
      } else {
        mEndDateTime.setText("");
      }
    } else {
      mStartDateTime.setText("");
      mEndDateTime.setText("");
    }

    mTitleView.setText(action.getTitle());
    mNoteView.setText(action.getNote());

    Category category = categories.get(action.getCategoryId());
    if (category != null) {
      mCategoryView.setText(category.getTitle());
      CategoryGroup group = categoryGroups.get(category.getGroupId());
      mCategoryGroupView.setText(group != null ? group.getTitle() : "");
    } else {
      mCategoryView.setText("");
      mCategoryGroupView.setText("");
    }
  }
}
