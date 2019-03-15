package com.brickgit.tomatist.view.actionlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public interface OnActionClickListener {
    void onActionClick(Action action);
  }

  private String mTag = "";
  private List<Action> mActions = new LinkedList<>();
  private OnActionClickListener mOnActionClickListener;

  private Map<String, CategoryGroup> mCategoryGroups = new HashMap<>();
  private Map<String, Category> mCategories = new HashMap<>();

  public ActionListAdapter() {}

  public void updateActions(String tag, List<Action> actions) {
    if (!mTag.equals(tag)) {
      mTag = tag;
      mActions.clear();
      mActions.addAll(actions);
      notifyDataSetChanged();
    } else {
      if (mActions.isEmpty()) {
        mActions.addAll(actions);
        notifyDataSetChanged();
      } else if (mActions.size() > actions.size()) {
        for (int index = 0; index < mActions.size(); index++) {
          Action action = mActions.get(index);
          if (!actions.contains(action)) {
            mActions.remove(index);
            notifyItemRemoved(index);
            return;
          }
        }
      } else if (actions.size() > mActions.size()) {
        for (int index = 0; index < actions.size(); index++) {
          Action action = actions.get(index);
          if (!mActions.contains(action)) {
            mActions.add(index, action);
            notifyItemInserted(index);
            return;
          }
        }
      } else {
        mActions.clear();
        mActions.addAll(actions);
        notifyDataSetChanged();
      }
    }
  }

  public void updateCategoryGroups(Map<String, CategoryGroup> categoryGroups) {
    mCategoryGroups.clear();
    mCategoryGroups.putAll(categoryGroups);
    notifyDataSetChanged();
  }

  public void updateCategories(Map<String, Category> categories) {
    mCategories.clear();
    mCategories.putAll(categories);
    notifyDataSetChanged();
  }

  public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
    mOnActionClickListener = onActionClickListener;
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_holder_action, parent, false);
    return new ActionListViewHolder(view, mOnActionClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ActionListViewHolder viewHolder = (ActionListViewHolder) holder;
    viewHolder.bind(mActions.get(position), mCategoryGroups, mCategories);
  }

  @Override
  public int getItemCount() {
    return mActions.size();
  }
}
