package com.brickgit.tomatist.view.categorylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public interface OnCategoryClickListener {
    void OnCategoryClick(Category category);
  }

  private List<Category> mCategories = new ArrayList<>();

  private OnCategoryClickListener mOnCategoryClickListener;

  public CategoryListAdapter() {}

  public void updateCategories(List<Category> categories) {
    mCategories.clear();
    mCategories.addAll(categories);
    notifyDataSetChanged();
  }

  public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
    mOnCategoryClickListener = onCategoryClickListener;
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_holder_category, parent, false);
    return new CategoryListViewHolder(view, mOnCategoryClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    CategoryListViewHolder viewHolder = (CategoryListViewHolder) holder;
    viewHolder.bind(mCategories.get(position));
  }

  @Override
  public int getItemCount() {
    return mCategories.size();
  }
}
