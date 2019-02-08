package com.brickgit.tomatist.view.activity;

import android.os.Bundle;

import com.brickgit.tomatist.data.viewmodel.ActivityViewModel;
import com.brickgit.tomatist.data.viewmodel.CategoryViewModel;
import com.brickgit.tomatist.data.viewmodel.ProjectViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseActivity extends AppCompatActivity {

  protected ProjectViewModel mProjectViewModel;
  protected ActivityViewModel mActivityViewModel;
  protected CategoryViewModel mCategoryViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mProjectViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);
    mActivityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);
    mCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
  }
}
