package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.Project;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/** Created by Daniel Lin on 2019/1/7. */
public class ProjectListViewModel extends ViewModel {

  private DataRepository mDataRepository;
  private LiveData<List<Project>> mProjects;

  public ProjectListViewModel() {
    mDataRepository = DataRepository.getInstance();
    if (mProjects != null) {
      return;
    }
    mProjects = mDataRepository.getProjects();
  }

  public LiveData<List<Project>> getProjects() {
    return mProjects;
  }
}
