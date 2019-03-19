package com.brickgit.tomatist.view.categorylist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.CategoryGroup;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryGroupListViewHolder extends RecyclerView.ViewHolder {

  private TextView mCategoryGroupTitleView;

  private CategoryGroup mCategoryGroup;

  public CategoryGroupListViewHolder(
      View view,
      CategoryGroupListAdapter.OnCategoryGroupClickListener onCategoryGroupClickListener) {
    super(view);

    mCategoryGroupTitleView = view.findViewById(R.id.category_group_title);

    itemView.setOnClickListener(
        (v) -> {
          if (onCategoryGroupClickListener == null) {
            return;
          }

          onCategoryGroupClickListener.onCategoryGroupClick(mCategoryGroup);
        });
  }

  public void bind(CategoryGroup categoryGroup) {
    mCategoryGroup = categoryGroup;

    mCategoryGroupTitleView.setText(categoryGroup.getTitle());
  }
}
