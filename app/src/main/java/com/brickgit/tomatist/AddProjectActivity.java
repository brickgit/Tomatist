package com.brickgit.tomatist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.brickgit.tomatist.data.database.Project;
import com.brickgit.tomatist.data.viewmodel.ProjectViewModel;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by Daniel Lin on 2018/12/15.
 */
public class AddProjectActivity extends AppCompatActivity {

  private TextInputEditText mNewProjectName;

  private ProjectViewModel mViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_project);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.add_project);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    mNewProjectName = findViewById(R.id.new_project_name);

    mViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);
  }

  private void addProject() {
    String newProjectName = mNewProjectName.getText().toString().trim();
    if (newProjectName.isEmpty()) {
      mNewProjectName.setError(getString(R.string.error_name_is_required));
      return;
    }

    Project project = new Project();
    project.setTitle(newProjectName);
    mViewModel.insertProject(project);

    finish();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_add_project, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_add:
        addProject();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
