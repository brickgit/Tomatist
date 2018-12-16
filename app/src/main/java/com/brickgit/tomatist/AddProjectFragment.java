package com.brickgit.tomatist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brickgit.tomatist.data.Database;
import com.brickgit.tomatist.data.DatabaseLoader;
import com.brickgit.tomatist.data.Project;
import com.brickgit.tomatist.data.ProjectDao;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/** Created by Daniel Lin on 2018/12/15. */
public class AddProjectFragment extends Fragment {

  private TextInputEditText mNewProjectName;

  private Database mDatabase;

  public AddProjectFragment() {}

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_add_project, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    mDatabase = DatabaseLoader.getAppDatabase();
    mNewProjectName = view.findViewById(R.id.new_project_name);
  }

  private void addProject() {
    String newProjectName = mNewProjectName.getText().toString().trim();
    if (newProjectName.isEmpty()) {
      mNewProjectName.setError(getString(R.string.error_name_is_required));
      return;
    }

    Project project = new Project();
    project.setTitle(newProjectName);
    ProjectDao dao = mDatabase.projectDao();
    dao.insertProject(project);

    getActivity().finish();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
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
