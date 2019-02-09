package com.brickgit.tomatist.data;

import com.brickgit.tomatist.data.database.Activity;
import com.brickgit.tomatist.data.database.ActivityDao;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryDao;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.database.CategoryGroupDao;
import com.brickgit.tomatist.data.database.Database;
import com.brickgit.tomatist.data.database.DatabaseLoader;
import com.brickgit.tomatist.data.database.Project;
import com.brickgit.tomatist.data.database.ProjectDao;
import com.brickgit.tomatist.data.database.TaskDao;

import java.util.List;

import androidx.lifecycle.LiveData;

/** Created by Daniel Lin on 2019/1/7. */
public class DataRepository {

  private static volatile DataRepository INSTANCE;

  private Database mDatabase;
  private ProjectDao mProjectDao;
  private TaskDao mTaskDao;
  private ActivityDao mActivityDao;
  private CategoryGroupDao mCategoryGroupDao;
  private CategoryDao mCategoryDao;

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
    mActivityDao = mDatabase.activityDao();
    mCategoryGroupDao = mDatabase.categoryGroupDao();
    mCategoryDao = mDatabase.categoryDao();
  }

  public void insertProject(Project project) {
    mProjectDao.insertProject(project);
  }

  public void deleteProject(Project project) {
    mProjectDao.deleteProject(project);
  }

  public LiveData<List<Project>> getProjects() {
    return mProjectDao.getProjects();
  }

  public void insertActivity(Activity activity) {
    mActivityDao.insertActivity(activity);
  }

  public void updateActivity(Activity activity) {
    mActivityDao.updateActivity(activity);
  }

  public void deleteActivity(Activity activity) {
    mActivityDao.deleteActivity(activity);
  }

  public LiveData<Activity> getActivity(long id) {
    return mActivityDao.getActivity(id);
  }

  public LiveData<List<Activity>> getActivities(int year, int month, int day) {
    return mActivityDao.getActivitiesForDate(year, month, day);
  }

  public long insertCategoryGroup(CategoryGroup categoryGroup) {
    return mCategoryGroupDao.insertCategoryGroup(categoryGroup);
  }

  public LiveData<CategoryGroup> getCategoryGroup(long categoryGroupId) {
    return mCategoryGroupDao.getCategoryGroup(categoryGroupId);
  }

  public LiveData<List<CategoryGroup>> getCategoryGroups() {
    return mCategoryGroupDao.getCategoryGroups();
  }

  public long[] insertCategories(List<Category> categories) {
    return mCategoryDao.insertCategories(categories);
  }

  public LiveData<Category> getCategory(long categoryId) {
    return mCategoryDao.getCategory(categoryId);
  }

  public LiveData<List<Category>> getCategories(long categoryGroupId) {
    return mCategoryDao.getCategories(categoryGroupId);
  }

  public LiveData<List<Category>> getCategories() {
    return mCategoryDao.getCategories();
  }
}
