package com.brickgit.tomatist.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.Project;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/14. */
public class ProjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int VIEW_TYPE_ADD_PROJECT = 0;
  private static final int VIEW_TYPE_PROJECT = 1;

  private static final int ADD_PROJECT_BUTTON_POSITION = 0;

  public interface OnProjectClickListener {
    void onAddProjectClick();

    void onProjectClick(Project project);

    void onProjectCheck(Project project);
  }

  private List<Project> mProjects = new ArrayList<>();
  private OnProjectClickListener mOnProjectClickListener;

  public ProjectListAdapter() {}

  public void updateProjects(List<Project> projects) {
    mProjects.clear();
    mProjects.addAll(projects);
    notifyDataSetChanged();
  }

  public void setOnTaskClickListener(OnProjectClickListener onProjectClickListener) {
    mOnProjectClickListener = onProjectClickListener;
  }

  @Override
  @NonNull
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ADD_PROJECT) {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.add_project_item_view_holder, parent, false);
      return new AddProjectItemViewHolder(view, mOnProjectClickListener);
    } else {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.project_item_view_holder, parent, false);
      return new ProjectItemViewHolder(view, mOnProjectClickListener);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == VIEW_TYPE_PROJECT) {
      ProjectItemViewHolder viewHolder = (ProjectItemViewHolder) holder;
      viewHolder.bind(mProjects.get(position - 1));
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == ADD_PROJECT_BUTTON_POSITION) {
      return VIEW_TYPE_ADD_PROJECT;
    } else {
      return VIEW_TYPE_PROJECT;
    }
  }

  @Override
  public int getItemCount() {
    return mProjects.size() + 1;
  }
}
