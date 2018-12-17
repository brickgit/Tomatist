package com.brickgit.tomatist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.brickgit.tomatist.data.Database;
import com.brickgit.tomatist.data.DatabaseLoader;
import com.brickgit.tomatist.data.Project;
import com.brickgit.tomatist.data.ProjectDao;
import com.brickgit.tomatist.view.projectlist.ProjectListAdapter;
import com.brickgit.tomatist.view.projectlist.ProjectListTouchHelperCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectListActivity extends AppCompatActivity {

  private RecyclerView mTaskList;
  private LinearLayoutManager mLayoutManager;
  private ProjectListAdapter mProjectListAdapter;

  private Database mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_project_list);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
          }
        });

    mTaskList = findViewById(R.id.project_list);
    mTaskList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(this);
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
    Intent intent = new Intent(this, AddProjectActivity.class);
    startActivity(intent);
  }

  private void showDeleteProjectDialog(final Project project) {
    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
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
