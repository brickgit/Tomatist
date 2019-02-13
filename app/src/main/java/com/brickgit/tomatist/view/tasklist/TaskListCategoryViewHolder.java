package com.brickgit.tomatist.view.tasklist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListCategoryViewHolder extends GroupViewHolder {

  private TextView mCategoryTitleView;

  public TaskListCategoryViewHolder(View view) {
    super(view);
    mCategoryTitleView = view.findViewById(R.id.category_title);
  }

  public void setGroupTitle(ExpandableGroup group) {
    mCategoryTitleView.setText(group.getTitle());
  }
}
