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
    void OnCategoryGroupClick(CategoryGroup categoryGroup);
  }

  private List<CategoryGroup> mCategoryGroups = new ArrayList<>();

  private OnCategoryGroupClickListener mOnCategoryGroupClickListener;

  public CategoryGroupListAdapter() {}

  public void updateCategoryGroups(List<CategoryGroup> categoryGroups) {
    mCategoryGroups.clear();
    mCategoryGroups.addAll(categoryGroups);
    notifyDataSetChanged();
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
