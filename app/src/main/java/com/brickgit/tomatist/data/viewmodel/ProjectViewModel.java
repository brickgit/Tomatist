package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;
import com.brickgit.tomatist.data.database.Project;
import com.brickgit.tomatist.data.database.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/** Created by Daniel Lin on 2019/1/7. */
public class ProjectViewModel extends ViewModel {

  private DataRepository mDataRepository;
  private LiveData<List<Project>> mProjects;
  private LiveData<List<Task>> mTasks;

  public ProjectViewModel() {
    mDataRepository = DataRepository.getInstance();
  }

  public void insertProject(Project project) {
    mDataRepository.insertProject(project);
  }

  public void deleteProject(Project project) {
    mDataRepository.deleteProject(project);
  }

  public LiveData<List<Project>> getProjects() {
    if (mProjects == null) {
      mProjects = mDataRepository.getProjects();
    }

    return mProjects;
  }

  public void insertTask(Task task) {
    mDataRepository.insertTask(task);
  }

  public LiveData<Task> getTask(Long taskId) {
    return mDataRepository.getTask(taskId);
  }

  public LiveData<List<Task>> getTasks() {
    if (mTasks == null) {
      mTasks = mDataRepository.getTasks();
    }

    return mTasks;
  }
}
