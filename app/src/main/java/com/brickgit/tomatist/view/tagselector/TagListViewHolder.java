package com.brickgit.tomatist.view.tagselector;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Tag;

import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/3/18. */
public class TagListViewHolder extends RecyclerView.ViewHolder {

  private CheckBox mTagCheckBox;
  private TextView mTagTitleView;
  private Tag mTag;

  public TagListViewHolder(View view, OnTagClickListener onTagClickListener) {
    super(view);
    mTagCheckBox = view.findViewById(R.id.tag_check_box);
    mTagTitleView = view.findViewById(R.id.tag_title);
    itemView.setOnClickListener(
        (v) -> {
          if (onTagClickListener == null) {
            return;
          }
          onTagClickListener.onTagClick(mTag);
        });
  }

  public void bind(Tag tag, boolean isSelected) {
    mTag = tag;
    mTagCheckBox.setChecked(isSelected);
    mTagTitleView.setText(tag.getTitle());
  }
}
