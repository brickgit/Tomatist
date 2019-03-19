package com.brickgit.tomatist.view.tagselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Tag;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/3/18. */
public class SelectedTagListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private OnTagClickListener mOnTagClickListener;

  private List<Tag> mTags = new ArrayList<>();

  public void updateTags(List<Tag> tags) {
    mTags.clear();
    mTags.addAll(tags);
    notifyDataSetChanged();
  }

  public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
    mOnTagClickListener = onTagClickListener;
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_holder_selected_tag, parent, false);
    return new SelectedTagListViewHolder(view, mOnTagClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    SelectedTagListViewHolder viewHolder = (SelectedTagListViewHolder) holder;
    viewHolder.bind(mTags.get(position));
  }

  @Override
  public int getItemCount() {
    return mTags.size();
  }
}
