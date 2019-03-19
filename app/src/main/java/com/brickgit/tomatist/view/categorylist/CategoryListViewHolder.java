package com.brickgit.tomatist.view.categorylist;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Category;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryListViewHolder extends RecyclerView.ViewHolder {

  private TextView mCategoryTitleView;

  private Category mCategory;

  public CategoryListViewHolder(
      View view, CategoryListAdapter.OnCategoryClickListener onCategoryClickListener) {
    super(view);

    mCategoryTitleView = view.findViewById(R.id.category_title);

    itemView.setOnClickListener(
        (v) -> {
          if (onCategoryClickListener == null) {
            return;
          }

          onCategoryClickListener.onCategoryClick(mCategory);
        });
  }

  public void bind(Category category) {
    mCategory = category;

    mCategoryTitleView.setText(category.getTitle());
  }
}
