package com.brickgit.tomatist.view.activity;

import android.os.Bundle;

import com.brickgit.tomatist.data.viewmodel.ActivityViewModel;
import com.brickgit.tomatist.data.viewmodel.CategoryViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseActivity extends AppCompatActivity {

  protected ActivityViewModel mActivityViewModel;
  protected CategoryViewModel mCategoryViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mActivityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);
    mCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
  }
}
