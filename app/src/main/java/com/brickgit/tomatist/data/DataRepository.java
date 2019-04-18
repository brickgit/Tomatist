package com.brickgit.tomatist.data;

import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.ActionDao;
import com.brickgit.tomatist.data.database.Database;
import com.brickgit.tomatist.data.database.DatabaseLoader;
import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.data.database.TagDao;

import java.util.List;

import androidx.lifecycle.LiveData;

/** Created by Daniel Lin on 2019/1/7. */
public class DataRepository {

  private static volatile DataRepository INSTANCE;

  private ActionDao mActionDao;
  private TagDao mTagDao;

  public static DataRepository getInstance() {
    if (INSTANCE == null) {
      synchronized (DataRepository.class) {
        if (INSTANCE == null) INSTANCE = new DataRepository();
      }
    }
    return INSTANCE;
  }

  private DataRepository() {
    Database database = DatabaseLoader.getAppDatabase();
    mActionDao = database.actionDao();
    mTagDao = database.tagDao();
  }

  public void insertAction(Action action) {
    mActionDao.insertAction(action);
  }

  public void updateAction(Action action) {
    mActionDao.updateAction(action);
  }

  public void deleteAction(Action action) {
    mActionDao.deleteAction(action);
  }

  public LiveData<Action> getAction(String id) {
    return mActionDao.getAction(id);
  }

  public LiveData<List<Action>> getFinishedActions(int year) {
    return mActionDao.getFinishedActionsForYear(year);
  }

  public LiveData<List<Action>> getFinishedActions(int year, int month) {
    return mActionDao.getFinishedActionsForMonth(year, month);
  }

  public LiveData<List<Action>> getFinishedActions(int year, int month, int day) {
    return mActionDao.getFinishedActionsForDate(year, month, day);
  }

  public LiveData<List<Action>> getActions(int year, int month, int day) {
    return mActionDao.getActionsForDate(year, month, day);
  }

  public LiveData<List<Action>> getUnfinishedActions() {
    return mActionDao.getUnfinishedActions();
  }

  public LiveData<Tag> getTag(String id) {
    return mTagDao.getTag(id);
  }

  public LiveData<List<Tag>> getTags() {
    return mTagDao.getTags();
  }

  public LiveData<List<Tag>> getTags(List<String> tagListString) {
    return mTagDao.getTags(tagListString);
  }

  public void insertTag(Tag tag) {
    mTagDao.insertTag(tag);
  }

  public void insertTags(List<Tag> tags) {
    mTagDao.insertTags(tags);
  }

  public void deleteTag(Tag tag) {
    mTagDao.deleteTag(tag);
  }
}
