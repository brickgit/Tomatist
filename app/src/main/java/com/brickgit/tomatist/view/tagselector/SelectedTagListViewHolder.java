package com.brickgit.tomatist.view.tagselector;

import android.view.View;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Tag;

import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/3/18. */
public class SelectedTagListViewHolder extends RecyclerView.ViewHolder {

  private TextView mTagTitleView;
  private Tag mTag;

  public SelectedTagListViewHolder(View view, OnTagClickListener onTagClickListener) {
    super(view);
    mTagTitleView = view.findViewById(R.id.tag_title);
    itemView.setOnClickListener(
        (v) -> {
          if (onTagClickListener == null) {
            return;
          }
          onTagClickListener.onTagClick(mTag);
        });
  }

  public void bind(Tag tag) {
    mTag = tag;
    mTagTitleView.setText(tag.getTitle());
  }
}
