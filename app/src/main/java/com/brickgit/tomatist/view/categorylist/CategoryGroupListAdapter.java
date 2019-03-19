package com.brickgit.tomatist.view.categorylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryGroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public interface OnCategoryGroupClickListener {
    void onCategoryGroupClick(CategoryGroup categoryGroup);
  }

  private List<CategoryGroup> mCategoryGroups = new ArrayList<>();

  private OnCategoryGroupClickListener mOnCategoryGroupClickListener;

  public CategoryGroupListAdapter() {}

  public void updateCategoryGroups(List<CategoryGroup> categoryGroups) {
    if (mCategoryGroups.isEmpty()) {
      mCategoryGroups.addAll(categoryGroups);
      notifyDataSetChanged();
    } else if (mCategoryGroups.size() > categoryGroups.size()) {
      for (int index = 0; index < mCategoryGroups.size(); index++) {
        CategoryGroup categoryGroup = mCategoryGroups.get(index);
        if (!categoryGroups.contains(categoryGroup)) {
          mCategoryGroups.remove(index);
          notifyItemRemoved(index);
          return;
        }
      }
    } else if (categoryGroups.size() > mCategoryGroups.size()) {
      for (int index = 0; index < categoryGroups.size(); index++) {
        CategoryGroup categoryGroup = categoryGroups.get(index);
        if (!mCategoryGroups.contains(categoryGroup)) {
          mCategoryGroups.add(index, categoryGroup);
          notifyItemInserted(index);
          return;
        }
      }
    } else {
      mCategoryGroups.clear();
      mCategoryGroups.addAll(categoryGroups);
      notifyDataSetChanged();
    }
  }

  public void setOnCategoryGroupClickListener(
      OnCategoryGroupClickListener onCategoryGroupClickListener) {
    mOnCategoryGroupClickListener = onCategoryGroupClickListener;
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_holder_category_group, parent, false);
    return new CategoryGroupListViewHolder(view, mOnCategoryGroupClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    CategoryGroupListViewHolder viewHolder = (CategoryGroupListViewHolder) holder;
    viewHolder.bind(mCategoryGroups.get(position));
  }

  @Override
  public int getItemCount() {
    return mCategoryGroups.size();
  }
}
