package com.brickgit.tomatist.view.activity;

import android.os.Bundle;

import com.brickgit.tomatist.data.viewmodel.ProjectViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseActivity extends AppCompatActivity {

  protected ProjectViewModel mViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);
  }
}
