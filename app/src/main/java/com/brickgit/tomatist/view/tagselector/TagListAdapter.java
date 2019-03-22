package com.brickgit.tomatist.view.tagselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/3/18. */
public class TagListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private OnTagClickListener mOnTagClickListener;

  private List<Tag> mTagList = new ArrayList<>();
  private Map<String, Tag> mSelectedTagMap = new HashMap<>();

  private String mFilterString = "";
  private List<Tag> mFilteredTagList = new ArrayList<>();

  public void updateTagList(List<Tag> tagList) {
    mTagList.clear();
    mTagList.addAll(tagList);
    notifyDataSetChanged();
  }

  public void updateSelectedTagMap(Map<String, Tag> selectedTagMap) {
    mSelectedTagMap.clear();
    mSelectedTagMap.putAll(selectedTagMap);
    notifyDataSetChanged();
  }

  public void updateFilterString(String filterString) {
    mFilterString = filterString;
    mFilteredTagList.clear();
    if (!filterString.isEmpty()) {
      for (Tag tag : mTagList) {
        if (tag.getTitle().contains(filterString)) {
          mFilteredTagList.add(tag);
        }
      }
    }
    notifyDataSetChanged();
  }

  public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
    mOnTagClickListener = onTagClickListener;
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_tag, parent, false);
    return new TagListViewHolder(view, mOnTagClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    Tag tag = mFilterString.isEmpty() ? mTagList.get(position) : mFilteredTagList.get(position);
    TagListViewHolder viewHolder = (TagListViewHolder) holder;
    viewHolder.bind(tag, mSelectedTagMap.containsKey(tag.getId()));
  }

  @Override
  public int getItemCount() {
    return mFilterString.isEmpty() ? mTagList.size() : mFilteredTagList.size();
  }
}
