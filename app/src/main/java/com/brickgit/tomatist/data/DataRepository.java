package com.brickgit.tomatist.data;

import java.util.List;

import androidx.lifecycle.LiveData;

/** Created by Daniel Lin on 2019/1/7. */
public class DataRepository {

  private static volatile DataRepository INSTANCE;

  private Database mDatabase;
  private ProjectDao mProjectDao;
  private TaskDao mTaskDao;

  public static DataRepository getInstance() {
    if (INSTANCE == null) {
      synchronized (DataRepository.class) {
        if (INSTANCE == null) INSTANCE = new DataRepository();
      }
    }
    return INSTANCE;
  }

  private DataRepository() {
    mDatabase = DatabaseLoader.getAppDatabase();
    mProjectDao = mDatabase.projectDao();
    mTaskDao = mDatabase.taskDao();
  }

  public LiveData<List<Project>> getProjects() {
    return mProjectDao.getProjects();
  }
}
