package com.brickgit.tomatist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Project;
import com.brickgit.tomatist.view.projectlist.ProjectListAdapter;
import com.brickgit.tomatist.view.projectlist.ProjectListTouchHelperCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectListActivity extends BaseActivity {

  private RecyclerView mProjectList;
  private LinearLayoutManager mLayoutManager;
  private ProjectListAdapter mProjectListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_project_list);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener((view) -> {
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null)
          .show();
    });

    mProjectList = findViewById(R.id.project_list);
    mProjectList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(this);
    mProjectList.setLayoutManager(mLayoutManager);

    mProjectListAdapter = new ProjectListAdapter();
    mProjectList.setAdapter(mProjectListAdapter);
    ItemTouchHelper.Callback callback = new ProjectListTouchHelperCallback(mProjectListAdapter);
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mProjectList);
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

    mProjectViewModel
        .getProjects()
        .observe(this, (projects) -> mProjectListAdapter.updateProjects(projects));
  }

  private void gotoAddProjectActivity() {
    Intent intent = new Intent(this, AddProjectActivity.class);
    startActivity(intent);
  }

  private void showDeleteProjectDialog(final Project project) {
    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
    normalDialog.setTitle("Deleting project");
    normalDialog.setMessage("Are you sure to delete " + project.getTitle() + "?");
    normalDialog.setPositiveButton(
        "Confirm", (dialog, which) -> deleteProject(project));
    normalDialog.setNegativeButton("Cancel", null);
    normalDialog.show();
  }

  private void deleteProject(Project project) {
    mProjectViewModel.deleteProject(project);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_project_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
