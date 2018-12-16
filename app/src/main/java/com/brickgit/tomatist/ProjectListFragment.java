package com.brickgit.tomatist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.brickgit.tomatist.data.Database;
import com.brickgit.tomatist.data.DatabaseLoader;
import com.brickgit.tomatist.data.Project;
import com.brickgit.tomatist.data.ProjectDao;
import com.brickgit.tomatist.view.projectlist.ProjectListTouchHelperCallback;
import com.brickgit.tomatist.view.projectlist.ProjectListAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** A placeholder fragment containing a simple view. */
public class ProjectListFragment extends Fragment {

  private RecyclerView mTaskList;
  private LinearLayoutManager mLayoutManager;
  private ProjectListAdapter mProjectListAdapter;

  private Database mDatabase;

  public ProjectListFragment() {}

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_project_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    mTaskList = view.findViewById(R.id.project_list);
    mTaskList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(getActivity());
    mTaskList.setLayoutManager(mLayoutManager);

    mProjectListAdapter = new ProjectListAdapter();
    mTaskList.setAdapter(mProjectListAdapter);
    ItemTouchHelper.Callback callback = new ProjectListTouchHelperCallback(mProjectListAdapter);
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mTaskList);

    mDatabase = DatabaseLoader.getAppDatabase();
    ProjectDao dao = mDatabase.projectDao();
    List<Project> projects = dao.getProjects();
    mProjectListAdapter.updateProjects(projects);
    mProjectListAdapter.setOnTaskClickListener(
        new ProjectListAdapter.OnProjectClickListener() {
          @Override
          public void onAddProjectClick() {
            gotoAddProjectActivity();
          }

          @Override
          public void onProjectClick(Project project) {
            showDeleteProjectDialog(project);
          }

          @Override
          public void onProjectCheck(Project project) {
            mProjectListAdapter.notifyDataSetChanged();
          }
        });
  }

  private void gotoAddProjectActivity() {
    Intent intent = new Intent(getActivity(), AddProjectActivity.class);
    startActivity(intent);
  }

  private void showDeleteProjectDialog(final Project project) {
    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
    normalDialog.setTitle("Deleting project");
    normalDialog.setMessage("Are you sure to delete " + project.getTitle() + "?");
    normalDialog.setPositiveButton(
        "Confirm",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            deleteProject(project);
          }
        });
    normalDialog.setNegativeButton("Cancel", null);
    normalDialog.show();
  }

  private void deleteProject(Project project) {
    ProjectDao dao = mDatabase.projectDao();
    dao.deleteProject(project);
    List<Project> projects = dao.getProjects();
    mProjectListAdapter.updateProjects(projects);
  }
}
