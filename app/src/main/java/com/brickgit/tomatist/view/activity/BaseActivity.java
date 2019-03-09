package com.brickgit.tomatist.view.activity;

import android.os.Bundle;

import com.brickgit.tomatist.data.viewmodel.ActionViewModel;
import com.brickgit.tomatist.data.viewmodel.CategoryViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseActivity extends AppCompatActivity {

  protected ActionViewModel mActionViewModel;
  protected CategoryViewModel mCategoryViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mActionViewModel = ViewModelProviders.of(this).get(ActionViewModel.class);
    mCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
  }
}
