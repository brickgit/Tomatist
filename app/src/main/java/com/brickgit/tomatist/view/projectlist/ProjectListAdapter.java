package com.brickgit.tomatist.view.projectlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.DatabaseLoader;
import com.brickgit.tomatist.data.Project;
import com.brickgit.tomatist.data.ProjectDao;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2018/10/14. */
public class ProjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements ProjectListTouchHelperListener {

  private static final int VIEW_TYPE_ADD_PROJECT = 0;
  private static final int VIEW_TYPE_PROJECT = 1;

  private static final int ADD_PROJECT_BUTTON_POSITION = 0;

  public interface OnProjectClickListener {
    void onAddProjectClick();

    void onProjectClick(Project project);

    void onProjectCheck(Project project);
  }

  private boolean mIsReordering = false;

  private List<Project> mProjects = new LinkedList<>();
  private OnProjectClickListener mOnProjectClickListener;

  public ProjectListAdapter() {}

  public void updateProjects(List<Project> projects) {

    if (mIsReordering) return;

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
              .inflate(R.layout.view_holder_add_project, parent, false);
      return new ProjectListAddViewHolder(view, mOnProjectClickListener);
    } else {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.view_holder_project, parent, false);
      return new ProjectListViewHolder(view, mOnProjectClickListener);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == VIEW_TYPE_PROJECT) {
      ProjectListViewHolder viewHolder = (ProjectListViewHolder) holder;
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

  @Override
  public void onItemSelect() {
    mIsReordering = true;
  }

  @Override
  public void onItemUnselect() {
    mIsReordering = false;
  }

  @Override
  public void onItemMove(int fromPosition, int toPosition) {
    int fromData = fromPosition - 1;
    int toData = toPosition - 1;
    if (fromData < toPosition) {
      for (int i = fromData; i < toData; i++) {
        swapProjects(i, i + 1);
      }
    } else {
      for (int i = fromData; i > toData; i--) {
        swapProjects(i, i - 1);
      }
    }

    notifyItemMoved(fromPosition, toPosition);
  }

  @Override
  public void onItemDismiss(int position) {
    mProjects.remove(position - 1);
    notifyItemRemoved(position - 1);
  }

  private void swapProjects(int from, int to) {
    Project fromProject = mProjects.get(from);
    Project toProject = mProjects.get(to);

    Collections.swap(mProjects, from, to);

    long order = fromProject.getOrder();
    fromProject.setOrder(toProject.getOrder());
    toProject.setOrder(order);

    ProjectDao dao = DatabaseLoader.getAppDatabase().projectDao();
    dao.updateProject(fromProject);
    dao.updateProject(toProject);
  }
}
